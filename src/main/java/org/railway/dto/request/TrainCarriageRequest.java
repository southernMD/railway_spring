package org.railway.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.railway.entity.TrainCarriage;

/**
 * 车厢创建/更新请求参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TrainCarriageRequest extends TrainCarriage {
    @NotNull(message = "车型ID不能为空")
    private Integer modelId;

    @NotNull(message = "车厢编号不能为空")
    private Integer carriageNumber;

    @NotNull(message = "车厢类型不能为空")
    private Integer carriageType;

    @NotNull(message = "座位数量不能为空")
    private Integer seatCount;
}
