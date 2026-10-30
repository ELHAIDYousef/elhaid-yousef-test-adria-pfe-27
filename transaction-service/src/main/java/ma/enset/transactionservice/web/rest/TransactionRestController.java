package ma.enset.transactionservice.web.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.enset.transactionservice.domain.Transaction;
import ma.enset.transactionservice.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author Yousef ELHAID
 */
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionRestController {

    private final TransactionService transactionService;

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable UUID id) {
        return transactionService.getTransactionById(id);
    }

    @GetMapping("/account/{accountId}")
    public List<Transaction> getTransactionsByAccount(@PathVariable UUID accountId) {
        return transactionService.getTransactionsByAccount(accountId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction createTransaction(@RequestBody @Valid TransactionRequest request) {
        Transaction transaction = Transaction.builder()
                .accountId(request.accountId())
                .type(request.type())
                .amount(request.amount())
                .currency(request.currency())
                .merchantCategory(request.merchantCategory())
                .country(request.country())
                .build();
        return transactionService.createTransaction(transaction);
    }
}