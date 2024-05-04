package com.shepherdmoney.interviewproject.repository;

import com.shepherdmoney.interviewproject.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Crud repository to store credit cards
 */
@Repository("CreditCardRepo")
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {

    // Custom query to find all credit cards by the owner's ID
    List<CreditCard> findAllByOwnerId(Integer userId);

    // Custom query to find a credit card by its number
    CreditCard findByNumber(String number);
}



