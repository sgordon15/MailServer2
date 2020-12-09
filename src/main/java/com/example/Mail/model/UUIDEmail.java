package com.example.Mail.model;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class UUIDEmail
{
    private UUID from;
    private String to;
    private String message;
}