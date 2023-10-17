package com.unkownkoder.controllers;

import com.unkownkoder.models.ApiResponse;
import com.unkownkoder.models.DepositTransactionRequest;
import com.unkownkoder.models.Transaction;
import com.unkownkoder.models.WithdrawalTransactionRequest;
import com.unkownkoder.services.TransactionService;
import com.unkownkoder.utils.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    TransactionService transactionService;


    @PostMapping("/selfwithdraw")
    public ResponseEntity<ApiResponse<Transaction>> selfWithdraw(@RequestBody WithdrawalTransactionRequest transactionRequest){
        ApiResponse<Transaction> transaction = transactionService.selfWithDraw(transactionRequest.getAmount(), transactionRequest.getAccountId(), transactionRequest.getTransactionType());
        return new ResponseEntity<>(transaction, transaction.isSuccess()?HttpStatus.OK:HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<Transaction>> deposit(@RequestBody DepositTransactionRequest transactionRequest){
        ApiResponse<Transaction> transaction = transactionService.transactionDeposit(transactionRequest.getAmount(), transactionRequest.getSourceAccountId(), transactionRequest.getTargetAccountId(), transactionRequest.getTransactionType());
        return new ResponseEntity<>(transaction, transaction.isSuccess()?HttpStatus.OK:HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/mytransactions")
    public ResponseEntity<ApiResponse<List<Transaction>>> mytransactions(@RequestParam Long accountId ){
        ApiResponse<List<Transaction>> transaction = transactionService.getMyTransactions(accountId);
        return new ResponseEntity<>(transaction, transaction.isSuccess()?HttpStatus.OK:HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/export/excel")
    public ResponseEntity<ApiResponse<String>> reportmytransactions(@RequestParam Long accountId ) throws IOException {
        ApiResponse<String> reportInB64 = transactionService.reportMyTransactions(accountId);
        return new ResponseEntity<>(reportInB64, reportInB64.isSuccess()?HttpStatus.OK:HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
