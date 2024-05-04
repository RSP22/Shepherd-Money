package com.shepherdmoney.interviewproject.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data       // Generates Getters, Setters, equals(), hashCode(), and toString()
@Builder    // Provides the builder pattern for object creation
@AllArgsConstructor // Generates a constructor with all arguments
public class CreditCardView {
    private String id; // Assuming you want to send the ID as a String
    private String number;
    private String issuanceBank;
}

