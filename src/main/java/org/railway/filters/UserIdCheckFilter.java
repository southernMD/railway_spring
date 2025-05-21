package org.railway.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.railway.dto.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 请求参数过滤器，用于检查请求参数中是否包含 userId 字段。
 * 支持从路径参数、查询参数和请求体中检查 userId。
 */
@Component
public class UserIdCheckFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 包装请求以便多次读取请求体
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);

        // 检查路径参数和查询参数中的 userId
        String userId = request.getParameter("userId");

        // 如果路径参数和查询参数中没有 userId，检查请求体
        if (userId == null || userId.isEmpty()) {
            userId = getPostData(wrappedRequest);
        }

        // 如果 userId 仍然为空，返回错误响应
        if (userId.isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("application/json");
            response.getWriter().write(
                    BaseResponse.error(400, "请求参数中缺少 userId 字段").toString()
            );
            return;
        }

        // 如果 userId 存在，继续执行后续过滤器或请求处理
        filterChain.doFilter(wrappedRequest, response);
    }

    /**
     * 从请求体中提取 userId
     *
     * @param request 请求对象
     * @return 返回 userId，如果不存在则返回 null
     */
    private static String getPostData(HttpServletRequest request) {
        StringBuffer data = new StringBuffer();
        String line = null;
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            while (null != (line = reader.readLine()))
                data.append(line);
        } catch (IOException e) {
            } finally {
        }
        return data.toString();
    }
}
