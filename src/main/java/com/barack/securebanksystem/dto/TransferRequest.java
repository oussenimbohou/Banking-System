package com.barack.securebanksystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
    private BigDecimal amount;
    private String senderAccountNumber;
    private String receiverAccountNumber;
}
