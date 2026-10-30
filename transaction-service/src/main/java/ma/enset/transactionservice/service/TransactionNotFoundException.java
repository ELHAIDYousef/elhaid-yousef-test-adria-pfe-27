package ma.enset.transactionservice.service;

import java.util.UUID;

/**
 *
 * @author Yousef ELHAID
 */
public class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException(UUID id) {
        super("Transaction not found: " + id);
    }
}