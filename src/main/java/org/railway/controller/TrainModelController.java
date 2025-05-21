package org.railway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.railway.dto.request.TrainModelRequest;
import org.railway.dto.response.BaseResponse;
import org.railway.dto.response.TrainModelResponse;
import org.railway.entity.TrainModel;
import org.railway.service.TrainModelService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

/**
 * 车型管理控制器
 * <p>
 * 该控制器提供了对车型（TrainModel）的增删改查功能。
 * 所有请求路径以 `/train-models` 开头。
 */
@RestController
@RequestMapping("/train-models")
@RequiredArgsConstructor
@Tag(name = "车型管理", description = "提供车型的CRUD操作")
public class TrainModelController {

    private final TrainModelService service;

    /**
     * 创建新的车型
     * @param trainModel 车型请求DTO，包含车型的详细信息
     * @return 返回创建的车型响应DTO
     * @throws SQLException 如果数据库操作失败
     */
    @Operation(
            summary = "创建车型",
            description = "根据传入的车型信息创建新的车型",
            responses = {
                    @ApiResponse(responseCode = "200", description = "车型创建成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数无效"),
                    @ApiResponse(responseCode = "500", description = "数据库操作失败")
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BaseResponse<TrainModelResponse> create(@Valid @RequestBody TrainModelRequest trainModel) throws SQLException {
        return BaseResponse.success(service.create(trainModel));
    }

    /**
     * 更新指定 ID 的车型信息
     * 根据路径参数中的车型 ID 和请求体中的更新数据，更新对应车型信息。
     *
     * @param id         车型的唯一标识符
     * @param trainModel 包含更新信息的请求对象
     * @return 返回更新后的车型响应对象
     * @throws SQLException 如果数据库操作失败，则抛出异常
     */
    @Operation(
            summary = "更新车型信息",
            description = "根据车型ID和传入的车型信息更新车型的详细信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "车型更新成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数无效"),
                    @ApiResponse(responseCode = "404", description = "车型不存在"),
                    @ApiResponse(responseCode = "500", description = "数据库操作失败")
            }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BaseResponse<TrainModelResponse> update(
            @PathVariable Integer id,
            @RequestBody @Valid TrainModelRequest trainModel) throws SQLException {
        return BaseResponse.success(service.update(id, trainModel));
    }

    /**
     * 根据 ID 获取车型信息
     * 根据路径参数中的车型 ID 查询对应的车型信息。
     *
     * @param id 车型的唯一标识符
     * @return 返回查询到的车型响应对象
     * @throws SQLException 如果数据库操作失败，则抛出异常
     */
    @Operation(
            summary = "根据ID查询车型信息",
            description = "根据车型ID获取车型的详细信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "车型获取成功"),
                    @ApiResponse(responseCode = "404", description = "车型不存在"),
                    @ApiResponse(responseCode = "500", description = "数据库操作失败")
            }
    )
    @GetMapping("/{id}")
    public BaseResponse<TrainModelResponse> getById(@PathVariable Integer id) throws SQLException {
        return BaseResponse.success(service.getById(id));
    }

    /**
     * 获取所有车型信息
     * 查询并返回系统中所有的车型信息列表。
     *
     * @return 返回包含所有车型信息的响应对象列表
     */
    @Operation(
            summary = "查询所有车型信息",
            description = "获取系统中所有车型的详细信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "车型列表获取成功")
            }
    )
    @GetMapping
    public BaseResponse<List<TrainModelResponse>> getAll() {
        return BaseResponse.success(service.getAll());
    }

    /**
     * 删除指定 ID 的车型信息
     * 根据路径参数中的车型 ID 删除对应的车型信息。
     *
     * @param id 车型的唯一标识符
     * @return 返回 HTTP 200 表示删除成功
     * @throws SQLException 如果数据库操作失败，则抛出异常
     */
    @Operation(
            summary = "删除车型信息",
            description = "根据车型ID删除车型信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "车型删除成功"),
                    @ApiResponse(responseCode = "404", description = "车型不存在"),
                    @ApiResponse(responseCode = "500", description = "数据库操作失败")
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BaseResponse<Object> delete(@PathVariable Integer id) throws SQLException {
        service.delete(id);
        return BaseResponse.success(null, "删除成功");
    }

    /**
     * 更具筛选结果进行查询
     *
     * @param criteria 为实体字段的任意值
     * @return 返回 查询结果
     */
    @Operation(
            summary = "根据条件查询车型信息",
            description = "根据传入的筛选条件查询车型信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "车型查询成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数无效")
            }
    )
    @PostMapping("/search")
    public BaseResponse<List<TrainModelResponse>> search(@Valid @RequestBody TrainModel criteria) {
        return BaseResponse.success(service.searchByCriteria(criteria));
    }
}
