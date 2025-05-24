package org.railway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.railway.dto.request.SeatLockRequest;
import org.railway.dto.response.BaseResponse;
import org.railway.dto.response.SeatLockResponse;
import org.railway.service.SeatLockService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
/**
 * 座位锁定控制器，提供对座位锁定的 CRUD 操作。
 * 包括创建座位锁定、删除座位锁定等功能。
 */
@RestController
@RequestMapping("/seat-locks")
@Tag(name = "座位锁定管理", description = "提供座位锁定的CRUD操作")
public class SeatLockController {

    private final SeatLockService service;

    public SeatLockController(SeatLockService service) {
        this.service = service;
    }


    /**
     * 创建新的座位锁定记录
     * @param dto 座位锁定请求DTO，包含座位锁定的详细信息
     * @return 返回创建的座位锁定响应DTO
     * @throws SQLException 如果该时刻座位已有未完成的锁定任务
     */
    @Operation(
            summary = "创建座位锁定记录",
            description = "根据传入的座位锁定信息创建新的座位锁定记录",
            responses = {
                    @ApiResponse(responseCode = "200", description = "座位锁定记录创建成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数无效"),
                    @ApiResponse(responseCode = "409", description = "该时刻座位已有未完成的锁定任务")
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public BaseResponse<SeatLockResponse> create(@Valid @RequestBody SeatLockRequest dto) throws SQLException {
        return BaseResponse.success(service.create(dto));
    }

    /**
     * 取消指定 id 的座位锁定记录
     * @param id 锁定记录的唯一标识
     * @return 返回成功信息
     */
    @Operation(
            summary = "取消座位锁定记录",
            description = "根据座位ID取消座位锁定记录",
            responses = {
                    @ApiResponse(responseCode = "200", description = "座位锁定记录取消成功"),
                    @ApiResponse(responseCode = "404", description = "座位锁定记录不存在")
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public BaseResponse<Object> delete(@PathVariable Long id) {
        return BaseResponse.success(service.deleteBySeatId(id), "取消成功");
    }
    /**
     * 完成指定 id 的座位锁定记录
     * @param id 锁定记录的唯一标识
     * @return 返回成功信息
     */
    @Operation(
            summary = "完成座位锁定记录",
            description = "根据座位ID完成座位锁定记录",
            responses = {
                    @ApiResponse(responseCode = "200", description = "座位锁定记录完成成功"),
                    @ApiResponse(responseCode = "404", description = "座位锁定记录不存在")
            }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public BaseResponse<Object> complete(@PathVariable Long id) {
        return BaseResponse.success(service.completeBySeatId(id), "完成成功");
    }
}
