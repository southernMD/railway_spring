package org.railway.dto.response;

import lombok.Data;
import java.time.LocalTime;

@Data
public class TrainStopResponse {
    private Long id;
    private Long trainId;
    private Long stationId;
    private Integer sequence;
    private LocalTime arrivalTime;
    private Integer stopDuration;
}
