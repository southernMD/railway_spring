package org.railway.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.railway.entity.TrainSeat;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TrainRequest {

    @NotBlank(message = "车次编号不能为空")
    private String trainNumber;

    @NotNull(message = "车型ID不能为空")
    private Long modelId;

    @NotNull(message = "起始站ID不能为空")
    private Long startStationId;

    @NotNull(message = "终点站ID不能为空")
    private Long endStationId;

    @NotNull(message = "出发日期不能为空")
    private LocalDate date;

    @NotNull(message = "出发时间不能为空")
    private LocalTime departureTime;

    @NotNull(message = "到达时间不能为空")
    private LocalTime arrivalTime;

    @NotNull(message = "座位信息不能为空")
    private TrainSeatRequest trainSeatInfo;
}
