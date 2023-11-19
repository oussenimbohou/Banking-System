package com.barack.securebanksystem.service.Impl;

import com.barack.securebanksystem.dto.TransactionRequest;
import com.barack.securebanksystem.entity.Transaction;

public interface TransactionService {
    void saveTransaction(TransactionRequest request);
}
