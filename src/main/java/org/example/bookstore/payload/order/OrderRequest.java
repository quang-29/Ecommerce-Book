package org.example.bookstore.payload.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.bookstore.enums.PaymentType;

import java.util.List;

@Getter
@Setter
public class OrderRequest {

    @NotEmpty
    private List<@Valid OrderItemRequest> items;

    @NotBlank
    private String shippingAddress;

    @NotNull
    private PaymentType paymentType;

    @Getter
    @Setter
    public static class OrderItemRequest {
        @NotNull
        private Long storeId;

        // Voucher của cửa hàng này (tùy chọn)
        private String voucherCode;

        @NotEmpty
        private List<@Valid BookRequest> books;
    }

    @Getter
    @Setter
    public static class BookRequest {
        @NotNull
        private Long bookId;

        @NotNull
        @Min(1)
        private Integer quantity;
    }
}
