package org.railway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
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
@Tag(name = "座位管理", description = "提供座位的CRUD操作")
public class SeatController {

    private final SeatService service;

    public SeatController(SeatService service) {
        this.service = service;
    }

    /**
     * 创建座位
     * @param dto 座位请求DTO，包含座位的详细信息
     * @return 返回创建的座位响应DTO
     */
    @Operation(
            summary = "创建座位",
            description = "根据传入的座位信息创建新的座位",
            responses = {
                    @ApiResponse(responseCode = "200", description = "座位创建成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数无效")
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BaseResponse<SeatResponse> create(@Valid @RequestBody SeatRequest dto) {
        return BaseResponse.success(service.create(dto));
    }

    /**
     * 批量创建座位
     * @param dtos 座位请求DTO列表，包含多个座位的详细信息
     * @return 返回创建的座位响应DTO列表
     */
    @Operation(
            summary = "批量创建座位",
            description = "根据传入的座位信息列表批量创建座位",
            responses = {
                    @ApiResponse(responseCode = "200", description = "座位批量创建成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数无效")
            }
    )
    @PostMapping("/batch")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BaseResponse<List<SeatResponse>> createBatch(@Valid @RequestBody List<SeatRequest> dtos) {
        return BaseResponse.success(dtos.stream()
                .map(service::create)
                .collect(Collectors.toList()));
    }

    /**
     * 更新座位
     * @param id 座位ID
     * @param dto 座位请求DTO，包含更新的座位信息
     * @return 返回更新后的座位响应DTO
     */
    @Operation(
            summary = "更新座位",
            description = "根据座位ID和传入的座位信息更新座位的详细信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "座位更新成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数无效"),
                    @ApiResponse(responseCode = "404", description = "座位不存在")
            }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BaseResponse<SeatResponse> update(@PathVariable Long id, @Valid @RequestBody SeatRequest dto) {
        return BaseResponse.success(service.update(id, dto));
    }

    /**
     * 删除座位
     * @param id 座位ID
     * @return 返回成功信息
     */
    @Operation(
            summary = "删除座位",
            description = "根据座位ID删除座位",
            responses = {
                    @ApiResponse(responseCode = "200", description = "座位删除成功"),
                    @ApiResponse(responseCode = "404", description = "座位不存在")
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BaseResponse<Object> delete(@PathVariable Long id) {
        service.delete(id);
        return BaseResponse.success(null, "删除成功");
    }

    /**
     * 查询单个座位
     * @param id 座位ID
     * @return 返回座位响应DTO
     */
    @Operation(
            summary = "查询单个座位",
            description = "根据座位ID获取座位的详细信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "座位获取成功"),
                    @ApiResponse(responseCode = "404", description = "座位不存在")
            }
    )
    @GetMapping("/{id}")
    public BaseResponse<SeatResponse> getById(@PathVariable Long id) {
        return BaseResponse.success(service.getById(id));
    }

    /**
     * 根据车厢查询座位
     * @param carriageId 车厢ID
     * @return 返回该车厢的所有座位响应DTO列表
     */
    @Operation(
            summary = "根据车厢查询座位",
            description = "根据车厢ID获取该车厢的所有座位",
            responses = {
                    @ApiResponse(responseCode = "200", description = "座位列表获取成功"),
                    @ApiResponse(responseCode = "404", description = "车厢不存在")
            }
    )
    @GetMapping("/carriage/{carriageId}")
    public BaseResponse<List<SeatResponse>> getByCarriageId(@PathVariable Long carriageId) {
        return BaseResponse.success(service.getByCarriageId(carriageId));
    }
}
