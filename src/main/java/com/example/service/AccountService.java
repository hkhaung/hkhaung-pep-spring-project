package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account) throws Exception {
        if (accountRepository.findByUsername(account.getUsername()) != null) {
            throw new RuntimeException("Duplicate username");
        }
        return accountRepository.save(account);
    }

    public Account login(Account account) throws Exception {
        Account existingAccount = accountRepository.findByUsername(account.getUsername());
        
        if (existingAccount == null || !existingAccount.getPassword().equals(account.getPassword())) {
            throw new Exception("Invalid credentials");
        }

        return existingAccount;
    }

}
