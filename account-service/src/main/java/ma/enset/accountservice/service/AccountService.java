package ma.enset.accountservice.service;

import lombok.RequiredArgsConstructor;
import ma.enset.accountservice.domain.Account;
import ma.enset.accountservice.domain.AccountStatus;
import ma.enset.accountservice.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author Yousef ELHAID
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Account getAccountById(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    public Account createAccount(Account account) {
        if (account.getBalance() < 0) {
            throw new BusinessRuleException("Initial balance must be zero or positive");
        }
        account.setId(null);
        account.setCurrency(account.getCurrency().toUpperCase());
        if (account.getStatus() == null) {
            account.setStatus(AccountStatus.ACTIVE);
        }
        return accountRepository.save(account);
    }

    public Account changeStatus(UUID id, AccountStatus status) {
        Account account = getAccountById(id);
        account.setStatus(status);
        return accountRepository.save(account);
    }

    public Account debit(UUID id, double amount) {
        Account account = getActiveAccount(id);
        checkPositive(amount);
        if (account.getBalance() < amount) {
            throw new BusinessRuleException("Insufficient balance on account " + id);
        }
        account.setBalance(account.getBalance() - amount);
        return accountRepository.save(account);
    }

    public Account credit(UUID id, double amount) {
        Account account = getActiveAccount(id);
        checkPositive(amount);
        account.setBalance(account.getBalance() + amount);
        return accountRepository.save(account);
    }

    private Account getActiveAccount(UUID id) {
        Account account = getAccountById(id);
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new BusinessRuleException("Account " + id + " is not active");
        }
        return account;
    }

    private void checkPositive(double amount) {
        if (amount <= 0) {
            throw new BusinessRuleException("Amount must be strictly positive");
        }
    }
}