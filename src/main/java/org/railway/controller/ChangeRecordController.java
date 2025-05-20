package org.railway.controller;

import org.railway.dto.request.ChangeRecordRequest;
import org.railway.dto.response.BaseResponse;
import org.railway.dto.response.ChangeRecordResponse;
import org.railway.service.ChangeRecordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 改签控制器，提供改签的CRUD接口。
 */
@RestController
@RequestMapping("/change-records")
public class ChangeRecordController {

    private final ChangeRecordService changeRecordService;

    public ChangeRecordController(ChangeRecordService changeRecordService) {
        this.changeRecordService = changeRecordService;
    }

    /**
     * 创建改签记录
     * @param request 改签请求DTO
     * @return 返回创建的改签响应DTO
     */
    @PostMapping
    public BaseResponse<ChangeRecordResponse> createChangeRecord(@RequestBody ChangeRecordRequest request) {
        ChangeRecordResponse responseDTO = changeRecordService.createChangeRecord(request);
        return BaseResponse.success(responseDTO);
    }

    /**
     * 根据ID获取改签记录
     * @param id 改签记录ID
     * @return 返回改签响应DTO
     */
    @GetMapping("/{id}")
    public BaseResponse<ChangeRecordResponse> getChangeRecordById(@PathVariable Long id) {
        ChangeRecordResponse responseDTO = changeRecordService.getChangeRecordById(id);
        return BaseResponse.success(responseDTO);
    }

    /**
     * 获取所有改签记录
     * @return 返回所有改签记录的响应DTO列表
     */
    @GetMapping
    public BaseResponse<List<ChangeRecordResponse>> getAllChangeRecords() {
        List<ChangeRecordResponse> responseDTOs = changeRecordService.getAllChangeRecords();
        return BaseResponse.success(responseDTOs);
    }

    /**
     * 删除改签记录
     * @param id 改签记录ID
     * @return 返回成功信息
     */
    @DeleteMapping("/{id}")
    public BaseResponse<Object> deleteChangeRecord(@PathVariable Long id) {
        changeRecordService.deleteChangeRecord(id);
        return BaseResponse.success(null, "删除成功");
    }

    /**
     * 根据原票ID查询改签记录
     * @param ticketId 原票ID
     * @return 返回该原票的所有改签记录响应DTO列表
     */
    @GetMapping("/ticket/{ticketId}")
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
    @PutMapping("/status/{id}")
    public BaseResponse<ChangeRecordResponse> updateChangeRecordStatus(@PathVariable Long id, @RequestParam Integer status) {
        ChangeRecordResponse responseDTO = changeRecordService.updateChangeRecordStatus(id, status);
        return BaseResponse.success(responseDTO);
    }

}
