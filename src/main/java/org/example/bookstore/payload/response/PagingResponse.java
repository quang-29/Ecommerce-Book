package org.example.bookstore.payload.response;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PagingResponse<T> {
    private T data;
    private String message;
    private int totalPage;
    private Long totalCount;
    private int totalNew;
}
