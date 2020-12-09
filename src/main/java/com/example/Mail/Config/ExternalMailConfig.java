package com.example.Mail.Config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@RequiredArgsConstructor
@Configuration
@ConfigurationProperties("mail.external")
public class ExternalMailConfig
{
    private String ip;
    private String apiKey;
}