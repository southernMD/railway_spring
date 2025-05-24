package org.railway.dto.request;

import lombok.Data;

@Data
public class SeatBatchDelRequest {
    private Long[] seatIds;
}
