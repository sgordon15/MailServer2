package com.example.Mail.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Email
{
    private UUID from;
    private UUID to;
    private String message;
}