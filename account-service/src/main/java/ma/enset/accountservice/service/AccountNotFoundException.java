package ma.enset.accountservice.service;

import java.util.UUID;

/**
 *
 * @author Yousef ELHAID
 */
public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(UUID id) {
        super("Account not found: " + id);
    }
}