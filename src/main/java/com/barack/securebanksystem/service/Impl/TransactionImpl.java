package com.barack.securebanksystem.service.Impl;

import com.barack.securebanksystem.dto.TransactionRequest;
import com.barack.securebanksystem.entity.Transaction;
import com.barack.securebanksystem.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class TransactionImpl implements  TransactionService{
    @Autowired
    private TransactionRepository repository;
    @Override
    public void saveTransaction(TransactionRequest request) {
        Transaction transaction = Transaction.builder()
                .transactionType(request.getTransactionType())
                .accountNumber(request.getAccountNumber())
                .amount(request.getAmount())
                .status("SUCCESS")
                .build();
        repository.save(transaction);
    }
}
