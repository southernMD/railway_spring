package org.railway.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.railway.dto.request.OrderRequest;
import org.railway.dto.response.OrderResponse;
import org.railway.entity.Order;
import org.railway.entity.User;
import org.railway.service.impl.OrderRepository;
import org.railway.service.impl.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单业务逻辑服务类
 * 提供对 orders 表的创建、更新、删除和查询操作
 */
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    /**
     * 查询所有订单信息
     *
     * @return 所有订单的 DTO 列表
     */
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 根据 ID 查询订单信息
     *
     * @param id 订单唯一标识
     * @return 对应的 DTO 数据
     * @throws EntityNotFoundException 如果未找到对应记录
     */
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("订单未找到"));
        return convertToResponse(order);
    }

    /**
     * 创建一个新的订单记录
     *
     * @param orderRequest 包含订单信息的请求数据
     * @return 创建后的响应数据
     */
    public OrderResponse createOrder(@Valid OrderRequest orderRequest) {
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("用户未找到"));
        Order order = new Order();
        BeanUtils.copyProperties(orderRequest, order);
        order.setUser(user);
        Order savedOrder = orderRepository.save(order);
        return convertToResponse(savedOrder);
    }

    /**
     * 更新指定 ID 的订单记录
     *
     * @param id           订单唯一标识
     * @param orderRequest 包含新数据的请求对象
     * @return 更新后的订单信息
     * @throws EntityNotFoundException 如果未找到对应记录
     */
    public OrderResponse updateOrder(Long id, @Valid OrderRequest orderRequest) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("订单未找到"));
        BeanUtils.copyProperties(orderRequest, existingOrder);
        Order updatedOrder = orderRepository.save(existingOrder);
        return convertToResponse(updatedOrder);
    }

    /**
     * 删除指定 ID 的订单记录
     *
     * @param id 订单唯一标识
     * @throws EntityNotFoundException 如果未找到对应记录
     */
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("订单记录不存在");
        }
        orderRepository.deleteById(id);
    }

    /**
     * 根据用户 ID 查询订单
     *
     * @param userId 用户唯一标识
     * @return 该用户的所有订单的 DTO 列表
     */
    public List<OrderResponse> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 将 Order 实体转换为 OrderResponse DTO
     *
     * @param order 订单实体
     * @return 订单响应 DTO
     */
    private OrderResponse convertToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        BeanUtils.copyProperties(order, response);
        return response;
    }
}
