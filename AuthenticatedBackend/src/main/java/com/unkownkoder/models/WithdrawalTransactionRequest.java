package com.unkownkoder.models;

import com.unkownkoder.utils.TransactionType;

public class WithdrawalTransactionRequest {
    private Long accountId;
    private Double amount;
    public TransactionType transactionType;
    public Double getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }



    // Getters y setters
}