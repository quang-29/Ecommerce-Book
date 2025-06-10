package org.example.bookstore.payload.request;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Data
@Getter
@Setter
public class RoomCreate {

    private String roomId;
    private UUID userId;
    private String userAvatar;
    private String userName;
    private Date createdAt = new Date();
}
