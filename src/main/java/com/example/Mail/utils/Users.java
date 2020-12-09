package com.example.Mail.utils;

import com.example.Mail.model.UserInformation;
import lombok.Data;
import org.apache.catalina.User;
import org.apache.catalina.UserDatabase;

import java.util.HashMap;
import java.util.UUID;

@Data
public class Users
    {
        public HashMap<UUID, UserInformation> userDatabase = new HashMap<>();
        public HashMap<UUID, UserInformation> getUserDatabase(){
            return this.userDatabase;
        }
        public Users()
    {

            userDatabase.put(UUID.randomUUID(), new UserInformation("SaraGordon", "Hello"));
            userDatabase.put(UUID.randomUUID(), new UserInformation("HadassahGordon", "Hello2"));
            userDatabase.put(UUID.randomUUID(), new UserInformation("RivkaGordon", "Hello3"));
            userDatabase.put(UUID.randomUUID(), new UserInformation("ChanaGordon", "Hello4"));
        }

    }





