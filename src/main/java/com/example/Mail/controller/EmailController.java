package com.example.Mail.controller;

import com.example.Mail.service.EmailService;
import com.sun.nio.sctp.MessageInfo;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor

public class EmailController {
    @PostMapping("/login")
    public String getLogin(@RequestBody UserPassword userPassword){
        return EmailService.getUserPassword(userPassword);
    }
    @PostMapping("/send")
    public String sendMessage(@RequestBody MessageInfo messageInfo) {
        return EmailService.sendMessage(messageInfo);
    }

}

