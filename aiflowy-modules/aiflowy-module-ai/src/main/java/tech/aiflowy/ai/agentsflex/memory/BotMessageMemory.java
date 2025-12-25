package tech.aiflowy.ai.agentsflex.memory;

import com.agentsflex.core.memory.ChatMemory;
import com.agentsflex.core.message.Message;
import com.mybatisflex.core.query.QueryWrapper;
import tech.aiflowy.ai.entity.BotMessage;
import tech.aiflowy.ai.service.BotMessageService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BotMessageMemory implements ChatMemory {
    private final BigInteger botId;
    private final BigInteger accountId;
    private final BigInteger conversationId;
    private final BotMessageService messageService;
    
    public BotMessageMemory(BigInteger botId, BigInteger accountId, BigInteger conversationId,
                            BotMessageService messageService) {
        this.botId = botId;
        this.accountId = accountId;
        this.conversationId = conversationId;
        this.messageService = messageService;
    }

    @Override
    public List<Message> getMessages(int count) {
        List<BotMessage> sysAiMessages = messageService.list(QueryWrapper.create()
                .eq(BotMessage::getBotId, botId, true)
                .eq(BotMessage::getAccountId, accountId, true)
                .eq(BotMessage::getConversationId, conversationId, true)
                .orderBy(BotMessage::getCreated, true)
                .limit(count)
        );

        if (sysAiMessages == null || sysAiMessages.isEmpty()) {
            return null;
        }

        List<Message> messages = new ArrayList<>(sysAiMessages.size());
        for (BotMessage botMessage : sysAiMessages) {
            Message message = botMessage.getContentAsMessage();
            if (message != null) messages.add(message);
        }
        return messages;
    }


    @Override
    public void addMessage(Message message) {

        BotMessage dbMessage = new BotMessage();
        dbMessage.setCreated(new Date());
        dbMessage.setBotId(botId);
        dbMessage.setAccountId(accountId);
        dbMessage.setConversationId(conversationId);
        dbMessage.setContentAndRole(message);
        dbMessage.setModified(new Date());
        messageService.save(dbMessage);
    }

    @Override
    public void clear() {

    }

    @Override
    public Object id() {
        return botId;
    }

}
