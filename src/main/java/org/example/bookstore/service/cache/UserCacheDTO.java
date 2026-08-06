package org.example.bookstore.service.cache;

import lombok.*;
import org.example.bookstore.enums.Roles;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCacheDTO {
    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String avatarUrl;
    private Roles roles;
    private Boolean isActive;
    private Boolean isDeleted;
    private Boolean isDisabled;
    private long createdTime;
}
