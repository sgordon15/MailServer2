package com.example.Mail.service;


import com.example.Mail.controller.UserPassword;
import com.example.Mail.utils.Users;
import com.sun.nio.sctp.MessageInfo;

public class EmailService {
    public static String getUserPassword( UserPassword userPassword){
      if(Users.userMap.containsKey(userPassword)) {
          return (Users.userMap.get(userPassword)).toString();
      }
      else
          return "401 unauthorized";
      }

    public static  String sendMessage(MessageInfo messageInfo) {
        if (Users.userMap.containsKey(getUserPassword())){

        }


    }
}


