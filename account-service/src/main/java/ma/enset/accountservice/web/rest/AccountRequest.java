package ma.enset.accountservice.web.rest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 *
 * @author Yousef ELHAID
 */
public record AccountRequest(
        @NotBlank String customerReference,
        @NotBlank @Size(min = 3, max = 3) String currency,
        @PositiveOrZero double balance) {
}