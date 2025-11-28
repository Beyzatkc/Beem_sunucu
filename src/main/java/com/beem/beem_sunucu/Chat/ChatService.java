package com.beem.beem_sunucu.Chat;

import com.beem.beem_sunucu.Chat.ChatParticipant.ChatParticipant;
import com.beem.beem_sunucu.Chat.ChatParticipant.ChatParticipantRepository;
import com.beem.beem_sunucu.Chat.ChatParticipant.ChatRole;
import com.beem.beem_sunucu.Chat.ChatParticipant.ParticipantDTO;
import com.beem.beem_sunucu.Chat.RequestDTO.*;
import com.beem.beem_sunucu.Chat.ResponseDTO.AddUserGroupResponse;
import com.beem.beem_sunucu.Chat.ResponseDTO.DirectChatResponse;
import com.beem.beem_sunucu.Chat.ResponseDTO.GroupChatResponse;
import com.beem.beem_sunucu.Chat.ResponseDTO.RemoveUserGroupResponse;
import com.beem.beem_sunucu.Users.User;
import com.beem.beem_sunucu.Users.User_Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.saml2.Saml2RelyingPartyAutoConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class ChatService {
    private final ChatRepository chatRepo;
    private final User_Repo userRepo;
    private final ChatParticipantRepository participantRepo;

    @Autowired
    public ChatService(ChatRepository chatRepo,
                       User_Repo userRepo,
                       ChatParticipantRepository participantRepo){
        this.chatRepo = chatRepo;
        this.userRepo = userRepo;
        this.participantRepo = participantRepo;
    }

    @Transactional
    public DirectChatResponse createDirectChat(CreateDirectChatRequest dtoDirect){
        User me = userRepo.findById(dtoDirect.getMyId())
                .orElseThrow(() -> new IllegalArgumentException("User (myId) not found"));
        User target = userRepo.findById(dtoDirect.getTargetId())
                .orElseThrow(() -> new IllegalArgumentException("Target user not found"));
        List<ChatParticipant> myChat = participantRepo.findByUser(me);

        for(ChatParticipant cp: myChat){
            Chat chat = cp.getChat();
            if(chat.getChatType()==ChatType.DIRECT){
                if(
                        participantRepo.existsByChatAndUser(chat, me)
                ){
                    throw new IllegalStateException("Direct chat already exists between these users");
                }
            }
        }

        Chat newChat = new Chat(
                ChatType.DIRECT,
                dtoDirect.getTitle(),
                me
        );
        newChat.setDescription(dtoDirect.getDescription());
        newChat.setParticipantsSize(2);

        chatRepo.save(newChat);

        ChatParticipant meParticipant = new ChatParticipant(newChat, me, ChatRole.ADMIN);
        ChatParticipant targetParticipant = new ChatParticipant(newChat, target, ChatRole.MEMBER);
        participantRepo.save(meParticipant);
        participantRepo.save(targetParticipant);

        List<ChatParticipant> participants = new ArrayList<>();
        participants.add(meParticipant);
        participants.add(targetParticipant);
        List<ParticipantDTO> participantDTOS = participants.stream()
                .map(p -> new ParticipantDTO(
                        p.getUser().getId(),
                        p.getUser().getUsername(),
                        p.getRole(),
                        p.isMuted()
                )).toList();

        return new DirectChatResponse(
                newChat.getChatId(),
                newChat.getChatType(),
                newChat.getTitle(),
                newChat.getDescription(),
                newChat.getCreatedAt(),
                participantDTOS
        );
    }


    @Transactional
    public GroupChatResponse createGroupChat(CreateGroupChatRequest dtoGroup){
        User me = userRepo.findById(dtoGroup.getCreaterId())
                .orElseThrow(() -> new IllegalArgumentException("User (myId) not found"));
        Chat newChat = new Chat(
                ChatType.GROUP,
                dtoGroup.getTitle(),
                me
        );
        newChat.setDescription(dtoGroup.getDescription());
        newChat.setAvatarUrl(dtoGroup.getAvatarUrl());
        newChat.setMaxParticipants(dtoGroup.getMaxParticipants());


        List<ChatParticipant> chatParticipants = new ArrayList<>();
        List<User> users = userRepo.findAllByIdIn(dtoGroup.getParticipantIds());

        if (dtoGroup.getMaxParticipants() != null && users.size() > dtoGroup.getMaxParticipants()) {
            throw new IllegalArgumentException("Number of participants exceeds max allowed");
        }

        newChat.setParticipantsSize(users.size());

        newChat.setParticipantsSize(users.size());
        chatRepo.save(newChat);

        for (User user: users){
            ChatParticipant p = new ChatParticipant(
                    newChat,
                    user,
                    ChatRole.MEMBER
            );
            p.setAddedBy(me);
            chatParticipants.add(p);
        }
        ChatParticipant myP = new ChatParticipant(newChat, me, ChatRole.ADMIN);
        myP.setAddedBy(me);
        chatParticipants.add(myP);

        participantRepo.saveAll(chatParticipants);

        List<ParticipantDTO> participantDTOS = chatParticipants.stream()
                .map(p -> new ParticipantDTO(
                        p.getUser().getId(),
                        p.getUser().getUsername(),
                        p.getRole(),
                        p.isMuted()
                )).toList();

        return new GroupChatResponse(
                newChat.getChatId(),
                newChat.getChatType(),
                newChat.getTitle(),
                newChat.getDescription(),
                newChat.getAvatarUrl(),
                newChat.getMaxParticipants(),
                me.getId(),
                participantDTOS,
                newChat.getCreatedAt()
        );
    }

    @Transactional
    public AddUserGroupResponse addUserGroup(AddUserGroupRequest dto){

        if(dto.getParticipantIds() == null || dto.getParticipantIds().isEmpty()){
            throw new IllegalArgumentException("No participant IDs provided");
        }

        User me = userRepo.findById(dto.getAdderId())
                .orElseThrow(() -> new IllegalArgumentException("User (myId) not found"));

        Chat chat = chatRepo.findById(dto.getChatId())
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));
        if(chat.getChatType() != ChatType.GROUP){
            throw new IllegalArgumentException("This chat not group chat!");
        }

        ChatParticipant myP = participantRepo.findByChatAndUser(chat, me)
                .orElseThrow(() -> new IllegalArgumentException("You are not a participant of this chat"));

        if(myP.getRole() != ChatRole.ADMIN){
            throw new SecurityException("You don't have permission to add users to this chat");
        }

        List<User> users = userRepo.findAllByIdIn(dto.getParticipantIds());
        HashMap<Long, ChatParticipant> mapUsers = new HashMap<>();

        List<ChatParticipant> chatP = participantRepo.findAllByChatAndUserIn(chat, users);

        for(ChatParticipant cp: chatP){
            mapUsers.put(cp.getUser().getId(), cp);
        }

        List<User> newUsers = users.stream()
                .filter(user-> !mapUsers.containsKey(user.getId())
                        || mapUsers.get(user.getId()).getRole() == ChatRole.KICKED
                ).toList();


        if(chat.getMaxParticipants() != null && newUsers.size()+ chat.getParticipantsSize() > chat.getMaxParticipants()){
            throw new IllegalArgumentException("Number of participants exceeds max allowed");
        }
        chat.setParticipantsSize(chat.getParticipantsSize() + newUsers.size());
        chatRepo.save(chat);

        List<ChatParticipant> participants = newUsers.stream()
                .map(user ->{
                    ChatParticipant p = new ChatParticipant(
                            chat,
                            user,
                            ChatRole.GUEST
                    );
                    p.setAddedBy(me);
                    p.setMuted(false);
                    return p;
                }).toList();
        participantRepo.saveAll(participants);
        participantRepo.flush();


        return new AddUserGroupResponse(
                chat.getChatId(),
                chat.getTitle(),
                me.getId(),
                newUsers.stream().map(u->u.getId()).toList(),
                chat.getParticipantsSize(),
                "Users added successfully"
        );
    }

    @Transactional
    public RemoveUserGroupResponse removeUserGroup(RemoveUserGroupRequest dto){
        if(dto.getParticipantIds() == null || dto.getParticipantIds().isEmpty()){
            throw new IllegalArgumentException("No participant IDs provided");
        }

        User me = userRepo.findById(dto.getRemoverId())
                .orElseThrow(() -> new IllegalArgumentException("User (myId) not found"));
        Chat chat = chatRepo.findById(dto.getChatId())
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));
        if(chat.getChatType() != ChatType.GROUP){
            throw new IllegalArgumentException("This chat not group chat!");
        }

        ChatParticipant myP = participantRepo.findByChatAndUser(chat, me)
                .orElseThrow(() -> new IllegalArgumentException("You are not a participant of this chat"));

        if(myP.getRole() != ChatRole.ADMIN){
            throw new SecurityException("You don't have permission to add users to this chat");
        }

        String message = "Users removed successfully";

        if(dto.getParticipantIds().contains(myP.getUser().getId())){
            dto.getParticipantIds().remove(myP.getUser().getId());
            message = "Users except admin removed successfully";
        }

        List<ChatParticipant> chatPList = participantRepo.findAllByChatAndUserIdIn(chat, dto.getParticipantIds());

        chat.setParticipantsSize(
                chat.getParticipantsSize() - chatPList.size()
        );

        for(ChatParticipant cp: chatPList){
            cp.setRemovedBy(me);
            cp.setMuted(true);
            cp.setRole(ChatRole.KICKED);
            cp.setRemovedTime(LocalDateTime.now());
        }

        participantRepo.saveAll(chatPList);
        participantRepo.flush();

        chatRepo.save(chat);

        return new RemoveUserGroupResponse(
                chat.getChatId(),
                chat.getTitle(),
                me.getId(),
                chatPList.stream().map(p->p.getUser().getId()).toList(),
                chat.getParticipantsSize(),
                message
        );
    }

    @Transactional
    public String roleUpdate(RoleUpdateRequest dto){
        User me = userRepo.findById(dto.getMyId())
                .orElseThrow(() -> new IllegalArgumentException("User (myId) not found"));
        Chat chat = chatRepo.findById(dto.getChatId())
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));
        ChatParticipant myCp = participantRepo.findByChatAndUser(chat, me)
                .orElseThrow(() -> new IllegalArgumentException("You are not a participant of this chat"));

        if(myCp.getRole() != ChatRole.ADMIN){
            throw new SecurityException("You don't have permission to update user roles in this chat");
        }

        int result = participantRepo.updateUserRole(dto.getChatId(), dto.getUserId(), dto.getRole());

        if (result == 0) {
            throw new IllegalArgumentException("User not found in this chat or role unchanged");
        }

        return "User role updated successfully";
    }

    @Transactional
    public void exitGroup(Long exitId, Long chatId){
        User me = userRepo.findById(exitId)
                .orElseThrow(() -> new IllegalArgumentException("User (myId) not found"));
        Chat chat = chatRepo.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));
        if(chat.getChatType() != ChatType.GROUP){
            throw new IllegalArgumentException("This chat not group chat!");
        }

        ChatParticipant myP = participantRepo.findByChatAndUser(chat, me)
                .orElseThrow(() -> new IllegalArgumentException("You are not a participant of this chat"));

        if(myP.getRole() == ChatRole.KICKED){
            throw new IllegalArgumentException("You are exiting this group");
        }

        if(myP.getRole() == ChatRole.ADMIN && participantRepo.countByChatAndRole(chat, ChatRole.ADMIN) == 1){
            throw new IllegalArgumentException("This group have a one admin. You choose the select admin.");
        }

        myP.setRemovedBy(me);
        myP.setMuted(true);
        myP.setRole(ChatRole.KICKED);
        myP.setRemovedTime(LocalDateTime.now());
        chat.setParticipantsSize(
                chat.getParticipantsSize() - 1
        );

        participantRepo.save(myP);

        chatRepo.save(chat);
    }

    @Transactional
    public List<Object> allMyChat(Long myId){

        List<Object> allChat = new ArrayList<>();

        User me = userRepo.findById(myId)
                .orElseThrow(() -> new IllegalArgumentException("User (myId) not found"));

        List<ChatParticipant> myP = participantRepo.findByUserAndChatDeleted(me, Boolean.FALSE);

        for (ChatParticipant cp: myP){
            if(cp.getChat().getChatType() == ChatType.GROUP){
                GroupChatResponse groupC = new GroupChatResponse(
                        cp.getChat().getChatId(),
                        cp.getChat().getChatType(),
                        cp.getChat().getTitle(),
                        cp.getChat().getDescription(),
                        cp.getChat().getAvatarUrl(),
                        cp.getChat().getMaxParticipants(),
                        cp.getChat().getCreatedBy().getId(),
                        participantRepo.findByChat(cp.getChat()).stream()
                                .map(ParticipantDTO::new).toList(),
                        cp.getChat().getCreatedAt()
                );
                allChat.add(groupC);
            }
            else{
                String title;
                String chatProfile;
                List<ChatParticipant> participants = participantRepo.findByChat(cp.getChat());
                if(!participants.get(0).getUser().getId().equals(me.getId())){
                    title = participants.get(0).getUser().getUsername();
                    chatProfile = participants.get(0).getUser().getProfile();
                }
                else {
                    title = participants.get(1).getUser().getUsername();
                    chatProfile = participants.get(1).getUser().getProfile();
                }

                DirectChatResponse directC = new DirectChatResponse(
                        cp.getChat().getChatId(),
                        cp.getChat().getChatType(),
                        title,
                        cp.getChat().getDescription(),
                        cp.getChat().getCreatedAt(),
                        participants.stream()
                                .map(ParticipantDTO::new).toList()
                );
                directC.setChatProfile(chatProfile);
                allChat.add(directC);
            }
        }
        return allChat;
    }

    @Transactional
    public void deleteChat(Long myId, Long chatId){
        User me = userRepo.findById(myId)
                .orElseThrow(() -> new IllegalArgumentException("User (myId) not found"));
        Chat chat = chatRepo.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));
        ChatParticipant myP = participantRepo.findByChatAndUser(chat, me)
                .orElseThrow(() -> new IllegalArgumentException("You are not a participant of this chat"));

        if(myP.getChatDeleted()){
            throw new IllegalArgumentException("This chat has already been deleted");
        }

        myP.setChatDeleted(Boolean.TRUE);
        myP.setJoinedAt(LocalDateTime.now());
        participantRepo.save(myP);
    }
}
