package com.oqplanner.chat;


import lombok.*;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("user") // options: timeToLive = 10
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private String flag;         // chatter, chat 구분
    private String userName;    // 사용자 이름
    private String userNo;      // 사용자 No
    private String date; // 채팅 메시지가 전송된 날짜와 시간
    private String text;        // 전송된 텍스트
    private Boolean systemMessage; // systemMessage 여부
}
