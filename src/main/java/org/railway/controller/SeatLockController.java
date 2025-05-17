package org.railway.controller;

import org.railway.dto.request.SeatLockRequest;
import org.railway.dto.response.BaseResponse;
import org.railway.dto.response.SeatLockResponse;
import org.railway.service.SeatLockService;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/seat-locks")
public class SeatLockController {

    private final SeatLockService service;

    public SeatLockController(SeatLockService service) {
        this.service = service;
    }

    /**
     * 创建新的座位锁定记录
     */
    @PostMapping
    public BaseResponse<SeatLockResponse> create(@RequestBody SeatLockRequest dto) throws SQLException {
        return BaseResponse.success(service.create(dto));
    }

    /**
     * 删除指定 seat_id 的座位锁定记录
     *
     * @return
     */
    @DeleteMapping("/{seatId}")
    public BaseResponse<Object> delete(@PathVariable Long seatId) {
        service.deleteBySeatId(seatId);
        return BaseResponse.success(null, "删除成功");
    }
}
