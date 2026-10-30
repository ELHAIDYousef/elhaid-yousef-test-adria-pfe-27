package ma.enset.transactionservice.web.rest;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

/**
 *
 * @author Yousef ELHAID
 */
public record TransferRequest(
        @NotNull UUID sourceAccountId,
        @NotNull UUID targetAccountId,
        @NotNull @Positive BigDecimal amount) {
}