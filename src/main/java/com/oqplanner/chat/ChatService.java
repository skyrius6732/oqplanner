package com.oqplanner.chat;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Service
public class ChatService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    // userNo를 통한 채팅자 리스트 관리
    /*public void saveChatter(ChatMessage chatMessage){
        try{
            String key = chatMessage.getUserNo();
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(chatMessage);
            redisTemplate.opsForValue().set(key, json);

            // 포맷을 지정하여 현재 날짜를 yyMMddHHmmss 형식으로 변환
            LocalDateTime date = LocalDateTime.parse(chatMessage.getDate(), DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"));
            // Sorted Set에 시간순으로 키를 추가
            redisTemplate.opsForZSet().add("chatters", key, date.toEpochSecond(ZoneOffset.UTC));
        }catch(JsonProcessingException e) {
            e.printStackTrace();
        }
    }*/

    // webSocket 세션 아이디를 통한 채팅자 리스트 관리
    public void saveChatter(String sessionId, ChatMessage chatMessage) {
        String key = sessionId;
        try {
            if (chatMessage == null) { // webSocket 클라이언트 접속 시
                // 포맷을 지정하여 현재 날짜를 yyMMddHHmmss 형식으로 변환
                String formatDate = new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date());
                LocalDateTime date = LocalDateTime.parse(formatDate, DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"));
                // Sorted Set에 시간순으로 키를 추가
                redisTemplate.opsForZSet().add("chatters", key, date.toEpochSecond(ZoneOffset.UTC));
            } else {  // 접속 후 onMessage(user정보 있음)
//            String key = chatMessage.getUserNo();
                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(chatMessage);
                redisTemplate.opsForValue().set(key, json);
            }
        }catch(JsonProcessingException e) {
            e.printStackTrace();
        }



    }

//    public void deleteChatter(ChatMessage chatMessage) {
//        // 실제 저장된 key 값들을 삭제
//        redisTemplate.delete(chatMessage.getUserNo());
//        // chatters 항목 중 해당 key값 삭제
//        redisTemplate.opsForZSet().remove("chatters", chatMessage.getUserNo());
//    }


    public void deleteChatter(String sessionId) {
        // 실제 저장된 key 값들을 삭제
        redisTemplate.delete(sessionId);
        // chatters 항목 중 해당 key값 삭제
        redisTemplate.opsForZSet().remove("chatters", sessionId);
    }

    public List<ChatMessage> getChatter(){
        List<ChatMessage> chatterList = new ArrayList<>();
        List<String> sortedKeys;
        String json = "";
        sortedKeys = getChatterKeys();
        for (String key : sortedKeys) {
            json = redisTemplate.opsForValue().get(key);
            if (json != null) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    chatterList.add(objectMapper.readValue(json, ChatMessage.class));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
        return chatterList;

    }


    public void saveChatMessage(String key, ChatMessage chatMessage) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(chatMessage);
            redisTemplate.opsForValue().set(key, json);
            // 일주일(604800s) 이후 삭제
            // 여기서 만료시간을 줘도 되고
//            redisTemplate.opsForValue().set(key, json, 604800, TimeUnit.SECONDS);

            // 포맷을 지정하여 현재 날짜를 yyMMddHHmmss 형식으로 변환
            LocalDateTime date = LocalDateTime.parse(chatMessage.getDate(), DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss"));
            // Sorted Set에 시간순으로 키를 추가
            redisTemplate.opsForZSet().add("chatMessages", key, date.toEpochSecond(ZoneOffset.UTC));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public List<ChatMessage> getChatMessage(String date) {
        List<ChatMessage> chatMessages = new ArrayList<>();
        List<String> sortedKeys;
        String json = "";


        if("".equals(date)) {
            sortedKeys = getChatMessageKeysInChronologicalOrder();
            for (String key : sortedKeys) {
                json = redisTemplate.opsForValue().get(key);
                if (json != null) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        chatMessages.add(objectMapper.readValue(json, ChatMessage.class));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{  // 아래 주석 풀면 구현 가능 (날짜별 조회)
            Set<String> keys = this.getChatMessagesFromPreviousDate(date);
            for (String key : keys) {
                json = redisTemplate.opsForValue().get(key);
                if (json != null) {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        chatMessages.add(objectMapper.readValue(json, ChatMessage.class));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return chatMessages;
    }

    public Set<String> getChatMessagesFromPreviousDate(String previousDate) {
        Set<String> keys = redisTemplate.keys(previousDate + "*");
        return keys;
    }

    public List<String> getChatMessageKeysInChronologicalOrder() {
        Set<String> keys = redisTemplate.opsForZSet().range("chatMessages", 0, -1);
        return new ArrayList<>(keys);
    }

    public List<String> getChatterKeys() {
        Set<String> keys = redisTemplate.opsForZSet().range("chatters", 0, -1);
        return new ArrayList<>(keys);
    }

    public void deleteKeysOlderThanOneWeek() {

        System.out.println("deleteKeysOlderThanOneWeek Method");
        // 현재 날짜 및 일주일 전 날짜 계산
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneWeekAgo = now.minusWeeks(1);
        LocalDateTime oneMinute = now.minusMinutes(1);

        // ZSet에서 일주일 이전의 키 값 조회
         Set<String> keysToDelete = redisTemplate.opsForZSet().rangeByScore("chatMessages", 0, oneWeekAgo.toEpochSecond(ZoneOffset.UTC));

//        Set<String> keysToDelete = redisTemplate.opsForZSet().rangeByScore("chatMessages", 0, oneMinute.toEpochSecond(ZoneOffset.UTC));


        for (Object key : keysToDelete) {
            // 실제 저장된 key 값들을 삭제
            redisTemplate.delete(key.toString());
            // 저장된 opsForZSet(chatMessages) 안의 내용 key 값들 삭제
            redisTemplate.opsForZSet().remove("chatMessages", key);
        }
    }





}
