package org.example.bookstore.service.dto;

import lombok.*;
import org.example.bookstore.enums.Roles;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserSaveDto {
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String id;
    private String email;
    private String phoneNumber;
    private Roles roles;
    private String avatarUrl;
}
