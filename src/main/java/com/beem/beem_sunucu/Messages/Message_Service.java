package com.beem.beem_sunucu.Messages;

import com.beem.beem_sunucu.Users.CustomExceptions;
import com.beem.beem_sunucu.Users.User;
import com.beem.beem_sunucu.Users.User_Repo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class Message_Service {
    private final Message_Repo messageRepo;
    private final User_Repo userRepo;
    RedisTemplate<String, String> redisTemplate;
    private final  KafkaTemplate<String, Message_DTO_Response> kafkaTemplate;
    private final MongoTemplate mongoTemplate;
    private final Message_Archive_Repo messageArchiveRepo;
    private ObjectMapper objectMapper;

    private final String MESSAGE_TOPIC = "message-topic";
    private final String MESSAGE_READ_TOPIC ="message-read-topic";
    private final String MESSAGE_DELETE_TOPIC="message-delete-topic";
    private final String REDIS_KEY_PREFIX = "chat:";

    public Message_Service(Message_Repo messageRepo, User_Repo userRepo, RedisTemplate<String, String> redisTemplate, KafkaTemplate<String, Message_DTO_Response> kafkaTemplate, MongoTemplate mongoTemplate, Message_Archive_Repo messageArchiveRepo,ObjectMapper objectMapper) {
        this.messageRepo = messageRepo;
        this.userRepo = userRepo;
        this.redisTemplate = redisTemplate;
        this.kafkaTemplate = kafkaTemplate;
        this.mongoTemplate = mongoTemplate;
        this.messageArchiveRepo = messageArchiveRepo;
        this.objectMapper=objectMapper;
    }

    @Transactional
    public Message_DTO_Response sendMessage(Message_DTO_Request messageDtoRequest) {

        Message message = new Message();
        message.setChatId(messageDtoRequest.getChatId());
        message.setContent(messageDtoRequest.getContent());
        message.setUserDTOSender(messageDtoRequest.getUserDTOSender());
        message.setSentAt(LocalDateTime.now());

        Message savedMessage = messageRepo.save(message);

        if (savedMessage.getId() == null) {
            throw new RuntimeException("MongoDB kaydı başarısız!");
        }
        Message_DTO_Response messageDtoResponse = new Message_DTO_Response(savedMessage);

        String redisKey = REDIS_KEY_PREFIX + messageDtoRequest.getChatId() + ":messages";

        try {
            String json = objectMapper.writeValueAsString(messageDtoResponse);

            redisTemplate.opsForList().leftPush(redisKey, json);
            redisTemplate.opsForList().trim(redisKey, 0, 99);

            System.out.println("Redis listesine kaydedildi. Toplam mesaj: " +
                    redisTemplate.opsForList().size(redisKey));

        } catch (Exception e) {
            System.out.println("Redis kaydı sırasında hata oluştu!");
            e.printStackTrace();
        }

        try {
            kafkaTemplate.send(MESSAGE_TOPIC, messageDtoResponse);
        } catch (Exception e) {
            throw new CustomExceptions.ServiceException("Mesaj Kafka'ya gönderilemedi: " + e.getMessage());
        }

        return messageDtoResponse;
    }


    public List<Message_DTO_Response> getMessages(Long chatId, Long currentUserId) {

        String redisKey = REDIS_KEY_PREFIX + chatId + ":messages";
        List<String> cachedList = redisTemplate.opsForList().range(redisKey, 0, -1);

        if (cachedList != null && !cachedList.isEmpty()) {
            try {
                List<Message_DTO_Response> messages = new ArrayList<>();

                for (String json : cachedList) {
                    Message_DTO_Response dto =
                            objectMapper.readValue(json, Message_DTO_Response.class);
                    if (!dto.getMessagesDeleteUser().contains(currentUserId)) {
                        messages.add(dto);
                    }
                }
                System.out.println("Redisten geldi: " + messages.size() + " mesaj");
                return messages;

            } catch (Exception e) {
                System.out.println("Redis verisi okunamadı, MongoDB'den çekiliyor...");
                e.printStackTrace();
            }
        }
        List<Message> messages = messageRepo.findTop100ByChatIdOrderBySentAtDesc(chatId);

        if (messages.isEmpty()) {
            throw new CustomExceptions.NotFoundException("Bu sohbet için mesaj bulunamadı.");
        }

        List<Message_DTO_Response> dtoList = messages.stream()
                .filter(m -> !m.getMessagesDeleteUser().contains(currentUserId))
                .map(Message_DTO_Response::new)
                .toList();

        try {
            for (Message_DTO_Response dto : dtoList) {
                String json = objectMapper.writeValueAsString(dto);
                redisTemplate.opsForList().leftPush(redisKey, json);
            }
            redisTemplate.opsForList().trim(redisKey, 0, 99);

        } catch (Exception e) {
            System.out.println("Mongo'dan gelen veri Redis'e yazılamadı!");
            e.printStackTrace();
        }

        return dtoList;
    }

    @Scheduled(cron = "0 0 3 ? * 7")
   public void archiveOldMessages(){
        LocalDateTime oneYearAgo=LocalDateTime.now().minusYears(1);
        List<Message>oldMessages = messageRepo.findBySentAtBefore(oneYearAgo);
        if(!oldMessages.isEmpty()){
            List<Message_Archive> archives=oldMessages.stream().map(message -> new Message_Archive(message)) .toList();
            messageArchiveRepo.saveAll(archives);
            messageRepo.deleteAll(oldMessages);
        }
   }
    public List<Message_DTO_Response> getOlderMessages(Long chatId, LocalDateTime lastMessageTime, int limit,Long currentId){
        Pageable pageable = PageRequest.of(0, limit);

        Page<Message> olderMessages = messageRepo.findMessages(chatId, lastMessageTime, Collections.singletonList(currentId), pageable);
        if (!olderMessages.isEmpty()) {
            return olderMessages.stream().map(Message_DTO_Response::new).toList();
        }
        Page<Message_Archive> olderArchive = messageArchiveRepo.findMessages(chatId, lastMessageTime,Collections.singletonList(currentId), pageable);
        if (olderArchive.isEmpty()) {
            throw new CustomExceptions.NotFoundException("Daha eski mesaj bulunamadı.");
        }
        return olderArchive.stream().map(Message_DTO_Response::new).toList();
    }

    @Transactional
    public void markAsRead(String messageId, String name) {

        Message message = messageRepo.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Mesaj bulunamadı"));

        if (message.getReadBy().contains(name)) {
            return;
        }
        Query query = new Query(Criteria.where("_id").is(messageId));
        Update update = new Update().addToSet("readBy", name);
        mongoTemplate.updateFirst(query, update, Message.class);

        String redisKey = REDIS_KEY_PREFIX + message.getChatId() + ":messages";

        try {
            List<String> cachedList = redisTemplate.opsForList().range(redisKey, 0, -1);

            if (cachedList != null && !cachedList.isEmpty()) {
                for (int i = 0; i < cachedList.size(); i++) {

                    String json = cachedList.get(i);

                    Message_DTO_Response dto =
                            objectMapper.readValue(json, Message_DTO_Response.class);

                    if (dto.getId().equals(messageId)) {
                        dto.getReadBy().add(name);
                        String updatedJson = objectMapper.writeValueAsString(dto);
                        redisTemplate.opsForList().set(redisKey, i, updatedJson);

                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Redis güncelleme sırasında hata oluştu!");
            e.printStackTrace();
        }

        message.getReadBy().add(name);
        Message_DTO_Response dto = new Message_DTO_Response(message);
        kafkaTemplate.send(MESSAGE_READ_TOPIC, dto);
    }

    @Transactional
    public void deleteFromEveryone(String messageId, Long currentUserId) {
        Message message = messageRepo.findById(messageId)
                .orElse(null);

        Message_Archive oldMessage = messageArchiveRepo.findById(messageId)
                .orElse(null);

        User user = userRepo.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı."));

        if (message != null && !message.getUserDTOSender().getUserId().equals(user.getId())) {
            throw new RuntimeException("Sadece mesajı gönderen herkesten silebilir.");
        }
        if (oldMessage != null && !oldMessage.getUserDTOSender().getUserId().equals(user.getId())) {
            throw new RuntimeException("Sadece mesajı gönderen herkesten silebilir.");
        }

        String redisKey = REDIS_KEY_PREFIX + (message != null ? message.getChatId() : oldMessage.getChatId()) + ":messages";

        try {
            List<String> cachedList = redisTemplate.opsForList().range(redisKey, 0, -1);

            if (cachedList != null && !cachedList.isEmpty()) {

                for (int i = 0; i < cachedList.size(); i++) {

                    String json = cachedList.get(i);
                    Message_DTO_Response dto = objectMapper.readValue(json, Message_DTO_Response.class);

                    if (dto.getId().equals(messageId)) {
                        redisTemplate.opsForList().remove(redisKey, 1, json);

                        break;
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Redis silme sırasında hata oluştu!");
            e.printStackTrace();
        }
        if (message != null) {
            Message_DTO_Response dto = new Message_DTO_Response(message);
            kafkaTemplate.send(MESSAGE_DELETE_TOPIC, dto);
        } else if (oldMessage != null) {
            Message_DTO_Response dto = new Message_DTO_Response(oldMessage);
            kafkaTemplate.send(MESSAGE_DELETE_TOPIC, dto);
        }
        if (message != null) {
            messageRepo.delete(message);
        }
        if (oldMessage != null) {
            messageArchiveRepo.delete(oldMessage);
        }
    }

    @Transactional
    public void deleteFromMe(String messageId,Long currentUserId){
        Message message=messageRepo.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Mesaj bulunamadı"));
        Message_Archive oldMessage=messageArchiveRepo.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Mesaj bulunamadı"));

        Query query = new Query(Criteria.where("_id").is(messageId));
        Update update = new Update().addToSet("messagesDeleteUser", currentUserId); // addToSet: tekrar eklemez
        mongoTemplate.updateFirst(query, update, Message.class);
    }
}
