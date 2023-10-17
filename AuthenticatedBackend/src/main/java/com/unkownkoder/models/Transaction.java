package com.unkownkoder.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unkownkoder.utils.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long transactionId;
    @Column(name = "transaction_date")
    private Date transactionDate;
    @Column(name = "amount")
    private Double amount;
    @Column(name = "transaction_type")
    private TransactionType transactionType;
    @ManyToOne
    @JoinColumn(name="source_account_id", nullable=false)
    @JsonIgnore
    private Account sourceAccount;
    @ManyToOne
    @JoinColumn(name="target_account_id", nullable=false)
    @JsonIgnore
    private  Account targetAccount;
}
