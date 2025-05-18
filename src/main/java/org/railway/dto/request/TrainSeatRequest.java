package org.railway.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.railway.entity.TrainSeat;

import java.math.BigDecimal;

@Data
public class TrainSeatRequest {

    @NotNull(message = "无座票数量不能为空")
    private Integer noSeatTickets;

    @NotNull(message = "商务座价格不能为空")
    @DecimalMin(value = "0.0", message = "商务座价格不能小于0")
    private BigDecimal businessPrice;

    @NotNull(message = "一等座价格不能为空")
    @DecimalMin(value = "0.0", message = "一等座价格不能小于0")
    private BigDecimal firstClassPrice;

    @NotNull(message = "二等座价格不能为空")
    @DecimalMin(value = "0.0", message = "二等座价格不能小于0")
    private BigDecimal secondClassPrice;

    @NotNull(message = "无座价格不能为空")
    @DecimalMin(value = "0.0", message = "无座价格不能小于0")
    private BigDecimal noSeatPrice;
}
