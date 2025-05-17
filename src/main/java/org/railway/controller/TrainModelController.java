package org.railway.controller;

import jakarta.transaction.Transactional;
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
public class TrainModelController {

    private final TrainModelService service;

    /**
     * 创建新的车型
     * 接收一个 JSON 格式的车型请求对象，并通过服务层创建新车型。
     *
     * @param trainModel 包含车型信息的请求对象
     * @return 返回创建成功的车型响应对象
     * @throws SQLException 如果数据库操作失败，则抛出异常
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BaseResponse<TrainModelResponse> create(@RequestBody @Valid TrainModelRequest trainModel) throws SQLException {
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
    @PostMapping("/search")
    public BaseResponse<List<TrainModelResponse>> search(@RequestBody TrainModel criteria) {
        return BaseResponse.success(service.searchByCriteria(criteria));
    }
}
