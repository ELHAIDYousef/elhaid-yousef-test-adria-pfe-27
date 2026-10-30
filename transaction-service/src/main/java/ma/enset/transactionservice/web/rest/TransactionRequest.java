package ma.enset.transactionservice.web.rest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ma.enset.transactionservice.domain.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

/**
 *
 * @author Yousef ELHAID
 */
public record TransactionRequest(
        @NotNull UUID accountId,
        TransactionType type,
        @NotNull @Positive BigDecimal amount,
        @NotBlank @Size(min = 3, max = 3) String currency,
        @NotBlank String merchantCategory,
        @NotBlank String country) {
}