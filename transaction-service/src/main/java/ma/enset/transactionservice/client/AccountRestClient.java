package ma.enset.transactionservice.client;

import ma.enset.transactionservice.domain.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

/**
 *
 * @author Yousef ELHAID
 */
@FeignClient(name = "account-service", url = "${account-service.url}",
        fallbackFactory = AccountRestClientFallbackFactory.class)
public interface AccountRestClient {

    @GetMapping("/api/accounts/{id}")
    Account getAccountById(@PathVariable("id") UUID id);

    @PostMapping("/api/accounts/{id}/debit")
    Account debit(@PathVariable("id") UUID id, @RequestBody AmountRequest request);

    @PostMapping("/api/accounts/{id}/credit")
    Account credit(@PathVariable("id") UUID id, @RequestBody AmountRequest request);
}