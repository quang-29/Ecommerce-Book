package org.example.bookstore.config.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.bookstore.enums.ResponseCase;

@Getter
@Setter
public class ServerResponseDto {
    private final ResponseCase status;
    private Object data;

    private ServerResponseDto(ResponseCase status, Object data){
        this.status = status;
        this.data = data;
    }

    public static ServerResponseDto success(Object data) {
        return new ServerResponseDto(ResponseCase.SUCCESS, data);
    };

    public static ServerResponseDto error(Object data) {
        return new ServerResponseDto(ResponseCase.ERROR, data);
    }

    public static ServerResponseDto with(ResponseCase status, Object data){
        return new ServerResponseDto(status, data);
    }

}
