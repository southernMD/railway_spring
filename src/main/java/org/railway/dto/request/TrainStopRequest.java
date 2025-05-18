package org.railway.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.railway.entity.TrainStop;

import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class TrainStopRequest extends TrainStop {
    @NotNull(message = "trainId 不能为空")
    private Long trainId;

    @NotNull(message = "stationId 不能为空")
    private Long stationId;

    @NotNull(message = "sequence 不能为空")
    private Integer sequence;

    @NotNull(message = "到达时间不能为空")
    private LocalTime arrivalTime;

    @NotNull(message = "停留时间不能为空")
    private Integer stopDuration;
}
