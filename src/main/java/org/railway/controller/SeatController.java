package org.railway.controller;

import jakarta.transaction.Transactional;
import org.railway.dto.request.SeatRequest;
import org.railway.dto.response.BaseResponse;
import org.railway.dto.response.SeatResponse;
import org.railway.service.SeatService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 座位 API 控制器
 */
@RestController
@RequestMapping("/seats")
public class SeatController {

    private final SeatService service;

    public SeatController(SeatService service) {
        this.service = service;
    }

    /**
     * 创建座位
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BaseResponse<SeatResponse> create(@RequestBody SeatRequest dto) {
        return BaseResponse.success(service.create(dto));
    }

    /**
     * 批量创建座位
     */
    @PostMapping("/batch")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BaseResponse<List<SeatResponse>> createBatch(@RequestBody List<SeatRequest> dtos) {
        return BaseResponse.success(dtos.stream()
                .map(service::create)
                .collect(Collectors.toList()));
    }

    /**
     * 更新座位
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BaseResponse<SeatResponse> update(@PathVariable Long id, @RequestBody SeatRequest dto) {
        return BaseResponse.success(service.update(id, dto));
    }

    /**
     * 删除座位
     *
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BaseResponse<Object> delete(@PathVariable Long id) {
        service.delete(id);
        return BaseResponse.success(null, "删除成功");
    }

    /**
     * 查询单个座位
     */
    @GetMapping("/{id}")
    public BaseResponse<SeatResponse> getById(@PathVariable Long id) {
        return BaseResponse.success(service.getById(id));
    }

    /**
     * 根据车厢查询座位
     */
    @GetMapping("/carriage/{carriageId}")
    public BaseResponse<List<SeatResponse>> getByCarriageId(@PathVariable Long carriageId) {
        return BaseResponse.success(service.getByCarriageId(carriageId));
    }
}
