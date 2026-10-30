package ma.enset.transactionservice.web.graphql;

import ma.enset.transactionservice.domain.TransactionType;

import java.util.UUID;

/**
 *
 * @author Yousef ELHAID
 */
public record TransactionInput(
        UUID accountId,
        TransactionType type,
        double amount,
        String currency,
        String merchantCategory,
        String country) {
}