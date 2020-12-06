package com.example.Mail.utils;

import com.example.Mail.controller.UserPassword;
import lombok.Data;

import java.util.HashMap;
import java.util.UUID;

@Data
public class Users {


    public static HashMap<UserPassword,String> userMap = mapUsers();

    public static HashMap<UserPassword,String>mapUsers(){
        HashMap<UserPassword,String> tempMap = new HashMap<>();
        tempMap.put(new UserPassword("Sara","password"),UUID.randomUUID().toString());
        tempMap.put( new UserPassword("NoName","password"), UUID.randomUUID().toString());
        tempMap.put( new UserPassword("Trump","2020"), UUID.randomUUID().toString());

        return tempMap;
    }
}
