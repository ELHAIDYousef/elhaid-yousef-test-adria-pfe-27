package ma.enset.chatbotservice.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yousef ELHAID
 */
@Service
public class BankAIAgent {

    private final ChatClient chatClient;

    public BankAIAgent(ChatClient.Builder builder, ChatMemory chatMemory, ToolCallbackProvider tools) {
        this.chatClient = builder
                .defaultSystem("""
                        You are a banking assistant. You use the available tools to read bank accounts,
                        read transactions, record payments and make transfers.
                        Accounts are identified by a UUID id. When the user gives a customer reference
                        such as CUST-002, first call getAllAccounts to find the matching account id.
                        Payments and transfers must always go through the transaction tools.
                        If a tool returns an error, explain it simply to the user.
                        If the tools don't give the information, say that you don't know.
                        Answer in the user's language.
                        """)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultToolCallbacks(tools)
                .build();
    }

    public String chat(String conversationId, String query) {
        return chatClient.prompt()
                .user(query)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
    }
}