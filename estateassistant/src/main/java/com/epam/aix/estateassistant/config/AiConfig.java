package com.epam.aix.estateassistant.config;

import com.epam.aix.estateassistant.ai.PromptProvider;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.mongo.MongoChatMemoryRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class AiConfig {

    public static final SimpleLoggerAdvisor SIMPLE_LOGGER_ADVISOR = new SimpleLoggerAdvisor();

    @Bean
    public PromptChatMemoryAdvisor promptChatMemoryAdvisor(
            @Qualifier("customChatMemoryRepository") ChatMemoryRepository customChatMemoryRepository) {

        var chatMessageWindow = MessageWindowChatMemory
                .builder()
                .chatMemoryRepository(customChatMemoryRepository)
                .build();

        return PromptChatMemoryAdvisor
                .builder(chatMessageWindow)
                .build();
    }

    @Bean("customChatMemoryRepository")
    public ChatMemoryRepository customChatMemoryRepository(MongoTemplate dataSource) {
        return MongoChatMemoryRepository
                .builder()
                .mongoTemplate(dataSource)
                .build();
    }

    @Bean
    ChatClient agentChatClient(ChatClient.Builder builder, PromptChatMemoryAdvisor promptChatMemoryAdvisor,
                               PromptProvider promptProvider) {
        return builder
                .defaultSystem(promptProvider.getSystemPromptForAgent().getContents())
                .defaultAdvisors(promptChatMemoryAdvisor, SIMPLE_LOGGER_ADVISOR)
                .build();
    }

    @Bean
    ChatClient propertiesGeneratorClient(ChatClient.Builder builder, PromptChatMemoryAdvisor promptChatMemoryAdvisor,
                                         PromptProvider promptProvider) {
        return builder
                .defaultSystem(promptProvider.getPropertiesDataGeneratorPrompt().getContents())
                .defaultAdvisors(promptChatMemoryAdvisor, SIMPLE_LOGGER_ADVISOR)
                .build();
    }
}
