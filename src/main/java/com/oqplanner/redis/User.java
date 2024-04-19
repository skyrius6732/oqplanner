package com.oqplanner.redis;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("user") // options: timeToLive = 10
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String id; // userId: 입력안하면 임의의 값 생성됨.

    private String name;
    private String major; // 전공: back, front
    private int age;

    //private List<String> skils; // List 필요시

}

