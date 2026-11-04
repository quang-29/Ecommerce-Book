package org.example.bookstore.payload;

import java.util.UUID;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserShortenDTO {
    private Long userId;
    private String username;
    private String phoneNumber;
    private String email;

}
