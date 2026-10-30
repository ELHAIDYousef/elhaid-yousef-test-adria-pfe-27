package ma.enset.accountservice.web.rest;

import jakarta.validation.constraints.Positive;

/**
 *
 * @author Yousef ELHAID
 */
public record AmountRequest(@Positive double amount) {
}