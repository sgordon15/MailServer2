package com.example.Mail.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class messageInfo{
    private String sendKey;
    private String recipient;
    private String message;
}
