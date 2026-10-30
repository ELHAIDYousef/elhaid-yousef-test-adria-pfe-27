package ma.enset.accountservice.web.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.enset.accountservice.domain.Account;
import ma.enset.accountservice.domain.AccountStatus;
import ma.enset.accountservice.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author Yousef ELHAID
 */
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountRestController {

    private final AccountService accountService;

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable UUID id) {
        return accountService.getAccountById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@RequestBody @Valid AccountRequest request) {
        Account account = Account.builder()
                .customerReference(request.customerReference())
                .currency(request.currency())
                .balance(request.balance())
                .status(AccountStatus.ACTIVE)
                .build();
        return accountService.createAccount(account);
    }

    @PatchMapping("/{id}/status")
    public Account changeStatus(@PathVariable UUID id, @RequestParam AccountStatus status) {
        return accountService.changeStatus(id, status);
    }

    @PostMapping("/{id}/debit")
    public Account debit(@PathVariable UUID id, @RequestBody @Valid AmountRequest request) {
        return accountService.debit(id, request.amount());
    }

    @PostMapping("/{id}/credit")
    public Account credit(@PathVariable UUID id, @RequestBody @Valid AmountRequest request) {
        return accountService.credit(id, request.amount());
    }
}