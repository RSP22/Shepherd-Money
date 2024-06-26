package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.vo.request.AddCreditCardToUserPayload;
import com.shepherdmoney.interviewproject.vo.request.UpdateBalancePayload;
import com.shepherdmoney.interviewproject.vo.response.CreditCardView;
import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.CreditCardRepository;
import com.shepherdmoney.interviewproject.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus; 
import org.springframework.beans.factory.annotation.Autowired; 
import com.shepherdmoney.interviewproject.repository.UserRepository; 
import com.shepherdmoney.interviewproject.repository.CreditCardRepository; 


@RestController
public class CreditCardController {

    // TODO: wire in CreditCard repository here (~1 line)

    // @PostMapping("/credit-card")
    // public ResponseEntity<Integer> addCreditCardToUser(@RequestBody AddCreditCardToUserPayload payload) {
    //     // TODO: Create a credit card entity, and then associate that credit card with user with given userId
    //     //       Return 200 OK with the credit card id if the user exists and credit card is successfully associated with the user
    //     //       Return other appropriate response code for other exception cases
    //     //       Do not worry about validating the card number, assume card number could be any arbitrary format and length
    //     return null;
    // }

    // @GetMapping("/credit-card:all")
    // public ResponseEntity<List<CreditCardView>> getAllCardOfUser(@RequestParam int userId) {
    //     // TODO: return a list of all credit card associated with the given userId, using CreditCardView class
    //     //       if the user has no credit card, return empty list, never return null
    //     return null;
    // }

    // @GetMapping("/credit-card:user-id")
    // public ResponseEntity<Integer> getUserIdForCreditCard(@RequestParam String creditCardNumber) {
    //     // TODO: Given a credit card number, efficiently find whether there is a user associated with the credit card
    //     //       If so, return the user id in a 200 OK response. If no such user exists, return 400 Bad Request
    //     return null;
    // }



    // @PostMapping("/credit-card:update-balance")
    // public SomeEnityData postMethodName(@RequestBody UpdateBalancePayload[] payload) {
    //     //TODO: Given a list of transactions, update credit cards' balance history.
    //     //      1. For the balance history in the credit card
    //     //      2. If there are gaps between two balance dates, fill the empty date with the balance of the previous date
    //     //      3. Given the payload `payload`, calculate the balance different between the payload and the actual balance stored in the database
    //     //      4. If the different is not 0, update all the following budget with the difference
    //     //      For example: if today is 4/12, a credit card's balanceHistory is [{date: 4/12, balance: 110}, {date: 4/10, balance: 100}],
    //     //      Given a balance amount of {date: 4/11, amount: 110}, the new balanceHistory is
    //     //      [{date: 4/12, balance: 120}, {date: 4/11, balance: 110}, {date: 4/10, balance: 100}]
    //     //      This is because
    //     //      1. You would first populate 4/11 with previous day's balance (4/10), so {date: 4/11, amount: 100}
    //     //      2. And then you observe there is a +10 difference
    //     //      3. You propagate that +10 difference until today
    //     //      Return 200 OK if update is done and successful, 400 Bad Request if the given card number
    //     //        is not associated with a card.
        
    //     return null;
    // }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @PostMapping("/credit-card")
    public ResponseEntity<?> addCreditCardToUser(@RequestBody AddCreditCardToUserPayload payload) {
        Optional<User> user = userRepository.findById(payload.getUserId());
        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }

        CreditCard creditCard = new CreditCard();
        creditCard.setIssuanceBank(payload.getBank()); // Correct usage of getter
        creditCard.setNumber(payload.getCardNumber()); // Correct usage of getter
        creditCard.setOwner(user.get());
        creditCardRepository.save(creditCard);

        return ResponseEntity.ok(creditCard.getId());
    }

    @GetMapping("/credit-card/all")
    public ResponseEntity<List<CreditCardView>> getAllCardOfUser(@RequestParam int userId) {
        List<CreditCard> cards = creditCardRepository.findAllByOwnerId(userId);
        if (cards.isEmpty()) {
            return ResponseEntity.ok(List.of()); // Ensure empty list rather than null
        }
        List<CreditCardView> cardViews = cards.stream()
            .map(card -> new CreditCardView(String.valueOf(card.getId()), card.getNumber(), card.getIssuanceBank()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(cardViews);
    }

    @GetMapping("/credit-card/user-id")
    public ResponseEntity<?> getUserIdForCreditCard(@RequestParam String creditCardNumber) {
        CreditCard creditCard = creditCardRepository.findByNumber(creditCardNumber);
        if (creditCard == null || creditCard.getOwner() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No such credit card found");
        }
        return ResponseEntity.ok(creditCard.getOwner().getId());
    }

    @PostMapping("/credit-card/update-balance")
    public ResponseEntity<?> updateBalanceHistory(@RequestBody UpdateBalancePayload[] payloads) {
        for (UpdateBalancePayload payload : payloads) {
            CreditCard creditCard = creditCardRepository.findByNumber(payload.getCardNumber()); // Correct usage
            if (creditCard == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Credit card not found");
            }
            creditCard.addBalanceUpdate(LocalDate.parse(payload.getDate()), payload.getAmount()); // Correct usage
            creditCardRepository.save(creditCard);
        }
        return ResponseEntity.ok("Balance updated successfully");
    }


 
    // public String getBank() {
    //     return this.bank; // Assuming 'bank' is the correct variable name in your payload class.
    // }    
    


    // public String getCardNumber() {
    //     return this.cardNumber;
    // }
    
    // public String getDate() {
    //     return this.date; // Make sure the date is a String or formatted correctly before using LocalDate.parse
    // }
    
    // public double getAmount() {
    //     return this.amount;
    // }
    
}
