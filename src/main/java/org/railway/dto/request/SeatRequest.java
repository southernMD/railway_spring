package org.railway.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 座位创建/更新请求参数
 */
@Data
public class SeatRequest {
    @NotNull(message = "车厢ID不能为空")
    private Long carriageId;

    @NotNull(message = "座位编号不能为空")
    private String seatNumber;

    private Integer status = 1;
}
