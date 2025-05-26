package org.railway.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.View;
import org.railway.annotation.CheckUserId;
import org.railway.dto.Views;
import org.railway.dto.request.TicketRequest;
import org.railway.dto.request.TicketUpdateRequest;
import org.railway.dto.request.TicketUpdateStatusRequest;
import org.railway.dto.response.BaseResponse;
import org.railway.dto.response.TicketDetailResponse;
import org.railway.dto.response.TicketResponse;
import org.railway.service.TicketService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

/**
 * 车票控制器类
 * 提供对车票的 CRUD 操作的 HTTP 接口
 */
@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
@Tag(name = "车票管理", description = "提供车票的CRUD操作")
public class TicketController {

    private final TicketService ticketService;


    /**
     * 查询所有车票
     * @return 返回所有车票的响应列表
     */
    @Operation(
            summary = "查询所有车票",
            description = "获取系统中所有车票的详细信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "车票列表获取成功")
            }
    )
    @GetMapping
    public BaseResponse<List<TicketResponse>> getAllTickets() {
        List<TicketResponse> tickets = ticketService.getAllTickets();
        return BaseResponse.success(tickets);
    }

    /**
     * 根据 ID 查询车票
     * @param id 车票唯一标识
     * @return 返回对应的车票响应
     */
    @Operation(
            summary = "根据ID查询车票",
            description = "根据车票ID获取车票的详细信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "车票获取成功"),
                    @ApiResponse(responseCode = "404", description = "车票不存在")
            }
    )
    @GetMapping("/{id}")
    public BaseResponse<TicketResponse> getTicketById(@PathVariable Long id) {
        TicketResponse ticket = ticketService.getTicketById(id);
        return BaseResponse.success(ticket);
    }
    /**
     * 根据 ID 获取详细车票信息
     * @param id 车票唯一标识
     * @return 返回对应的车票响应
     */
    @Operation(
            summary = "根据ID获取详细车票信息",
            description = "根据车票ID获取车票的详细信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "车票获取成功"),
                    @ApiResponse(responseCode = "404", description = "车票不存在")
            }
    )
    @GetMapping("/detail/{id}")
    public BaseResponse<TicketDetailResponse> getTicketDetailById(@PathVariable Long id) {
        return BaseResponse.success(ticketService.createTicketDetail(id));
    }

    /**
     * 创建一个新车票
     * @param ticketRequest 车票请求DTO，包含车票的详细信息
     * @return 返回创建的车票响应DTO
     * @throws SQLException 如果车票创建过程中发生数据库异常
     */
    @Operation(
            summary = "创建车票",
            description = "根据传入的车票信息创建新的车票",
            responses = {
                    @ApiResponse(responseCode = "200", description = "车票创建成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数无效"),
                    @ApiResponse(responseCode = "500", description = "数据库异常")
            }
    )
    @PostMapping
    @JsonView(Views.Basic.class)
    @CheckUserId
    public BaseResponse<TicketResponse> createTicket(@Valid @RequestBody TicketRequest ticketRequest) throws SQLException {
        TicketResponse ticket = ticketService.createTicket(ticketRequest);
        return BaseResponse.success(ticket);
    }

    /**
     * 更新指定 ID 的车票
     * @param id 车票唯一标识
     * @param ticketRequest 车票请求DTO，包含更新的车票信息
     * @return 返回更新后的车票响应DTO
     * @throws SQLException 如果车票更新过程中发生数据库异常
     */
    @Operation(
            summary = "更新车票",
            description = "根据车票ID和传入的车票信息更新车票的详细信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "车票更新成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数无效"),
                    @ApiResponse(responseCode = "404", description = "车票不存在"),
                    @ApiResponse(responseCode = "500", description = "数据库异常")
            }
    )
    @PutMapping("/{id}")
    @CheckUserId
    public BaseResponse<TicketResponse> updateTicket(
            @PathVariable Long id,
            @Valid @RequestBody TicketUpdateRequest ticketRequest) throws SQLException {
        TicketResponse ticket = ticketService.updateTicket(id, ticketRequest);
        return BaseResponse.success(ticket);
    }

    /**
     * 删除指定 ID 的车票
     * @param id 车票唯一标识
     * @return 返回成功信息
     */
    @Operation(
            summary = "删除车票",
            description = "根据车票ID删除车票",
            responses = {
                    @ApiResponse(responseCode = "200", description = "车票删除成功"),
                    @ApiResponse(responseCode = "404", description = "车票不存在")
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public BaseResponse<Object> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return BaseResponse.success(null, "删除成功");
    }

    /*
    * 更新指定车票状态
    *
    * */
    @Operation(
            summary = "更新车票状态",
            description = "根据车票ID和状态值更新车票的状态",
            responses = {
                    @ApiResponse(responseCode = "200", description = "车票状态更新成功"),
                    @ApiResponse(responseCode = "404", description = "车票不存在")
            }
    )
    @PutMapping("/status")
    @CheckUserId
    public BaseResponse<TicketResponse> updateTicketStatus(
            @Valid @RequestBody TicketUpdateStatusRequest ticketUpdateStatusRequest) throws SQLException {
        return BaseResponse.success(ticketService.updateTicketStatus(ticketUpdateStatusRequest),  "更新成功");
    }
}
