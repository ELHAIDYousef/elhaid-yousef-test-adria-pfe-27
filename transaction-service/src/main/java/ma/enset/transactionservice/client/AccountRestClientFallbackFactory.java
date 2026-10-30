package ma.enset.transactionservice.client;

import feign.FeignException;
import ma.enset.transactionservice.domain.Account;
import ma.enset.transactionservice.service.AccountNotFoundException;
import ma.enset.transactionservice.service.AccountServiceUnavailableException;
import ma.enset.transactionservice.service.BusinessRuleException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 *
 * @author Yousef ELHAID
 */
@Component
public class AccountRestClientFallbackFactory implements FallbackFactory<AccountRestClient> {

    @Override
    public AccountRestClient create(Throwable cause) {
        return new AccountRestClient() {
            @Override
            public Account getAccountById(UUID id) {
                throw translate(cause, id);
            }

            @Override
            public Account debit(UUID id, AmountRequest request) {
                throw translate(cause, id);
            }

            @Override
            public Account credit(UUID id, AmountRequest request) {
                throw translate(cause, id);
            }
        };
    }

    private RuntimeException translate(Throwable cause, UUID id) {
        FeignException feignException = findFeignException(cause);
        if (feignException != null && feignException.status() == 404) {
            return new AccountNotFoundException(id);
        }
        if (feignException != null && feignException.status() == 400) {
            return new BusinessRuleException("Operation rejected by account-service for account " + id);
        }
        return new AccountServiceUnavailableException("account-service is unavailable, the transaction is rejected");
    }

    private FeignException findFeignException(Throwable cause) {
        Throwable current = cause;
        while (current != null) {
            if (current instanceof FeignException feignException) {
                return feignException;
            }
            current = current.getCause();
        }
        return null;
    }
}