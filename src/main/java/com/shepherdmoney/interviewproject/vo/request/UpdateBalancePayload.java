package com.shepherdmoney.interviewproject.vo.request;

import java.time.LocalDate;

import lombok.Data;

@Data
// public class UpdateBalancePayload {

//     private String creditCardNumber;
    
//     private LocalDate balanceDate;

//     private double balanceAmount;
// }

public class UpdateBalancePayload {
    private String cardNumber;
    private String date;
    private double amount;
}