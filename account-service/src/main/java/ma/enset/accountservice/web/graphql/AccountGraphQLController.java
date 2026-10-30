package ma.enset.accountservice.web.graphql;

import graphql.GraphQLError;
import lombok.RequiredArgsConstructor;
import ma.enset.accountservice.domain.Account;
import ma.enset.accountservice.domain.AccountStatus;
import ma.enset.accountservice.service.AccountNotFoundException;
import ma.enset.accountservice.service.AccountService;
import ma.enset.accountservice.service.BusinessRuleException;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author Yousef ELHAID
 */
@Controller
@RequiredArgsConstructor
public class AccountGraphQLController {

    private final AccountService accountService;

    @QueryMapping
    public List<Account> accounts() {
        return accountService.getAllAccounts();
    }

    @QueryMapping
    public Account accountById(@Argument UUID id) {
        return accountService.getAccountById(id);
    }

    @MutationMapping
    public Account createAccount(@Argument AccountInput account) {
        Account newAccount = Account.builder()
                .customerReference(account.customerReference())
                .currency(account.currency())
                .balance(account.balance())
                .status(AccountStatus.ACTIVE)
                .build();
        return accountService.createAccount(newAccount);
    }

    @MutationMapping
    public Account changeStatus(@Argument UUID id, @Argument AccountStatus status) {
        return accountService.changeStatus(id, status);
    }

    @MutationMapping
    public Account debit(@Argument UUID id, @Argument double amount) {
        return accountService.debit(id, amount);
    }

    @MutationMapping
    public Account credit(@Argument UUID id, @Argument double amount) {
        return accountService.credit(id, amount);
    }

    @GraphQlExceptionHandler
    public GraphQLError handleNotFound(AccountNotFoundException e) {
        return GraphQLError.newError().errorType(ErrorType.NOT_FOUND).message(e.getMessage()).build();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleBusinessRule(BusinessRuleException e) {
        return GraphQLError.newError().errorType(ErrorType.BAD_REQUEST).message(e.getMessage()).build();
    }
}