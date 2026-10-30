package ma.enset.accountservice.service;

/**
 *
 * @author Yousef ELHAID
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}