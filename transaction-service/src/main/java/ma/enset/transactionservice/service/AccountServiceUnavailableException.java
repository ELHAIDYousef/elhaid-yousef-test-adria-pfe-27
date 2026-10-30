package ma.enset.transactionservice.service;

/**
 *
 * @author Yousef ELHAID
 */
public class AccountServiceUnavailableException extends RuntimeException {

    public AccountServiceUnavailableException(String message) {
        super(message);
    }
}