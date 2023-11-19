package com.barack.securebanksystem.service.Impl;

import com.barack.securebanksystem.dto.*;
import com.barack.securebanksystem.entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
    String nameEnquiry(EnquiryRequest enquiryRequest);
    List<User> getAllUsers();
    BankResponse getUserById(Long id);
    BankResponse creditAccount(CreditDebitRequest request);
    BankResponse debitAccount(CreditDebitRequest request);
    BankResponse transfer(TransferRequest request);
    BankResponse deductionAccount(String accountNumber, BigDecimal amount);
}
