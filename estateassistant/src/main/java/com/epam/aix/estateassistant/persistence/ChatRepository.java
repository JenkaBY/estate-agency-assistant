package com.epam.aix.estateassistant.persistence;

import com.epam.aix.estateassistant.persistence.entity.Chat;
import com.epam.aix.estateassistant.persistence.projection.ChatIdTitleProjection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface ChatRepository extends MongoRepository<Chat, String> {

    @Query(value = "{}", fields = "{ 'id' : 1, 'title' : 1 }")
    List<ChatIdTitleProjection> findAllChatIdsAndTitles();
}
