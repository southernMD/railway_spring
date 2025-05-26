package org.railway.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TicketUpdateRequest extends TicketRequest{
    @NotNull(message = "用户ID不能为空")
    private Long userId;
}
