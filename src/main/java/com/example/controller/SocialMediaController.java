package com.example.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    @Autowired
    AccountService accountService = new AccountService();

    @Autowired
    MessageService messageService = new MessageService();
    
    @PostMapping("/register")
    public ResponseEntity<Object> createAccount(@RequestBody Account account) {
        try {

            if (account.getUsername() == "" || account.getUsername().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username cannot be blank");
            }
            if (account.getPassword() == null || account.getPassword().length() < 4) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is too short");
            }

            Account newAccount = accountService.createAccount(account);
            return ResponseEntity.status(HttpStatus.OK).body(newAccount);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create account");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Account account) {
        try {
            Account acc = accountService.login(account);
            return ResponseEntity.status(HttpStatus.OK).body(acc);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<Object> createMessage(@RequestBody Message message) {
        try {
            if (message.getMessageText() == null || message.getMessageText().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message text cannot be blank");
            }
            
            if (message.getMessageText().length() > 255) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message text must be under 255 characters");
            }
            
            Message newMessage = messageService.createNewMessage(message);
            return ResponseEntity.status(HttpStatus.OK).body(newMessage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create message");
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }

    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Object> getMessageByMessageId(@PathVariable int message_id) {
        Message message = messageService.getMessageByMessageId(message_id);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Integer> deleteMessageByMessageId(@PathVariable int message_id) {
        int numberRowsDeleted = messageService.deleteMessageByMessageId(message_id);
        if (numberRowsDeleted > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(numberRowsDeleted);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Integer> updateMessageByMessageId(@PathVariable int message_id, @RequestBody Map<String, String> requestBody) {
        String newMessage = requestBody.get("messageText");

        if (newMessage == null || newMessage.isBlank() || newMessage.length() > 255) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        int numberRowsUpdated = messageService.updateMessageByMessageId(message_id, newMessage);
        if (numberRowsUpdated > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(numberRowsUpdated);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getAllMessagesByAccountId(@PathVariable int account_id) {
        List<Message> messages = messageService.getAllMessagesByAccountId(account_id);
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }

}
