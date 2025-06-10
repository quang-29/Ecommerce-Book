package org.example.bookstore.controller;


import lombok.Getter;
import org.example.bookstore.model.chat.Room;
import org.example.bookstore.payload.request.RoomCreate;
import org.example.bookstore.repository.RoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @PostMapping("")
    public ResponseEntity<?> createRoom(@RequestBody RoomCreate roomCreate) {
        Room room = roomRepository.findByRoomId(roomCreate.getRoomId());
        if (room != null) {
            return new ResponseEntity<>("Room is already existed", HttpStatus.BAD_REQUEST);
        }
        Room newRoom = new Room();
        newRoom.setRoomId(roomCreate.getRoomId());
        newRoom.setUserAvatar(roomCreate.getUserAvatar());
        newRoom.setUserId(roomCreate.getUserId());
        newRoom.setUserName(roomCreate.getUserName());
        newRoom.setCreatedAt(new Date());
        roomRepository.save(newRoom);
        return new ResponseEntity<>(room, HttpStatus.CREATED);
    }

    // get Room
    @GetMapping("/{roomId}")
    public ResponseEntity<?> getRoom(@PathVariable String roomId) {
        Room room = roomRepository.findByRoomId(roomId);

        if (room == null) {
            return new ResponseEntity<>("Room not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(room, HttpStatus.OK);

    }
    @GetMapping("/getAllRooms")
    public ResponseEntity<?> getAllRoom() {
        List<Room> rooms = roomRepository.findAll();
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }
}
