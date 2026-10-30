package ma.enset.accountservice;

import ma.enset.accountservice.domain.Account;
import ma.enset.accountservice.domain.AccountStatus;
import ma.enset.accountservice.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 *
 * @author Yousef ELHAID
 */
@SpringBootApplication
public class AccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner initAccounts(AccountRepository accountRepository) {
        return args -> {
            if (accountRepository.count() > 0) {
                return;
            }
            accountRepository.saveAll(List.of(
                    Account.builder().customerReference("CUST-001").currency("MAD").balance(5000).status(AccountStatus.ACTIVE).build(),
                    Account.builder().customerReference("CUST-002").currency("EUR").balance(1200).status(AccountStatus.ACTIVE).build(),
                    Account.builder().customerReference("CUST-003").currency("MAD").balance(800).status(AccountStatus.BLOCKED).build(),
                    Account.builder().customerReference("CUST-004").currency("EUR").balance(300).status(AccountStatus.ACTIVE).build()
            ));
        };
    }
}