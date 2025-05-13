package org.example.bookstore.payload;

import java.util.UUID;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserShortenDTO {
    private UUID userId;
    private String username;
    private String phoneNumber;
    private String email;

}
