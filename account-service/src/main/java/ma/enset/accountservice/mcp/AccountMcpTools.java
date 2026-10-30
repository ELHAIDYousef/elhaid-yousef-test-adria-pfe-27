package ma.enset.accountservice.mcp;

import lombok.RequiredArgsConstructor;
import ma.enset.accountservice.domain.Account;
import ma.enset.accountservice.domain.AccountStatus;
import ma.enset.accountservice.service.AccountService;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author Yousef ELHAID
 */
@Component
@RequiredArgsConstructor
public class AccountMcpTools {

    private final AccountService accountService;

    @McpTool(name = "getAllAccounts", description = "Get the list of all bank accounts with their status, currency and balance")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @McpTool(name = "getAccountById", description = "Get one bank account by its id (UUID)")
    public Account getAccountById(@McpToolParam(description = "Account id (UUID)") String id) {
        return accountService.getAccountById(UUID.fromString(id));
    }

    @McpTool(name = "createAccount", description = "Create a new ACTIVE bank account")
    public Account createAccount(
            @McpToolParam(description = "Customer reference, e.g. CUST-007") String customerReference,
            @McpToolParam(description = "Currency code with 3 letters, e.g. MAD, EUR, USD") String currency,
            @McpToolParam(description = "Initial balance, zero or positive") double balance) {
        Account account = Account.builder()
                .customerReference(customerReference)
                .currency(currency)
                .balance(balance)
                .status(AccountStatus.ACTIVE)
                .build();
        return accountService.createAccount(account);
    }

    @McpTool(name = "changeAccountStatus", description = "Activate or block a bank account")
    public Account changeAccountStatus(
            @McpToolParam(description = "Account id (UUID)") String id,
            @McpToolParam(description = "New status: ACTIVE or BLOCKED") AccountStatus status) {
        return accountService.changeStatus(UUID.fromString(id), status);
    }
}