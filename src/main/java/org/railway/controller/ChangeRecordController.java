package org.railway.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.railway.annotation.CheckUserId;
import org.railway.dto.Views;
import org.railway.dto.request.ChangeRecordRequest;
import org.railway.dto.response.BaseResponse;
import org.railway.dto.response.ChangeRecordResponse;
import org.railway.service.ChangeRecordService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;


/**
 * 改签控制器，提供改签记录的CRUD操作。
 * 包括创建改签记录、获取改签信息、更新改签状态、删除改签记录等功能。
 */
@RestController
@RequestMapping("/change-records")
@Tag(name = "改签管理", description = "提供改签记录的CRUD操作")
public class ChangeRecordController {

    private final ChangeRecordService changeRecordService;

    public ChangeRecordController(ChangeRecordService changeRecordService) {
        this.changeRecordService = changeRecordService;
    }

    /**
     * 创建改签记录
     * @param request 改签请求DTO，包含改签的详细信息
     * @return 返回创建的改签响应DTO
     */
    @Operation(
            summary = "创建改签记录",
            description = "根据传入的改签信息创建新的改签记录",
            responses = {
                    @ApiResponse(responseCode = "200", description = "改签记录创建成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数无效"),
                    @ApiResponse(responseCode = "404", description = "原票、新票或订单不存在")
            }
    )
    @CheckUserId
    @PostMapping
    public BaseResponse<ChangeRecordResponse> createChangeRecord(@RequestBody ChangeRecordRequest request) throws SQLException {
        ChangeRecordResponse responseDTO = changeRecordService.createChangeRecord(request);
        return BaseResponse.success(responseDTO);
    }

    /**
     * 根据ID获取改签记录
     * @param id 改签记录ID
     * @return 返回改签响应DTO
     */
    @Operation(
            summary = "根据ID获取改签记录",
            description = "根据改签记录ID获取改签的详细信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "改签记录获取成功"),
                    @ApiResponse(responseCode = "404", description = "改签记录不存在")
            }
    )
    @CheckUserId
    @GetMapping("/{id}")
    public BaseResponse<ChangeRecordResponse> getChangeRecordById(@PathVariable Long id) {
        ChangeRecordResponse responseDTO = changeRecordService.getChangeRecordById(id);
        return BaseResponse.success(responseDTO);
    }


    /**
     * 获取所有改签记录
     * @return 返回所有改签记录的响应DTO列表
     */
    @Operation(
            summary = "获取所有改签记录",
            description = "获取系统中所有改签记录的详细信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "改签记录列表获取成功")
            }
    )
    @CheckUserId
    @GetMapping
    @JsonView(Views.Basic.class)
    public BaseResponse<List<ChangeRecordResponse>> getAllChangeRecords() {
        List<ChangeRecordResponse> responseDTOs = changeRecordService.getAllChangeRecords();
        return BaseResponse.success(responseDTOs);
    }

    /**
     * 删除改签记录
     * @param id 改签记录ID
     * @return 返回成功信息
     */
    @Operation(
            summary = "删除改签记录",
            description = "根据改签记录ID删除改签记录",
            responses = {
                    @ApiResponse(responseCode = "200", description = "改签记录删除成功"),
                    @ApiResponse(responseCode = "404", description = "改签记录不存在")
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @CheckUserId
    public BaseResponse<Object> deleteChangeRecord(@PathVariable Long id) {
        changeRecordService.deleteChangeRecord(id);
        return BaseResponse.success(null, "删除成功");
    }

    /**
     * 根据原票ID查询改签记录
     * @param ticketId 原票ID
     * @return 返回该原票的所有改签记录响应DTO列表
     */
    @Operation(
            summary = "根据原票ID查询改签记录",
            description = "根据原票ID获取该原票的所有改签记录",
            responses = {
                    @ApiResponse(responseCode = "200", description = "改签记录列表获取成功"),
                    @ApiResponse(responseCode = "404", description = "原票不存在")
            }
    )
    @GetMapping("/ticket/{ticketId}")
    @CheckUserId
    public BaseResponse<List<ChangeRecordResponse>> getChangeRecordsByTicketId(@PathVariable Long ticketId) {
        List<ChangeRecordResponse> responseDTOs = changeRecordService.getChangeRecordsByTicketId(ticketId);
        return BaseResponse.success(responseDTOs);
    }

    /**
     * 根据ID和状态更新改签记录的状态
     * @param id 改签记录ID
     * @param status 新的状态值
     * @return 返回更新后的改签响应DTO
     */
    @Operation(
            summary = "更新改签记录状态",
            description = "根据改签记录ID和状态值更新改签记录的状态",
            responses = {
                    @ApiResponse(responseCode = "200", description = "改签记录状态更新成功"),
                    @ApiResponse(responseCode = "404", description = "改签记录不存在")
            }
    )
    @CheckUserId
    @PutMapping("/status/{id}")
    public BaseResponse<ChangeRecordResponse> updateChangeRecordStatus(@PathVariable Long id, @RequestParam Integer status,@RequestParam Long userId) {
        ChangeRecordResponse responseDTO = changeRecordService.updateChangeRecordStatus(id, status,userId);
        return BaseResponse.success(responseDTO);
    }

}
