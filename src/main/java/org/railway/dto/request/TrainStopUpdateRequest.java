package org.railway.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TrainStopUpdateRequest extends TrainStopRequest {
    @NotNull(message = "id 不能为空")
    protected Long id;
}
