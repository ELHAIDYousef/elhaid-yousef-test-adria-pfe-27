package ma.enset.transactionservice.service;

import lombok.RequiredArgsConstructor;
import ma.enset.transactionservice.client.AccountRestClient;
import ma.enset.transactionservice.client.AmountRequest;
import ma.enset.transactionservice.domain.Account;
import ma.enset.transactionservice.domain.Transaction;
import ma.enset.transactionservice.domain.TransactionType;
import ma.enset.transactionservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Yousef ELHAID
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {

    private static final String TRANSFER_CATEGORY = "TRANSFER";
    private static final String TRANSFER_COUNTRY = "MA";

    private final TransactionRepository transactionRepository;
    private final AccountRestClient accountRestClient;

    @Value("${bank.transaction.max-amount}")
    private BigDecimal maxAmount;

    public Transaction createTransaction(Transaction transaction) {
        checkAmount(transaction.getAmount());
        if (transaction.getType() == null) {
            transaction.setType(TransactionType.DEBIT);
        }
        transaction.setCurrency(transaction.getCurrency().toUpperCase());

        Account account = accountRestClient.getAccountById(transaction.getAccountId());
        checkAccount(account, transaction);

        transaction.setId(null);
        transaction.setCreatedAt(Instant.now());
        Transaction saved = transactionRepository.save(transaction);

        AmountRequest request = new AmountRequest(saved.getAmount().doubleValue());
        if (saved.getType() == TransactionType.DEBIT) {
            accountRestClient.debit(saved.getAccountId(), request);
        } else {
            accountRestClient.credit(saved.getAccountId(), request);
        }
        return saved;
    }

    public List<Transaction> transfer(UUID sourceAccountId, UUID targetAccountId, BigDecimal amount) {
        if (sourceAccountId.equals(targetAccountId)) {
            throw new BusinessRuleException("Source and target accounts must be different");
        }
        checkAmount(amount);
        Account source = accountRestClient.getAccountById(sourceAccountId);
        Account target = accountRestClient.getAccountById(targetAccountId);
        if (!source.getCurrency().equals(target.getCurrency())) {
            throw new BusinessRuleException("Transfer between different currencies is not allowed");
        }
        checkActive(target);

        Transaction debit = createTransaction(buildTransfer(sourceAccountId, TransactionType.DEBIT, amount, source.getCurrency()));
        try {
            Transaction credit = createTransaction(buildTransfer(targetAccountId, TransactionType.CREDIT, amount, target.getCurrency()));
            return List.of(debit, credit);
        } catch (RuntimeException e) {
            accountRestClient.credit(sourceAccountId, new AmountRequest(amount.doubleValue()));
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Transaction getTransactionById(UUID id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByAccount(UUID accountId) {
        return transactionRepository.findByAccountIdOrderByCreatedAtDesc(accountId);
    }

    private void checkAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new BusinessRuleException("Amount must be strictly positive");
        }
        if (amount.compareTo(maxAmount) > 0) {
            throw new BusinessRuleException("Amount exceeds the maximum allowed: " + maxAmount);
        }
    }

    private void checkAccount(Account account, Transaction transaction) {
        checkActive(account);
        if (!account.getCurrency().equals(transaction.getCurrency())) {
            throw new BusinessRuleException("Currency mismatch: account in " + account.getCurrency()
                    + ", transaction in " + transaction.getCurrency());
        }
        if (transaction.getType() == TransactionType.DEBIT
                && BigDecimal.valueOf(account.getBalance()).compareTo(transaction.getAmount()) < 0) {
            throw new BusinessRuleException("Insufficient balance on account " + account.getId());
        }
    }

    private void checkActive(Account account) {
        if (!"ACTIVE".equals(account.getStatus())) {
            throw new BusinessRuleException("Account " + account.getId() + " is not active");
        }
    }

    private Transaction buildTransfer(UUID accountId, TransactionType type, BigDecimal amount, String currency) {
        return Transaction.builder()
                .accountId(accountId)
                .type(type)
                .amount(amount)
                .currency(currency)
                .merchantCategory(TRANSFER_CATEGORY)
                .country(TRANSFER_COUNTRY)
                .build();
    }
}