package org.railway.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.railway.utils.TimerFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    private int code;
    private String message;
    private T data;
    private String timestamp;

    public static <T> BaseResponse<T> success(T data,  String message) {
        return new BaseResponse<>(200, message, data, TimerFormat.getNowTime());
    }
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(200, "Success", data, TimerFormat.getNowTime());
    }

    public static <T> BaseResponse<T> error(int code, String message) {
        return new BaseResponse<>(code, message, null, TimerFormat.getNowTime());
    }
}
