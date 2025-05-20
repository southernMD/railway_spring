package org.railway.dto.response;

import lombok.Data;

@Data
public class PassengerResponse {
    private Long id;
    private Long userId;
    private String realName;
    private String idCard;
    private String phone;
    private Integer passengerType;
    private Integer isDefault;
}
