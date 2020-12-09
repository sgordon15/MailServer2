package com.example.Mail.model;

import com.example.Mail.model.Email;
import lombok.Data;

import java.util.ArrayList;

@Data
public class UserInformation
{
    private String userName;
    private String password;
    private ArrayList<Email> emailInbox;
    private ArrayList<Email> emailOutbox;

    public UserInformation(String userName, String password)
    {
        this.userName = userName;
        this.password = password;
        emailInbox = new ArrayList<>();
        emailOutbox = new ArrayList<>();
    }

    public void updateEmails(Email email, String inbOut)
    {
        if (inbOut.equalsIgnoreCase("inbox")) {
            this.emailInbox.add(0, email);
        } else {
            this.emailOutbox.add(0, email);
        }
    }

    public void printEmails(String inbOut) {
        if (inbOut.equalsIgnoreCase("inbox")) {
            printEmails(this.emailInbox);
        } else {
            printEmails(this.emailOutbox);
        }
    }

    private void printEmails(ArrayList<Email> emailList)
    {
        if (emailList.isEmpty()) {
            System.out.println("No current emails.");
        } else {
            int count = 0;
            for (Email email : emailList) {
                System.out.println("Email" + count + " = " + email.toString());
                count += 1;
            }
            System.out.println("Total number of emails: " + count);
        }
    }
}