package org.railway.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PassengerRequest {
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotBlank(message = "真实姓名不能为空")
    @Size(max = 50, message = "真实姓名长度不能超过50")
    private String realName;

    @NotBlank(message = "身份证号不能为空")
    @Size(max = 18, message = "身份证号长度不能超过18")
    private String idCard;

    @NotBlank(message = "手机号不能为空")
    @Size(max = 20, message = "手机号长度不能超过20")
    private String phone;

    @NotNull(message = "乘客类型不能为空")
    private Integer passengerType;

    @NotNull(message = "是否默认不能为空")
    private Integer isDefault;
}
