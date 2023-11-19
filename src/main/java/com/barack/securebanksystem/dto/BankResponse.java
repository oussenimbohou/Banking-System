package com.barack.securebanksystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class BankResponse {
    @Schema(
            name = "Response Code after operation"
    )
    private String responseCode;
    @Schema(
            name = "Response Message after operation"
    )
    private String responseMessage;
    @Schema(
            name = "Account Information object"
    )
    private AccountInfo accountInfo;

}
