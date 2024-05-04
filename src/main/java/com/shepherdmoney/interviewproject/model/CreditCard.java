package com.shepherdmoney.interviewproject.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


import jakarta.persistence.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String issuanceBank;

    private String number;

    // TODO: Credit card's owner. For detailed hint, please see User class
    // Some field here <> owner;

    // Many-to-One relationship to User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;


    // List to maintain balance history, sorted by date in descending order
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "creditCard")
    @OrderBy("date DESC")
    private List<BalanceHistory> balanceHistories = new ArrayList<>();




    // TODO: Credit card's balance history. It is a requirement that the dates in the balanceHistory 
    //       list must be in chronological order, with the most recent date appearing first in the list. 
    //       Additionally, the last object in the "list" must have a date value that matches today's date, 
    //       since it represents the current balance of the credit card.
    //       This means that if today is 04-16, and the list begin as empty, you receive a payload for 04-13,
    //       you should fill the list up until 04-16. For example:
    //       [
    //         {date: '2023-04-10', balance: 800},
    //         {date: '2023-04-11', balance: 1000},
    //         {date: '2023-04-12', balance: 1200},
    //         {date: '2023-04-13', balance: 1100},
    //         {date: '2023-04-16', balance: 900},
    //       ]
    // ADDITIONAL NOTE: For the balance history, you can use any data structure that you think is appropriate.
    //        It can be a list, array, map, pq, anything. However, there are some suggestions:
    //        1. Retrieval of a balance of a single day should be fast
    //        2. Traversal of the entire balance history should be fast
    //        3. Insertion of a new balance should be fast
    //        4. Deletion of a balance should be fast
    //        5. It is possible that there are gaps in between dates (note the 04-13 and 04-16)
    //        6. In the condition that there are gaps, retrieval of "closest **previous**" balance date should also be fast. Aka, given 4-15, return 4-13 entry tuple



    public void addBalanceUpdate(LocalDate date, double balance) {
        // Check if the date is already in the history, update it if found
        for (BalanceHistory history : balanceHistories) {
            if (history.getDate().isEqual(date)) {
                history.setBalance(balance);
                return;
            }
        }

        // If not found, create a new history entry
        BalanceHistory newHistory = new BalanceHistory(date, balance, this);
        balanceHistories.add(0, newHistory); // Add to the front to maintain order
        balanceHistories.sort((h1, h2) -> h2.getDate().compareTo(h1.getDate())); // Ensure list is sorted
    }

    /**
     * Retrieves the balance for the closest previous date to the given date.
     * If no exact match is found, it returns the closest previous balance.
     *
     * @param date the date to find the closest balance for
     * @return the balance for the closest date
     */
    public double getBalanceForDate(LocalDate date) {
        return balanceHistories.stream()
                .filter(b -> !b.getDate().isAfter(date))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No balance record found for date or before"))
                .getBalance();
    }
}
