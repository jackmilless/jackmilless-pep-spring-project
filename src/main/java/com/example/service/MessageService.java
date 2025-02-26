package com.example.service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service 
public class MessageService {

    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    @Autowired 
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    /*
     * if messageText meets length requirements and posted by refers to existing account,
     * calls save to persist message to database 
     * returns persisted message or null on failure
     */
    public Message createMessage(Message message) {
        int msgLength = message.getMessageText().length();
        if(msgLength > 0 && msgLength <= 255 && accountRepository.existsById(message.getPostedBy())) {
            return messageRepository.save(message);
        } else {
            return null;
        }
    }

    /*
     * calls findAll to return all messages stored on database
     */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    /*
     * calls findById to return message given by messageId, or null if message does not exist
     */
    public Message getMessageById(int messageId) {
        return messageRepository.findById(messageId).orElse(null);
    }

    /*
     * calls existsById to check if message given by messageId exists
     * then calls deleteById to delete message if message exists
     * returns 1 if message existed or 0 if it did not 
     */
    public int deleteMessageById(int messageId) {
        if(messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return 1;
        } else {
            return 0;
        }
    }

    /*
     * if messageText meets length requirements, calls existsById to check if message given by messageId exists
     * if all requirements met, calls getMessageById to get message and update it with new messageText
     * returns 1 on success or 0 on failure
     */
    public int updateMessageById(String messageText, int messageId) {
        int msgLength = messageText.length();
        if(msgLength > 0 && msgLength <= 255 && messageRepository.existsById(messageId)) {
            Message updatedMessage = getMessageById(messageId);
            updatedMessage.setMessageText(messageText);
            messageRepository.save(updatedMessage);
            return 1;
        } else {
            return 0;
        }
    }

    /*
     * calls findByPostedBy to return all messages given by accountId
     */
    public List<Message> getMessagesByAccountId(int accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}
