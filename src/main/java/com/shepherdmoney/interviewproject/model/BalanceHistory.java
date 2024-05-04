package com.shepherdmoney.interviewproject.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;


@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class BalanceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    private LocalDate date;
    private double balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_card_id")
    private CreditCard creditCard;

    /**
     * Constructs a new BalanceHistory object with specified date and balance.
     *
     * @param date the date of the balance history record
     * @param balance the balance amount on that date
     * @param creditCard the credit card associated with this balance history
     */
    

    public BalanceHistory(LocalDate date, double balance, CreditCard creditCard) {
        this.date = date;
        this.balance = balance;
        this.creditCard = creditCard;
    }
}