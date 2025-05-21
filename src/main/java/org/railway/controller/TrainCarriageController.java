package org.railway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import org.railway.dto.request.TrainCarriageRequest;
import org.railway.dto.response.BaseResponse;
import org.railway.dto.response.TrainCarriageResponse;
import org.railway.service.TrainCarriageService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车厢 API 控制器
 */
@RestController
@RequestMapping("/carriages")
@Tag(name = "车厢管理", description = "提供车厢的CRUD操作")
public class TrainCarriageController {

    private final TrainCarriageService service;

    public TrainCarriageController(TrainCarriageService service) {
        this.service = service;
    }


    /**
     * 创建车厢
     * @param dto 车厢请求DTO，包含车厢的详细信息
     * @return 返回创建的车厢响应DTO
     */
    @Operation(
            summary = "创建车厢",
            description = "根据传入的车厢信息创建新的车厢",
            responses = {
                    @ApiResponse(responseCode = "200", description = "车厢创建成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数无效")
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BaseResponse<TrainCarriageResponse> create(@Valid @RequestBody TrainCarriageRequest dto) {
        return BaseResponse.success(service.create(dto));
    }

    /**
     * 更新车厢
     * @param id 车厢ID
     * @param dto 车厢请求DTO，包含更新的车厢信息
     * @return 返回更新后的车厢响应DTO
     */
    @Operation(
            summary = "更新车厢",
            description = "根据车厢ID和传入的车厢信息更新车厢的详细信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "车厢更新成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数无效"),
                    @ApiResponse(responseCode = "404", description = "车厢不存在")
            }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BaseResponse<TrainCarriageResponse> update(@PathVariable Long id, @Valid @RequestBody TrainCarriageRequest dto) {
        return BaseResponse.success(service.update(id, dto));
    }

    /**
     * 删除车厢
     * @param id 车厢ID
     * @return 返回成功信息
     */
    @Operation(
            summary = "删除车厢",
            description = "根据车厢ID删除车厢",
            responses = {
                    @ApiResponse(responseCode = "200", description = "车厢删除成功"),
                    @ApiResponse(responseCode = "404", description = "车厢不存在")
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
     * 查询单个车厢
     * @param id 车厢ID
     * @return 返回车厢响应DTO
     */
    @Operation(
            summary = "查询单个车厢",
            description = "根据车厢ID获取车厢的详细信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "车厢获取成功"),
                    @ApiResponse(responseCode = "404", description = "车厢不存在")
            }
    )
    @GetMapping("/{id}")
    public BaseResponse<TrainCarriageResponse> getById(@PathVariable Long id) {
        return BaseResponse.success(service.getById(id));
    }

    /**
     * 根据车型查询车厢
     * @param modelId 车型ID
     * @return 返回该车型的所有车厢响应DTO列表
     */
    @Operation(
            summary = "根据车型查询车厢",
            description = "根据车型ID获取该车型的所有车厢",
            responses = {
                    @ApiResponse(responseCode = "200", description = "车厢列表获取成功"),
                    @ApiResponse(responseCode = "404", description = "车型不存在")
            }
    )
    @GetMapping("/model/{modelId}")
    public BaseResponse<List<TrainCarriageResponse>> getByModelId(@PathVariable Integer modelId) {
        return BaseResponse.success(service.getByModelId(modelId));
    }
}
