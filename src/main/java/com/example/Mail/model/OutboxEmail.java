package com.example.Mail.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OutboxEmail
{
    private String to;
    private String message;
}