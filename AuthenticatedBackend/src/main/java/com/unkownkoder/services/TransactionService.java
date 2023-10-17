package com.unkownkoder.services;

import com.unkownkoder.models.Account;
import com.unkownkoder.models.ApiResponse;
import com.unkownkoder.models.ApplicationUser;
import com.unkownkoder.models.Transaction;
import com.unkownkoder.repository.AccountRepository;
import com.unkownkoder.repository.TransactionRepository;
import com.unkownkoder.repository.UserRepository;
import com.unkownkoder.utils.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FileServices fileServices;

    public ApiResponse<Transaction> selfWithDraw(Double amount, Long accountId, TransactionType transactionType){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ApplicationUser actualUser = userRepository.findByUsername(authentication.getName()).get();

        ApiResponse<Transaction> response = new ApiResponse<>();


        Account actualAccount = accountRepository.findById(accountId).get();

        if (!accountRepository.existsById(accountId)){
            response.setSuccess(false);
            response.setMessage("No existe la cuenta a la cual desea retirar fondos");
            response.setData(null);
            return response;
        }
        if (actualAccount.getBalance() - amount < 0){
            response.setSuccess(false);
            response.setMessage("No cuenta con fondos suficientes para realizar esta acción");
            response.setData(null);
            return response;
        }

        actualAccount.setBalance(actualAccount.getBalance() - amount);

        accountRepository.save(actualAccount);

        Transaction withdrawalTransaction =  Transaction.builder()
                .transactionType(transactionType)
                .transactionDate(new Date())
                .amount(amount)
                .sourceAccount(actualAccount)
                .targetAccount(actualAccount)
                .build();

        Transaction transaction = transactionRepository.save(withdrawalTransaction);

        response.setSuccess(true);
        response.setMessage("Se han retirado satisfactoriamente "+ amount +" de su cuenta");
        response.setData(transaction);
        return response;

    }

    public ApiResponse<Transaction> transactionDeposit(Double amount, Long sourceAccountId, Long targetAccountId, TransactionType transactionType){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ApplicationUser actualUser = userRepository.findByUsername(authentication.getName()).get();

        ApiResponse<Transaction> response = new ApiResponse<>();


        Account sourceAccount = accountRepository.findById(sourceAccountId).get();
        Account targetAccount = accountRepository.findById(targetAccountId).get();

        if (!accountRepository.existsById(sourceAccountId) || !accountRepository.existsById(targetAccountId)){
            response.setSuccess(false);
            response.setMessage("Opps parece que una de las cuentas a las que deseas transferir no existe");
            response.setData(null);
            return response;
        }
        if (sourceAccount.getBalance() - amount < 0){
            response.setSuccess(false);
            response.setMessage("No cuenta con fondos suficientes para realizar esta acción");
            response.setData(null);
            return response;
        }

        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount);

        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        Transaction withdrawalTransaction =  Transaction.builder()
                .transactionType(transactionType)
                .transactionDate(new Date())
                .amount(amount)
                .sourceAccount(sourceAccount)
                .targetAccount(targetAccount)
                .build();

        Transaction transaction = transactionRepository.save(withdrawalTransaction);

        response.setSuccess(true);
        response.setMessage("Se han depositado satisfactoriamente "+ amount +" a la cuenta: "+targetAccountId);
        response.setData(transaction);
        return response;
    }

    public ApiResponse<List<Transaction>> getMyTransactions( Long accountId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ApplicationUser actualUser = userRepository.findByUsername(authentication.getName()).get();

        ApiResponse<List<Transaction>> response = new ApiResponse<>();

        if (!accountRepository.existsById(accountId)){
            response.setSuccess(false);
            response.setMessage("No existe la cuenta a la cual desea retirar fondos");
            response.setData(null);
            return response;
        }
        Account account = accountRepository.findById(accountId).get();

        if(!actualUser.getAccounts().contains(account)){
            response.setSuccess(false);
            response.setMessage("No eres el propietario de esta cuenta.");
            response.setData(null);
            return response;
        }

        List<Transaction> allMyTransactions = transactionRepository.findBySourceAccount_accountIdOrTargetAccount_accountId(accountId,accountId).get();

        response.setSuccess(true);
        response.setMessage("Estas son tus transacciones hechas en la cuenta:"+accountId);
        response.setData(allMyTransactions);
        return response;
    }

    public ApiResponse<String> reportMyTransactions( Long accountId) throws IOException {

        //DECODIFICAR AQUÍ: https://www.decodebase64.net/convert/base64tofile

        List<Transaction> allMyTransactions = transactionRepository.findBySourceAccount_accountIdOrTargetAccount_accountId(accountId,accountId).get();
        String reportInB64 = fileServices.generateExcelBase64(allMyTransactions);
        ApiResponse<String> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Archivo creado con éxito");
        response.setData(reportInB64);
        return response;
    }

}
