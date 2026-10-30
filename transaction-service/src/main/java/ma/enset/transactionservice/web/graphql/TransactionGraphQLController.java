package ma.enset.transactionservice.web.graphql;

import graphql.GraphQLError;
import lombok.RequiredArgsConstructor;
import ma.enset.transactionservice.domain.Transaction;
import ma.enset.transactionservice.service.BusinessRuleException;
import ma.enset.transactionservice.service.TransactionNotFoundException;
import ma.enset.transactionservice.service.TransactionService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Yousef ELHAID
 */
@Controller
@RequiredArgsConstructor
public class TransactionGraphQLController {

    private final TransactionService transactionService;

    @QueryMapping
    public List<Transaction> transactions() {
        return transactionService.getAllTransactions();
    }

    @QueryMapping
    public Transaction transactionById(@Argument UUID id) {
        return transactionService.getTransactionById(id);
    }

    @QueryMapping
    public List<Transaction> transactionsByAccount(@Argument UUID accountId) {
        return transactionService.getTransactionsByAccount(accountId);
    }

    @MutationMapping
    public Transaction createTransaction(@Argument TransactionInput transaction) {
        Transaction newTransaction = Transaction.builder()
                .accountId(transaction.accountId())
                .type(transaction.type())
                .amount(BigDecimal.valueOf(transaction.amount()))
                .currency(transaction.currency())
                .merchantCategory(transaction.merchantCategory())
                .country(transaction.country())
                .build();
        return transactionService.createTransaction(newTransaction);
    }

    @GraphQlExceptionHandler
    public GraphQLError handleNotFound(TransactionNotFoundException e) {
        return GraphQLError.newError().errorType(ErrorType.NOT_FOUND).message(e.getMessage()).build();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleBusinessRule(BusinessRuleException e) {
        return GraphQLError.newError().errorType(ErrorType.BAD_REQUEST).message(e.getMessage()).build();
    }
}