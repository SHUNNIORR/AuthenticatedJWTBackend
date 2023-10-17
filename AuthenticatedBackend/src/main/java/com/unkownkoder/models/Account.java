package com.unkownkoder.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="accounts")
public class Account {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long accountId;
    @Column(name = "type")
    private String type;
    @Column(name = "balance")
    private Double balance;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private ApplicationUser user;
    // Getters y setters
}
