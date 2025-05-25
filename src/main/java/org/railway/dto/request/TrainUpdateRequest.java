package org.railway.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TrainUpdateRequest extends TrainRequest {
    private Long id;
}
