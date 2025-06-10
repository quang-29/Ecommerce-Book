package org.example.bookstore.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;


import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Size(min = 3, max = 20, message = "First Name must be between 5 and 20 characters long")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "First Name must not contain numbers or special characters")
    @Column(name = "first_name")
    private String firstName;

    @Size(min = 3, max = 20, message = "Last Name must be between 3 and 20 characters long")
    @Column(name = "last_name")
    private String lastName;

    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters long ")
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "avatar")
    private String avatarUrl ;

    @Size(min = 10, max = 10, message = "Mobile Number must be exactly 10 digits long")
    @Pattern(regexp = "^\\d{10}$", message = "Mobile Number must contain only Numbers")
    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Order> orders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @Column(name = "deviceToken")
    private String deviceToken;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "users_liked_books",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> likedBooks = new HashSet<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notifications> notifications = new ArrayList<>();

}
