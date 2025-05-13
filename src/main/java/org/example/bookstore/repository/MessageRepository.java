package org.example.bookstore.repository;

import org.example.bookstore.model.chat.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {
    List<Message> findAllByRoomId(String roomId);

}
