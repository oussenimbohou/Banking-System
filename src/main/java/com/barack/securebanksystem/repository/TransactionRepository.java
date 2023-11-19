package com.barack.securebanksystem.repository;

import com.barack.securebanksystem.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    @Query("SELECT t FROM Transaction t " +
            "WHERE t.createAt BETWEEN :startDate AND :endDate " +
            "AND t.accountNumber = :accountNumber")
    List<Transaction> findAccountNumbersInDateInterval(
            @Param("accountNumber") String accountNumber,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
