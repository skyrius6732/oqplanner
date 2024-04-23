package com.oqplanner.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name="chat-controller",  description = "채팅 Controller 입니다.")
@RestController
@Log4j2
@RequestMapping("/trip/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/connect")
    public String chatGET(){
        log.info("@ChatController, chat GET()");
        return "chater";
    }

    @GetMapping("/message")
    public List<ChatMessage> getChatMessage(@RequestParam String date){
        return chatService.getChatMessage(date);
    }

    @GetMapping("/chatter")
    public List<ChatMessage> getChatter(){
        return chatService.getChatter();
    }




}
