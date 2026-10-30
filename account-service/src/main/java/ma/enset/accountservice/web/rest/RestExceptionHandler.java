package ma.enset.accountservice.web.rest;

import ma.enset.accountservice.service.AccountNotFoundException;
import ma.enset.accountservice.service.BusinessRuleException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *
 * @author Yousef ELHAID
 */
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ProblemDetail handleNotFound(AccountNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ProblemDetail handleBusinessRule(BusinessRuleException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}