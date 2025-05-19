package org.railway.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.railway.dto.Views;
import org.railway.dto.request.OrderRequest;
import org.railway.dto.response.BaseResponse;
import org.railway.dto.response.OrderResponse;
import org.railway.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单控制器类
 * 提供对订单的 CRUD 操作的 HTTP 接口
 */
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 查询所有订单
     *
     * @return 所有订单的响应列表
     */
    @GetMapping
    @JsonView(Views.Basic.class)
    public BaseResponse<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return BaseResponse.success(orders);
    }

    /**
     * 根据 ID 查询订单
     *
     * @param id 订单唯一标识
     * @return 对应的订单响应
     */
    @GetMapping("/{id}")
    public BaseResponse<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderResponse order = orderService.getOrderById(id);
        return BaseResponse.success(order);
    }

    /**
     * 创建一个新订单
     *
     * @param orderRequest 包含订单信息的请求数据
     * @return 创建后的订单响应
     */
    @PostMapping
    public BaseResponse<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse order = orderService.createOrder(orderRequest);
        return BaseResponse.success(order);
    }

    /**
     * 更新指定 ID 的订单
     *
     * @param id           订单唯一标识
     * @param orderRequest 包含新数据的请求对象
     * @return 更新后的订单响应
     */
    @PutMapping("/{id}")
    public BaseResponse<OrderResponse> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderRequest orderRequest) {
        OrderResponse order = orderService.updateOrder(id, orderRequest);
        return BaseResponse.success(order);
    }

    /**
     * 删除指定 ID 的订单
     *
     * @param id 订单唯一标识
     * @return 无内容响应
     */
    @DeleteMapping("/{id}")
    public BaseResponse<Object> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return BaseResponse.success(null, "删除成功");
    }

    /**
     * 根据用户 ID 查询订单
     *
     * @param userId 用户唯一标识
     * @return 该用户的所有订单的响应列表
     */
    @GetMapping("/user/{userId}")
    @JsonView(Views.Basic.class)
    public BaseResponse<List<OrderResponse>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderResponse> orders = orderService.getOrdersByUserId(userId);
        return BaseResponse.success(orders);
    }
}
