package org.railway.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TrainStopUpdateRequest extends TrainStopRequest {
    protected Long id;
}
