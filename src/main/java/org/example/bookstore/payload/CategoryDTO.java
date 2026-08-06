package org.example.bookstore.payload;

import lombok.*;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryDTO {
    private Long id;
    private String categoryName;
    private String category_img;
}
