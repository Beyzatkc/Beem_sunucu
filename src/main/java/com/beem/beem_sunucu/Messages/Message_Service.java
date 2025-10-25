package com.beem.beem_sunucu.Messages;

import com.beem.beem_sunucu.Users.CustomExceptions;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class Message_Service {
    private final Message_Repo messageRepo;
    private final RedisTemplate<String, List<Message_DTO_Response>>redisTemplate;
    private final  KafkaTemplate<String, Message_DTO_Response> kafkaTemplate;
    private final MongoTemplate mongoTemplate;
    private final Message_Archive_Repo messageArchiveRepo;

    private final String MESSAGE_TOPIC = "message-topic";
    private final String MESSAGE_READ_TOPIC ="message-read-topic";
    private final String REDIS_KEY_PREFIX = "chat:";

    public Message_Service(Message_Repo messageRepo, RedisTemplate<String, List<Message_DTO_Response>> redisTemplate, KafkaTemplate<String, Message_DTO_Response> kafkaTemplate, MongoTemplate mongoTemplate, Message_Archive_Repo messageArchiveRepo) {
        this.messageRepo = messageRepo;
        this.redisTemplate = redisTemplate;
        this.kafkaTemplate = kafkaTemplate;
        this.mongoTemplate = mongoTemplate;
        this.messageArchiveRepo = messageArchiveRepo;
    }

    public Message_DTO_Response sendMessage(Message_DTO_Request messageDtoRequest){
        Message message=new Message();
        message.setChat_id(messageDtoRequest.getChat_id());
        message.setContent(messageDtoRequest.getContent());
        message.setUserDTOSender(messageDtoRequest.getUserDTOSender());

        Message savedMessage=messageRepo.save(message);

        String redisKey=REDIS_KEY_PREFIX + messageDtoRequest.getChat_id()+ ":messages";
        List<Message_DTO_Response>cached=redisTemplate.opsForValue().get(redisKey);

        Message_DTO_Response messageDtoResponse=new Message_DTO_Response(savedMessage);
        if (cached != null){
            cached.add(0, messageDtoResponse);
            if (cached.size() > 100) cached = cached.subList(0, 100);
            redisTemplate.opsForValue().set(redisKey, cached);
        } else {
            redisTemplate.opsForValue().set(redisKey, new ArrayList<>(List.of(messageDtoResponse)));
        }
        try {
            kafkaTemplate.send(MESSAGE_TOPIC, messageDtoResponse);
        } catch (Exception e) {
            throw new CustomExceptions.ServiceException("Mesaj Kafka'ya gönderilemedi: " + e.getMessage());
        }

        return messageDtoResponse;
    }

    public List<Message_DTO_Response>getMessages(Long chatId){
        String redisKey=REDIS_KEY_PREFIX + chatId + ":messages";
        List<Message_DTO_Response>cached=redisTemplate.opsForValue().get(redisKey);
        if(cached!=null){
            return cached;
        }
        List<Message>messages=messageRepo.findTop100ByChatIdOrderBySentAtDesc(chatId);
        if (messages.isEmpty()) {
            throw new CustomExceptions.NotFoundException("Bu sohbet için mesaj bulunamadı.");
        }
        List<Message_DTO_Response> dtos= messages.stream().map(message -> new Message_DTO_Response(message)).toList();
        redisTemplate.opsForValue().set(redisKey, dtos);
        return dtos;
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
    public List<Message_DTO_Response> getOlderMessages(Long chatId, LocalDateTime lastMessageTime, int limit){
        Pageable pageable = PageRequest.of(0, limit);

        Page<Message> olderMessages = messageRepo.findByChatIdAndSentAtBeforeOrderBySentAtDesc(chatId, lastMessageTime, pageable);
        if (!olderMessages.isEmpty()) {
            return olderMessages.stream().map(Message_DTO_Response::new).toList();
        }
        Page<Message_Archive> olderArchive = messageArchiveRepo.findByChatIdAndSentAtBeforeOrderBySentAtDesc(chatId, lastMessageTime, pageable);
        if (olderArchive.isEmpty()) {
            throw new CustomExceptions.NotFoundException("Daha eski mesaj bulunamadı.");
        }
        return olderArchive.stream().map(Message_DTO_Response::new).toList();
    }

    public void markAsRead(Long messageId,String name){
        Message message=messageRepo.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Mesaj bulunamadı"));

        if(!message.getReadBy().contains(name)){
            Query query = new Query(Criteria.where("_id").is(messageId));
            Update update = new Update().addToSet("readBy", name);
            mongoTemplate.updateFirst(query, update, Message.class);
            String redisKey=REDIS_KEY_PREFIX + message.getChat_id() + ":messages";
            List<Message_DTO_Response> cached = redisTemplate.opsForValue().get(redisKey);

            if (cached != null) {
                for (Message_DTO_Response dto : cached) {
                    if (dto.getId().equals(messageId)) {
                        dto.getReadBy().add(name);
                        break;
                    }
                }
                redisTemplate.opsForValue().set(redisKey, cached);
            }
            message.getReadBy().add(name);
            Message_DTO_Response dtos = new Message_DTO_Response(message);
            kafkaTemplate.send(MESSAGE_READ_TOPIC,dtos);
        }
    }
}
