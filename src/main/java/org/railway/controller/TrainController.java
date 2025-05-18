package org.railway.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.railway.dto.Views;
import org.railway.dto.request.TrainRequest;
import org.railway.dto.response.BaseResponse;
import org.railway.dto.response.TrainResponse;
import org.railway.service.TrainService;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

/**
 * 列车信息控制器
 * 提供对 trains 表的 RESTful API 接口
 */
@RestController
@RequestMapping("/trains")
@RequiredArgsConstructor
public class TrainController {

    private final TrainService trainService;

    /**
     * 查询所有列车信息
     *
     * @return 所有列车的响应数据
     */
    @GetMapping
    @JsonView(Views.Basic.class)
    public BaseResponse<List<TrainResponse>> getAll() {
        List<TrainResponse> trains = trainService.getAll();
        return BaseResponse.success(trains);
    }

    /**
     * 根据 ID 查询列车信息
     *
     * @param id 列车唯一标识
     * @return 对应的列车响应数据
     * @throws SQLException 如果未找到对应记录
     */
    @GetMapping("/{id}")
    @JsonView(Views.Basic.class)
    public  BaseResponse <TrainResponse> getById(@PathVariable Long id) throws SQLException {
        TrainResponse train = trainService.getById(id);
        return  BaseResponse.success(train);
    }

    /**
     * 创建一个新的列车记录
     *
     * @param request 包含列车信息的请求数据
     * @return 创建后的列车响应数据
     * @throws SQLException 如果车次编号重复或时间冲突
     */
    @PostMapping
    public  BaseResponse<TrainResponse> create(@Valid @RequestBody TrainRequest request) throws SQLException {
        TrainResponse train = trainService.create(request);
        return  BaseResponse.success(train);
    }

    /**
     * 更新指定 ID 的列车记录
     *
     * @param id      列车唯一标识
     * @param request 包含新数据的请求对象
     * @return 更新后的列车响应数据
     * @throws SQLException 如果未找到记录或时间冲突
     */
    @PutMapping("/{id}")
    public BaseResponse<TrainResponse> update(@PathVariable Long id, @Valid @RequestBody TrainRequest request) throws SQLException {
        TrainResponse train = trainService.update(id, request);
        return BaseResponse.success(train);
    }

    /**
     * 删除指定 ID 的列车记录
     *
     * @param id 列车唯一标识
     * @throws SQLException 如果未找到记录
     */
    @DeleteMapping("/{id}")
    public BaseResponse<Object> deleteById(@PathVariable Long id) throws SQLException {
        trainService.deleteById(id);
        return BaseResponse.success(null, "删除成功");
    }
}
