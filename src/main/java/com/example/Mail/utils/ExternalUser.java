package com.example.Mail.utils;
import lombok.Data;

import java.util.HashMap;
import java.util.UUID;

@Data

public class ExternalUser {
    public static final HashMap<String, UUID> externalUserDatabase = new HashMap<>();

        public void ExternalUsers()
        {

            externalUserDatabase.put("testExternal", UUID.randomUUID());
        }
    }

