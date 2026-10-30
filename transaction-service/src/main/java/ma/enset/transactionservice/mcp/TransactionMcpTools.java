package ma.enset.transactionservice.mcp;

import lombok.RequiredArgsConstructor;
import ma.enset.transactionservice.domain.Transaction;
import ma.enset.transactionservice.domain.TransactionType;
import ma.enset.transactionservice.service.TransactionService;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Yousef ELHAID
 */
@Component
@RequiredArgsConstructor
public class TransactionMcpTools {

    private final TransactionService transactionService;

    @McpTool(name = "getAllTransactions", description = "Get the list of all bank transactions")
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @McpTool(name = "getTransactionById", description = "Get one bank transaction by its id (UUID)")
    public Transaction getTransactionById(@McpToolParam(description = "Transaction id (UUID)") String id) {
        return transactionService.getTransactionById(UUID.fromString(id));
    }

    @McpTool(name = "getTransactionsByAccount", description = "Get the transactions of one bank account, newest first")
    public List<Transaction> getTransactionsByAccount(@McpToolParam(description = "Account id (UUID)") String accountId) {
        return transactionService.getTransactionsByAccount(UUID.fromString(accountId));
    }

    @McpTool(name = "createTransaction", description = "Record a transaction on a bank account: a payment (DEBIT, default) or a deposit (CREDIT)")
    public Transaction createTransaction(
            @McpToolParam(description = "Account id (UUID)") String accountId,
            @McpToolParam(description = "Amount, strictly positive") double amount,
            @McpToolParam(description = "Currency code with 3 letters, must match the account currency") String currency,
            @McpToolParam(description = "Merchant category, e.g. GROCERY, TRAVEL, ELECTRONICS") String merchantCategory,
            @McpToolParam(description = "Country code, e.g. MA, FR") String country,
            @McpToolParam(description = "DEBIT or CREDIT, DEBIT if not given", required = false) TransactionType type) {
        Transaction transaction = Transaction.builder()
                .accountId(UUID.fromString(accountId))
                .type(type)
                .amount(BigDecimal.valueOf(amount))
                .currency(currency)
                .merchantCategory(merchantCategory)
                .country(country)
                .build();
        return transactionService.createTransaction(transaction);
    }

    @McpTool(name = "transfer", description = "Transfer money from one bank account to another account with the same currency")
    public List<Transaction> transfer(
            @McpToolParam(description = "Source account id (UUID)") String sourceAccountId,
            @McpToolParam(description = "Target account id (UUID)") String targetAccountId,
            @McpToolParam(description = "Amount to transfer, strictly positive") double amount) {
        return transactionService.transfer(UUID.fromString(sourceAccountId), UUID.fromString(targetAccountId), BigDecimal.valueOf(amount));
    }
}