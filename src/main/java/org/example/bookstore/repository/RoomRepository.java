package org.example.bookstore.repository;

import org.example.bookstore.model.chat.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {

    @Query("SELECT r FROM Room r WHERE r.roomId = :roomId")
    Room findByRoomId(@Param("roomId") String roomId);
}
