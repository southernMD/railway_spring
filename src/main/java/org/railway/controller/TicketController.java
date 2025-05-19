package org.railway.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.railway.dto.request.TicketRequest;
import org.railway.dto.response.BaseResponse;
import org.railway.dto.response.TicketResponse;
import org.railway.service.TicketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车票控制器类
 * 提供对车票的 CRUD 操作的 HTTP 接口
 */
@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    /**
     * 查询所有车票
     *
     * @return 所有车票的响应列表
     */
    @GetMapping
    public BaseResponse<List<TicketResponse>> getAllTickets() {
        List<TicketResponse> tickets = ticketService.getAllTickets();
        return BaseResponse.success(tickets);
    }

    /**
     * 根据 ID 查询车票
     *
     * @param id 车票唯一标识
     * @return 对应的车票响应
     */
    @GetMapping("/{id}")
    public BaseResponse<TicketResponse> getTicketById(@PathVariable Long id) {
        TicketResponse ticket = ticketService.getTicketById(id);
        return BaseResponse.success(ticket);
    }

    /**
     * 创建一个新车票
     *
     * @param ticketRequest 包含车票信息的请求数据
     * @return 创建后的车票响应
     */
    @PostMapping
    public BaseResponse<TicketResponse> createTicket(@Valid @RequestBody TicketRequest ticketRequest) {
        TicketResponse ticket = ticketService.createTicket(ticketRequest);
        return BaseResponse.success(ticket);
    }

    /**
     * 更新指定 ID 的车票
     *
     * @param id            车票唯一标识
     * @param ticketRequest 包含新数据的请求对象
     * @return 更新后的车票响应
     */
    @PutMapping("/{id}")
    public BaseResponse<TicketResponse> updateTicket(
            @PathVariable Long id,
            @Valid @RequestBody TicketRequest ticketRequest) {
        TicketResponse ticket = ticketService.updateTicket(id, ticketRequest);
        return BaseResponse.success(ticket);
    }

    /**
     * 删除指定 ID 的车票
     *
     * @param id 车票唯一标识
     * @return 无内容响应
     */
    @DeleteMapping("/{id}")
    public BaseResponse<Object> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return BaseResponse.success(null, "删除成功");
    }
}
