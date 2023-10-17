package com.unkownkoder.services;

import com.unkownkoder.models.Account;
import com.unkownkoder.models.ApiResponse;
import com.unkownkoder.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;
    public ApiResponse<Account> saveAccount(Account account) {
        Account account1 = accountRepository.save(account);
        ApiResponse<Account> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Creado con exito");
        response.setData(account1);
        return response;
    }
}
