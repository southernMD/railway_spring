package org.railway.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.railway.dto.Views;
import org.railway.dto.request.TrainRequest;
import org.railway.dto.response.BaseResponse;
import org.railway.dto.response.TrainResponse;
import org.railway.service.TrainService;
import org.railway.dto.request.TrainUpdateRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * 列车信息控制器
 * 提供对 trains 表的 RESTful API 接口
 */
@RestController
@RequestMapping("/trains")
@RequiredArgsConstructor
@Tag(name = "列车管理", description = "提供列车的CRUD操作")
public class TrainController {

    private final TrainService trainService;


    /**
     * 查询所有列车信息
     * @return 返回所有列车的响应数据
     */
    @Operation(
            summary = "查询所有列车信息",
            description = "获取系统中所有列车的详细信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "列车列表获取成功")
            }
    )
    @GetMapping
    @JsonView(Views.Basic.class)
    public BaseResponse<List<TrainResponse>> getAll() {
        List<TrainResponse> trains = trainService.getAll();
        return BaseResponse.success(trains);
    }

    /**
     * 根据 ID 查询列车信息
     * @param id 列车唯一标识
     * @return 返回对应的列车响应数据
     * @throws SQLException 如果未找到对应记录
     */
    @Operation(
            summary = "根据ID查询列车信息",
            description = "根据列车ID获取列车的详细信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "列车获取成功"),
                    @ApiResponse(responseCode = "404", description = "列车不存在")
            }
    )
    @GetMapping("/{id}")
    @JsonView(Views.Basic.class)
    public  BaseResponse <TrainResponse> getById(@PathVariable Long id) throws SQLException {
        TrainResponse train = trainService.getById(id);
        return  BaseResponse.success(train);
    }

    /**
     * 创建一个新的列车记录
     * @param request 列车请求DTO，包含列车的详细信息
     * @return 返回创建的列车响应数据
     * @throws SQLException 如果车次编号重复或时间冲突
     */
    @Operation(
            summary = "创建列车记录",
            description = "根据传入的列车信息创建新的列车记录",
            responses = {
                    @ApiResponse(responseCode = "200", description = "列车创建成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数无效"),
                    @ApiResponse(responseCode = "409", description = "车次编号重复或时间冲突")
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public  BaseResponse<TrainResponse> create(@Valid @RequestBody TrainRequest request) throws SQLException {
        TrainResponse train = trainService.create(request);
        return  BaseResponse.success(train);
    }

    /**
     * 更新指定 ID 的列车记录
     * @param id 列车唯一标识
     * @param request 列车请求DTO，包含更新的列车信息
     * @return 返回更新后的列车响应数据
     * @throws SQLException 如果未找到记录或时间冲突
     */
    @Operation(
            summary = "更新列车记录",
            description = "根据列车ID和传入的列车信息更新列车的详细信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "列车更新成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数无效"),
                    @ApiResponse(responseCode = "404", description = "列车不存在"),
                    @ApiResponse(responseCode = "409", description = "时间冲突")
            }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public BaseResponse<TrainResponse> update(@PathVariable Long id, @Valid @RequestBody TrainUpdateRequest request) throws SQLException {
        TrainResponse train = trainService.update(id, request);
        return BaseResponse.success(train);
    }

    /**
     * 删除指定 ID 的列车记录
     * @param id 列车唯一标识
     * @return 返回成功信息
     * @throws SQLException 如果未找到记录
     */
    @Operation(
            summary = "删除列车记录",
            description = "根据列车ID删除列车记录",
            responses = {
                    @ApiResponse(responseCode = "200", description = "列车删除成功"),
                    @ApiResponse(responseCode = "404", description = "列车不存在")
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public BaseResponse<Object> deleteById(@PathVariable Long id) throws SQLException {
        trainService.deleteById(id);
        return BaseResponse.success(null, "删除成功");
    }

    /**
     * 根据车站和日期查询符合条件的列车
     * @param startStationId 起始站ID
     * @param endStationId 终点站ID
     * @param date 查询日期
     * @return 符合条件的列车列表
     */
    @Operation(
            summary = "查询两站间的列车",
            description = "根据起始站、终点站和日期查询符合条件的列车",
            responses = {
                    @ApiResponse(responseCode = "200", description = "查询成功"),
                    @ApiResponse(responseCode = "400", description = "参数无效")
            }
    )
    @GetMapping("/route")
    @JsonView(Views.Basic.class)
    public BaseResponse<List<TrainResponse>> getTrainsByRoute(
            @RequestParam Long startStationId,
            @RequestParam Long endStationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<TrainResponse> trains = trainService.getTrainsByRoute(startStationId, endStationId, date);
        return BaseResponse.success(trains);
    }

}
