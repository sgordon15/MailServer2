package com.example.Mail.controller;


import com.example.Mail.model.*;
import com.example.Mail.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/email")
public class EmailController
{
    private final EmailService emailService;

    @PostMapping("/login")
    public Object login(@RequestBody UserInformation userInformation)
    {
        return emailService.inboxLogin(userInformation);
    }

    @ResponseBody
    @PostMapping("/send")
    public Object sendEmail(@RequestBody UUIDEmail email)
    {
        return emailService.sendEmail(email);
    }

    @ResponseBody
    @PostMapping("/inbox")
    public ArrayList<InboxEmail> checkInbox(@RequestBody GetUUID primaryKey)
    {
        return emailService.checkInbox(primaryKey.getPrimaryKey());
    }

    @ResponseBody
    @PostMapping("/outbox")
    public ArrayList<OutboxEmail> checkOutbox(@RequestBody GetUUID primaryKey)
    {
        return emailService.checkOutbox(primaryKey.getPrimaryKey());
    }

    @PostMapping("/receiveExternalMail")
    public Object receiveExternalMail(@RequestBody ExternalEmail externalEmail,
                                      @RequestHeader ("api-key") String keyValue)
    {
        return emailService.receiveExternalMail(externalEmail, keyValue);
    }
}