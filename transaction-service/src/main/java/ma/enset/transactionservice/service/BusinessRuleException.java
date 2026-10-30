package ma.enset.transactionservice.service;

/**
 *
 * @author Yousef ELHAID
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}