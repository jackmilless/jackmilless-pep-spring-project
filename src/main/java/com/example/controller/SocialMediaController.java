package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    /*
     * registers new account with given username and password
     * responds with CONFLICT status if username already exists,
     * BAD_REQUEST if username/password requirements not met,
     * or returns registered account with id on success
     */
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        if(accountService.getAccountByUsername(account.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        Account registeredAccount = accountService.registerAccount(account);

        if(registeredAccount == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(registeredAccount);
        }
    }

    /*
     * logs into existing account with given username and password
     * responds with UNAUTHORIZED status on failure or returns account with id on success
     */
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Account loggedInAccount = accountService.loginAccount(account);

        if(loggedInAccount == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(loggedInAccount);
        }
    }

    /*
     * posts a new message
     * responds with status BAD_REQUEST on failure or returns uploaded message with id on success
     */
    @PostMapping("/messages")
    public ResponseEntity<Message> postMessage(@RequestBody Message message) {
        Message createdMessage = messageService.createMessage(message);

        if(createdMessage == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(createdMessage);
        }
    }

    /*
     * returns all posted messages or an empty response if no messages exist; always successful
     */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> allMessages = messageService.getAllMessages();
        return ResponseEntity.status(HttpStatus.OK).body(allMessages);
    }

    /*
     * returns message given by messageId or an empty response if message does not exist; always successful
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable int messageId) {
        Message message = messageService.getMessageById(messageId);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    /*
     * deletes message given by messageId
     * responds with OK status regardless of result,
     * returns "1" on success or an empty response if message does not exist
     */
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable int messageId) {
        int rowsAffected = messageService.deleteMessageById(messageId);
        if(rowsAffected != 1) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(rowsAffected);
        }
    }

    /*
     * updates message with requested messageText
     * responds with BAD_REQUEST if message length requirements not met or message does not exist
     * or returns "1" if successful
     */
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> patchMessageById(@RequestBody Message message, @PathVariable int messageId) {
        int rowsAffected = messageService.updateMessageById(message.getMessageText(), messageId);
        if(rowsAffected != 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(rowsAffected);
        }
    }

    /*
     * returns all messages posted by user with given accountId
     * or an empty response if none exist; always successful
     */
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable int accountId) {
        List<Message> accountMessages = messageService.getMessagesByAccountId(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(accountMessages);
    }
}
