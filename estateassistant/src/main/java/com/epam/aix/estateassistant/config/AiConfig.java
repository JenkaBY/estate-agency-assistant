package com.epam.aix.estateassistant.config;

import com.epam.aix.estateassistant.config.prompt.PromptProvider;
import com.epam.aix.estateassistant.service.dto.UserGatheredPropertiesSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.StructuredOutputValidationAdvisor;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.mongo.MongoChatMemoryRepository;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@RequiredArgsConstructor
@Configuration
public class AiConfig {

    public static final SimpleLoggerAdvisor SIMPLE_LOGGER_ADVISOR = new SimpleLoggerAdvisor();

    @Value("${app.chatbot.model}")
    private final String chatBotModel;

    @Value("${app.properties-generator.model}")
    private final String generatorModel;

    private final PromptProvider chatAgencyPromptProvider;
    private final PromptProvider fakeRealEstateApiPromptProvider;

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
    ChatClient agentChatClient(ChatClient.Builder builder, PromptChatMemoryAdvisor promptChatMemoryAdvisor) {

        var structuredValidatorAdvisor = StructuredOutputValidationAdvisor.builder()
                .outputType(UserGatheredPropertiesSearch.class)
                .maxRepeatAttempts(3)
                .build();

        return builder
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(chatBotModel)
                        .temperature(1.0)
                        .build())
                .defaultSystem(chatAgencyPromptProvider.getSystemPrompt().getContents())
                .defaultAdvisors(promptChatMemoryAdvisor, SIMPLE_LOGGER_ADVISOR, structuredValidatorAdvisor)
                .build();
    }

    @Bean
    ChatClient propertiesGeneratorClient(ChatClient.Builder builder, PromptChatMemoryAdvisor promptChatMemoryAdvisor) {
        return builder
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(generatorModel)
                        .temperature(1.0)
                        .build())
                .defaultSystem(fakeRealEstateApiPromptProvider.getSystemPrompt().getContents())
                .defaultAdvisors(promptChatMemoryAdvisor, SIMPLE_LOGGER_ADVISOR)
                .build();
    }
}
