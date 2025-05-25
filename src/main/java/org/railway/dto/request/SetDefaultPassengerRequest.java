package org.railway.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SetDefaultPassengerRequest {
    @NotNull(message = "用户id不为空")
    private Long userId;

    @NotNull(message = "乘客id不为空")
    private Long id;
}
