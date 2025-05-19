package org.railway.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderRequest {

    @NotNull(message = "订单号不能为空")
    @Size(min = 1, max = 32, message = "订单号长度必须在1到32之间")
    private String orderNo;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotNull(message = "总金额不能为空")
    @DecimalMin(value = "0.0", inclusive = false, message = "总金额必须大于0")
    private BigDecimal totalAmount;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值必须大于等于0")
    @Max(value = 2, message = "状态值必须小于等于2")
    private Integer status;

    @FutureOrPresent(message = "支付时间必须是当前或将来时间")
    private LocalDateTime paymentTime;
}
