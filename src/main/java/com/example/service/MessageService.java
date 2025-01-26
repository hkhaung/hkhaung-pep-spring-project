package com.example.service;

import java.lang.StackWalker.Option;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Message createNewMessage(Message message) throws Exception {
        Optional<Account> poster = accountRepository.findById(message.getPostedBy());
        if (poster.isPresent()) {
            return messageRepository.save(message);
        }

        throw new Exception("Failed to create message");
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageByMessageId(int id) {
        Optional<Message> message = messageRepository.findById(id);
        if (message.isPresent()) {
            return message.get();
        }
        return null;
    }

    public int deleteMessageByMessageId(int id) {
        Optional<Message> message = messageRepository.findById(id);
        if (message.isPresent()) {
            messageRepository.deleteById(id);
            return 1;
        }
        return 0;
    }

    public int updateMessageByMessageId(int id, String newMessage) {
        Optional<Message> messageOptional = messageRepository.findById(id);
        if (messageOptional.isEmpty()) {
            return 0;
        }

        Message message = messageOptional.get();
        message.setMessageText(newMessage);
        messageRepository.save(message);
        return 1;
    }

    public List<Message> getAllMessagesByAccountId(int id) {
        Integer[] ids = {id};
        List<Message> messages = messageRepository.findAllById(Arrays.asList(ids));
        return messages;
    }
}
