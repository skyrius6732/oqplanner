package com.oqplanner.redis;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class RedisController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostMapping("/redisTest")
    public ResponseEntity<?> addRedisKey(){
        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        vop.set("202404221523", "{'banana':'yellow, 'red':'apple'}");
        vop.set("202404221623", "{'peach':'bunhong', 'watermellon':'green'}");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/redisTest/{key}")
    public ResponseEntity<?> getRedisKey(@PathVariable String key){
        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        String value = vop.get(key);
        System.out.println("value ::: " + value);
        return new ResponseEntity<>(value,HttpStatus.OK);
    }


    @GetMapping("/redisTestAll/{previousDate}")
    public void getChatMessagesFromPreviousDate(@PathVariable String previousDate) {
        Set<String> keys = redisTemplate.keys(previousDate + "*");
        System.out.println("keys ::: " + keys);
        for (String key:keys) {
            this.getRedisKey(key);
        }
    }
}
