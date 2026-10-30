package ma.enset.accountservice.web.graphql;

/**
 *
 * @author Yousef ELHAID
 */
public record AccountInput(String customerReference, String currency, double balance) {
}