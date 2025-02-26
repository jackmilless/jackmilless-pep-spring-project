package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service 
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /*
     * helper method for register control handler:
     * calls findByUsername to check whether account with given username already exists
     * returns account if it exists (failure) or null if it does not (success)
     */
    public Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    /*
     * if username and password requirements are met, calls save to persist account in database 
     * returns persisted account or null on failure
     */
    public Account registerAccount(Account newAccount) {
        if(newAccount.getUsername().length() > 0 && newAccount.getPassword().length() >= 4) {
            return accountRepository.save(newAccount);
        } else {
            return null;
        }
        
    }

    /*
     * calls findByUsernameAndPassword to return account or null on failure (no such account exists)
     */
    public Account loginAccount(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();
        return accountRepository.findByUsernameAndPassword(username, password);
    }
}
