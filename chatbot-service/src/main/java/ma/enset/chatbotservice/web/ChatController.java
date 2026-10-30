package ma.enset.chatbotservice.web;

import lombok.RequiredArgsConstructor;
import ma.enset.chatbotservice.agent.BankAIAgent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Yousef ELHAID
 */
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final BankAIAgent bankAIAgent;

    @GetMapping("/chat")
    public String chat(@RequestParam String query,
                       @RequestParam(defaultValue = "web") String conversationId) {
        return bankAIAgent.chat(conversationId, query);
    }
}