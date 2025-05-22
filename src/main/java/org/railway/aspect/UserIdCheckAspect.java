package org.railway.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.railway.service.impl.UserRepository;
import org.railway.utils.JwtUtil;
import org.railway.utils.JwtUserInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.railway.entity.User;
@Aspect
@Component
@AllArgsConstructor
public class UserIdCheckAspect {

    private final UserRepository userRepository;

    @Around("@annotation(org.railway.annotation.CheckUserId)")
    public Object checkUserId(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取请求对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // 从请求头中获取 JWT
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 从 JWT 中提取用户信息
        JwtUserInfo userInfo = JwtUtil.extractUserInfo(token);
        Long jwtUserId = userInfo.getUserId();

        Object[] args = joinPoint.getArgs();
        Long userId = null;

        for (Object arg : args) {
            if (arg == null) continue;
            // 直接是Long类型
            if (arg instanceof Long) {
                userId = (Long) arg;
                break;
            }
            // 反射查找userId字段
            try {
                java.lang.reflect.Field field = arg.getClass().getDeclaredField("userId");
                field.setAccessible(true);
                Object value = field.get(arg);
                if (value != null) {
                    userId = Long.valueOf(value.toString());
                    break;
                }
            } catch (NoSuchFieldException | IllegalAccessException ignored) {}
        }

        if (userId == null) {
            String userIdStr = request.getParameter("userId");
            if (userIdStr != null) {
                userId = Long.parseLong(userIdStr);
            }
        }
        Integer userType = userRepository.findById(jwtUserId)
                .map(User::getUserType)
                .orElse(null);

        if (userType != null && userType != 1 && userId != null && !userId.equals(jwtUserId)) {
            throw new RuntimeException("无权操作该用户的数据");
        }
        return joinPoint.proceed();
    }
}
