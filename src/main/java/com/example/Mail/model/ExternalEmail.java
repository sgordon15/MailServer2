package com.example.Mail.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExternalEmail
{
    private String from;
    private String to;
    private String message;
}