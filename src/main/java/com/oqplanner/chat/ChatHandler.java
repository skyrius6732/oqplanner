package com.oqplanner.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Log4j2
public class ChatHandler extends TextWebSocketHandler{

        private static List<WebSocketSession> list = new ArrayList<>();

        @Autowired
        private ChatService chatService;

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            String payload = message.getPayload();
            log.info("payload : " + payload);
            ObjectMapper objectMapper = new ObjectMapper();
            ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);

                String preDate = chatMessage.getDate();
                String keyDate = "";
                String[] value = preDate.split(" ");
                String[] yymmdd = value[0].split("-");
                String[] hhmmss = value[1].split(":");
                keyDate = yymmdd[0] + yymmdd[1] + yymmdd[2] + hhmmss[0] + hhmmss[1] + hhmmss[2];


                if("enter".equals(chatMessage.getFlag()) || "message".equals(chatMessage.getFlag())) {
                    // 입장 시 채팅자 redis 저장 webSocket send X
                    chatService.saveChatter(chatMessage);

                    // 입장시, 전송시 메세지 redis 저장
                    chatService.saveChatMessage(keyDate, chatMessage);

                }else if("exit".equals(chatMessage.getFlag())) {
                    // 퇴장 시 채팅자 redis 삭제 webSocket send X
                    chatService.deleteChatter(chatMessage);

                    // 입장시, 전송시 메세지 redis 저장
                    chatService.saveChatMessage(keyDate, chatMessage);
                }



                    // Message Send
                    //페이로드란 전송되는 데이터를 의미한다.
                    for (WebSocketSession sess : list) {
                        sess.sendMessage(message);  // webSocket 전달
                    }


        }
        /* Client가 접속 시 호출되는 메서드 */
        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            list.add(session);

            log.info(session + " 클라이언트 접속");
        }


        /* Client가 접속 해제 시 호출되는 메서드 */
        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
            log.info(session + " 클라이언트 접속 해제");
            list.remove(session);
        }
    }

