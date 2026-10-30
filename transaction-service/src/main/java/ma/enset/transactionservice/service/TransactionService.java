package ma.enset.transactionservice.service;

import lombok.RequiredArgsConstructor;
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

    private final TransactionRepository transactionRepository;

    @Value("${bank.transaction.max-amount}")
    private BigDecimal maxAmount;

    public Transaction createTransaction(Transaction transaction) {
        checkAmount(transaction.getAmount());
        transaction.setId(null);
        transaction.setCurrency(transaction.getCurrency().toUpperCase());
        if (transaction.getType() == null) {
            transaction.setType(TransactionType.DEBIT);
        }
        transaction.setCreatedAt(Instant.now());
        return transactionRepository.save(transaction);
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
}
