package org.railway.exception;

import jakarta.security.auth.message.AuthException;
import jakarta.validation.ConstraintViolationException;
import org.railway.dto.response.BaseResponse;
import org.railway.utils.TimerFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.security.access.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalErrorControllerAdvice {

    //登录错误
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<BaseResponse<Void>> handleBadCredentials(BadCredentialsException es) {
        BaseResponse<Void> response = BaseResponse.error(HttpStatus.UNAUTHORIZED.value(), es.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    //  处理 @RequestBody + @Valid 校验失败
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            if (error instanceof FieldError fieldError) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
        });

        BaseResponse<Map<String, String>> response = new BaseResponse<>(
                HttpStatus.BAD_REQUEST.value(),
                "参数校验失败",
                errors,
                TimerFormat.getNowTime()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    //  处理 URL 参数、路径变量等非 @RequestBody 的校验失败
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        BaseResponse<Void> response = BaseResponse.error(HttpStatus.BAD_REQUEST.value(), "参数验证失败：" + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    //401
    @ExceptionHandler({AccessDeniedException.class, AuthException.class})
    public ResponseEntity<BaseResponse<Void>> handleAccessDeniedAndAuthException(Exception ex) {
        BaseResponse<Void> response = BaseResponse.error(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleOtherErrors(Exception ex) {
        System.out.println("捕获的异常类型: " + ex.getClass().getName());
        BaseResponse<Void> response = BaseResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
