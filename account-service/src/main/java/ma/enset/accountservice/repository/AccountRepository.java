package ma.enset.accountservice.repository;

import ma.enset.accountservice.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 *
 * @author Yousef ELHAID
 */
public interface AccountRepository extends JpaRepository<Account, UUID> {
}