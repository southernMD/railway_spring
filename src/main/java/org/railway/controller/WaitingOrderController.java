package org.railway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.railway.dto.request.WaitingOrderRequest;
import org.railway.dto.response.BaseResponse;
import org.railway.entity.WaitingOrder;
import org.railway.service.WaitingOrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 候补订单控制器
 */
@RestController
@RequestMapping("/waiting-orders")
@RequiredArgsConstructor
@Tag(name = "候补订单管理", description = "提供候补订单的CRUD操作")
public class WaitingOrderController {

    private final WaitingOrderService waitingOrderService;

    /**
     * 创建候补订单
     * @param request 候补订单请求DTO，包含候补订单的详细信息
     * @return 返回创建的候补订单实体
     */
    @Operation(
            summary = "创建候补订单",
            description = "根据传入的候补订单信息创建新的候补订单",
            responses = {
                    @ApiResponse(responseCode = "200", description = "候补订单创建成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数无效")
            }
    )
    @PostMapping
    public BaseResponse<WaitingOrder> create(@RequestBody WaitingOrderRequest request) {
        return BaseResponse.success(waitingOrderService.createWaitingOrder(request));
    }

    /**
     * 获取候补订单详情
     * @param id 候补订单唯一标识
     * @return 返回查询到的候补订单实体
     */
    @Operation(
            summary = "获取候补订单详情",
            description = "根据候补订单ID获取候补订单的详细信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "候补订单获取成功"),
                    @ApiResponse(responseCode = "404", description = "候补订单不存在")
            }
    )
    @GetMapping("/{id}")
    public BaseResponse<WaitingOrder> getById(@PathVariable Long id) {
        return BaseResponse.success(waitingOrderService.getWaitingOrder(id));
    }

    /**
     * 获取用户的所有候补订单
     * @param userId 用户唯一标识
     * @return 返回该用户的所有候补订单列表
     */
    @Operation(
            summary = "获取用户的所有候补订单",
            description = "根据用户ID获取该用户的所有候补订单",
            responses = {
                    @ApiResponse(responseCode = "200", description = "候补订单列表获取成功"),
                    @ApiResponse(responseCode = "404", description = "用户不存在")
            }
    )
    @GetMapping("/user/{userId}")
    public BaseResponse<List<WaitingOrder>> getByUser(@PathVariable Long userId) {
        return BaseResponse.success(waitingOrderService.getUserWaitingOrders(userId));
    }

    /**
     * 更新候补订单
     * @param id 候补订单唯一标识
     * @param request 候补订单请求DTO，包含更新的候补订单信息
     * @return 返回更新后的候补订单实体
     */
    @Operation(
            summary = "更新候补订单",
            description = "根据候补订单ID和传入的候补订单信息更新候补订单的详细信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "候补订单更新成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数无效"),
                    @ApiResponse(responseCode = "404", description = "候补订单不存在")
            }
    )
    @PutMapping("/{id}")
    public BaseResponse<WaitingOrder> update(
            @PathVariable Long id,
            @RequestBody WaitingOrderRequest request) {
        return BaseResponse.success(waitingOrderService.updateWaitingOrder(id, request));
    }

    /**
     * 取消候补订单
     * @param id 候补订单唯一标识
     * @return 返回成功信息
     */
    @Operation(
            summary = "取消候补订单",
            description = "根据候补订单ID取消候补订单",
            responses = {
                    @ApiResponse(responseCode = "200", description = "候补订单取消成功"),
                    @ApiResponse(responseCode = "404", description = "候补订单不存在")
            }
    )
    @DeleteMapping("/{id}")
    public BaseResponse<Void> cancel(@PathVariable Long id) {
        waitingOrderService.cancelWaitingOrder(id);
        return BaseResponse.success(null,"取消成功");
    }
}