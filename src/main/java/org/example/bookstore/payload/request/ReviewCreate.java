package org.example.bookstore.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCreate {

    @JsonProperty("user")
    private Long userId;

    @JsonProperty("book")
    private Long bookId;

    private String content;
    private int rating;

}
