package org.railway.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class TrainStopBatchUpdateRequest {
    @NotNull(message = "trainStops 不能为空")
    @Valid
    private List<TrainStopUpdateRequest> trainStops;

    @NotNull(message = "trainId 不能为空")
    private Long trainId;
}
