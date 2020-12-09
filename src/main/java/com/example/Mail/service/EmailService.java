package com.example.Mail.service;

import com.example.Mail.Config.ExternalMailConfig;
import com.example.Mail.Config.FeatureSwitchReceiveExternalMailServer;
import com.example.Mail.Config.SwitchSendExternalMailServer;
import com.example.Mail.model.*;
import com.example.Mail.utils.ExternalUser;
import com.example.Mail.utils.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EmailService
{
    private Users users = new Users();
    private HashMap<UUID, UserInformation> userDatabase = users.getUserDatabase();

    private final SwitchSendExternalMailServer featureSwitchSendExternalMailConfiguration;
    private final FeatureSwitchReceiveExternalMailServer receiveExternalMailConfiguration;
    private final ExternalMailConfig externalMailConfiguration;

    private final RestTemplate restTemplate;

    public Object inboxLogin(UserInformation userInformation)
    {
        return inboxLogin(userInformation.getUserName(), userInformation.getPassword());
    }

    public Object inboxLogin(String userName, String password)
    {
        ResponseEntity<String> responseEntity;
        UUID userUUID = checkUserExists(userName);

        if (userUUID != null) {
            if(password.equals(userDatabase.get(userUUID).getPassword())) {
                responseEntity = new ResponseEntity<>(userUUID.toString(), HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<>("Sorry, you have entered invalid password or Username. Try again.", HttpStatus.UNAUTHORIZED);
            }
        } else {
            responseEntity = new ResponseEntity<>("Sorry, this Username and Password have not been registered yet. Please sign up.", HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }



    public ResponseEntity<String> sendEmail(UUIDEmail tempEmail)
    {
        return sendEmail(tempEmail.getFrom(), tempEmail.getTo(), tempEmail.getMessage());
    }

    public ResponseEntity<String> sendEmail(UUID sender, String recipient, String message)
    {
        ResponseEntity<String> emailSent;


        UUID recipientUUID = checkUserExists(recipient);
        if (recipientUUID != null && userDatabase.containsKey(sender)) {
            UserInformation recipientObject = userDatabase.get(recipientUUID);
            UserInformation senderObject = userDatabase.get(sender);

            Email email = Email.builder().from(sender).to(recipientUUID).message(message).build();
            recipientObject.updateEmails(email,"inbox");
            senderObject.updateEmails(email, "outbox");

            emailSent = new ResponseEntity<>((" Your email has been sent to " + recipient), HttpStatus.OK);
        } else {
            if(!featureSwitchSendExternalMailConfiguration.isSendExternalMailOn()) {
                emailSent = new ResponseEntity<>(("Sorry, you are trying to send a message to an email that does not exist. " +
                        "Please enter the correct email."), HttpStatus.SERVICE_UNAVAILABLE);
            } else {
                ExternalEmail body = ExternalEmail.builder().from(userDatabase.get(sender).getUserName()).to(recipient).message(message).build();
                String headerValue = new String (Base64.getEncoder().encode(externalMailConfiguration.getApiKey().getBytes()));
                HttpHeaders headers = new HttpHeaders();
                headers.add("api-key", headerValue);
                HttpEntity<ExternalEmail> httpEntity = new HttpEntity<>(body, headers);

                try {
                    ResponseEntity<Void> response = restTemplate.exchange(externalMailConfiguration.getIp(), HttpMethod.POST, httpEntity, Void.class);
                    emailSent = new ResponseEntity<>(" Your email has been sent.", HttpStatus.OK);
                } catch (RestClientException e) {
                    emailSent = new ResponseEntity<>(("You are trying to send this message to an email that doesn't exist. " +
                            "Please enter the correct email. "), HttpStatus.BAD_REQUEST);
                }
            }
        }
        return emailSent;
    }



    private UUID checkUserExists(String userName)
    {
        UUID userPrimaryKey = null;
        Iterator userDatabaseIterator = userDatabase.entrySet().iterator();

        while (userDatabaseIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)userDatabaseIterator.next();
            UserInformation tempUserInfo = (UserInformation) mapElement.getValue();
            if (userName.equals(tempUserInfo.getUserName())) {
                userPrimaryKey = (UUID)mapElement.getKey();
            }
        }
        return userPrimaryKey;
    }



    public ArrayList<InboxEmail> checkInbox(UUID primaryKey)
    {
        UserInformation userObject = userDatabase.get(primaryKey);
        ArrayList<Email> emailInbox = userObject.getEmailInbox();

        ArrayList<InboxEmail> emailDisplay = new ArrayList<>();

        Iterator iterator = emailInbox.iterator();
        while (iterator.hasNext()) {
            Email tempEmail = (Email)iterator.next();

            if (ExternalUser.externalUserDatabase.containsValue(tempEmail.getFrom())) {
                String externalUserUUID = getExternalUserName(tempEmail.getFrom());
                emailDisplay.add(InboxEmail.builder()
                        .from(externalUserUUID)
                        .message(tempEmail.getMessage())
                        .build());
            } else {
                emailDisplay.add(InboxEmail.builder()
                        .from(userDatabase.get(tempEmail.getFrom()).getUserName())
                        .message(tempEmail.getMessage())
                        .build());
            }
        }

        return emailDisplay;
    }

    private String getExternalUserName(UUID externalUserUUID)
    {
        String userName = null;
        Iterator externalUserDatabaseIterator = ExternalUser.externalUserDatabase.entrySet().iterator();
        while (externalUserDatabaseIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)externalUserDatabaseIterator.next();
            UUID tempUUID = (UUID) mapElement.getValue();
            if (tempUUID.equals(externalUserUUID)) {
                userName = (String) mapElement.getKey();;
            }
        }
        return userName;
    }



    public ArrayList<OutboxEmail> checkOutbox(UUID primaryKey)
    {
        UserInformation userObject = userDatabase.get(primaryKey);
        ArrayList<Email> emailInbox = userObject.getEmailOutbox();

        ArrayList<OutboxEmail> emailDisplay = new ArrayList<>();

        Iterator iterator = emailInbox.iterator();
        while (iterator.hasNext()) {
            Email tempEmail = (Email)iterator.next();
            emailDisplay.add(OutboxEmail.builder()
                    .to(userDatabase.get(tempEmail.getTo()).getUserName())
                    .message(tempEmail.getMessage())
                    .build());
        }
        return emailDisplay;
    }



    public Object receiveExternalMail(ExternalEmail externalEmail, String keyValue)
    {
        String decodedHeader = new String (Base64.getDecoder().decode(keyValue));
        if (decodedHeader.equals(externalMailConfiguration.getApiKey()))
        {
            return receiveExternalMail(externalEmail.getFrom(), externalEmail.getTo(), externalEmail.getMessage());
        } else {
            return new ResponseEntity<>("Sorry wrong key .", HttpStatus.UNAUTHORIZED);
        }
    }

    public Object receiveExternalMail(String sender, String recipient, String message)
    {
        ResponseEntity<String> responseEntity;

        if (!receiveExternalMailConfiguration.isReceiveExternalMailOn()) {
            responseEntity = new ResponseEntity<>("This service is not available. Try again later.",
                    HttpStatus.SERVICE_UNAVAILABLE);
        } else {
            UUID recipientUUID = checkUserExists(recipient);
            if (recipientUUID != null) {
                if (!ExternalUser.externalUserDatabase.containsKey(sender)) {
                    ExternalUser.externalUserDatabase.put(sender, UUID.randomUUID());
                }
                UUID senderUUID = ExternalUser.externalUserDatabase.get(sender);
                UserInformation recipientObject = userDatabase.get(recipientUUID);
                Email email = Email.builder().from(senderUUID).to(recipientUUID).message(message).build();
                recipientObject.updateEmails(email,"Inbox");

                responseEntity = new ResponseEntity<>("Your email has been received from the external server!", HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<>("The user that you are trying to send an email to does not exist.",
                        HttpStatus.BAD_REQUEST);
            }
        }
        return responseEntity;
    }
}