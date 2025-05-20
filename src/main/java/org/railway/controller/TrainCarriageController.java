package org.railway.controller;

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
public class TrainCarriageController {

    private final TrainCarriageService service;

    public TrainCarriageController(TrainCarriageService service) {
        this.service = service;
    }

    /**
     * 创建车厢
     * @param dto 请求数据
     * @return 创建后的车厢数据
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BaseResponse<TrainCarriageResponse> create(@Valid @RequestBody TrainCarriageRequest dto) {
        return BaseResponse.success(service.create(dto));
    }

    /**
     * 更新车厢
     * @param id 车厢ID
     * @param dto 新的数据
     * @return 更新后的车厢数据
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BaseResponse<TrainCarriageResponse> update(@PathVariable Long id, @Valid @RequestBody TrainCarriageRequest dto) {
        return BaseResponse.success(service.update(id, dto));
    }

    /**
     * 删除车厢
     *
     * @param id 车厢ID
     * @return
     */
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
     * @return 车厢数据
     */
    @GetMapping("/{id}")
    public BaseResponse<TrainCarriageResponse> getById(@PathVariable Long id) {
        return BaseResponse.success(service.getById(id));
    }

    /**
     * 根据车型查询车厢
     * @param modelId 车型ID
     * @return 车厢列表
     */
    @GetMapping("/model/{modelId}")
    public BaseResponse<List<TrainCarriageResponse>> getByModelId(@PathVariable Integer modelId) {
        return BaseResponse.success(service.getByModelId(modelId));
    }
}
