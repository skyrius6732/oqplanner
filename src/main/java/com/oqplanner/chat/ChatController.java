package com.oqplanner.chat;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="chat-controller",  description = "채팅 Controller 입니다.")
@RestController
@Log4j2
@RequestMapping("/trip/chat")
public class ChatController {
    @GetMapping("/connect")
    public String chatGET(){
        log.info("@ChatController, chat GET()");
        return "chater";
    }
}
