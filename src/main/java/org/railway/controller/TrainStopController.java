package org.railway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.railway.dto.request.TrainStopBatchUpdateRequest;
import org.railway.dto.request.TrainStopRequest;
import org.railway.dto.response.BaseResponse;
import org.railway.dto.response.TrainStopResponse;
import org.railway.service.TrainStopService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 列车停靠站管理控制器
 * <p>
 * 该控制器提供了对列车停靠站（TrainStop）的增删改查功能。
 * 所有请求路径以 `/api/train-stops` 开头。
 */
@RestController
@RequestMapping("/train-stops")
@RequiredArgsConstructor
@Tag(name = "列车停靠站管理", description = "提供列车停靠站的CRUD操作")
public class TrainStopController {

    private final TrainStopService trainStopService;

    /**
     * 创建新的列车停靠站
     * 接收一个 JSON 格式的列车停靠站请求对象，并通过服务层创建新停靠站。
     *
     * @param request 包含列车停靠站信息的请求对象
     * @return 返回创建成功的列车停靠站响应对象
     */
    @Operation(
            summary = "创建列车停靠站",
            description = "根据传入的列车停靠站信息创建新的停靠站",
            responses = {
                    @ApiResponse(responseCode = "201", description = "停靠站创建成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数无效")
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseResponse<TrainStopResponse> create(@Valid @RequestBody TrainStopRequest request) {
        return BaseResponse.success(trainStopService.create(request));
    }

    /**
     * 根据 ID 查询列车停靠站信息
     * 根据路径参数中的停靠站 ID 查询对应的停靠站信息。
     *
     * @param id 列车停靠站的唯一标识符
     * @return 返回查询到的列车停靠站响应对象
     */
    @Operation(
            summary = "根据ID查询列车停靠站信息",
            description = "根据列车停靠站ID获取停靠站的详细信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "停靠站获取成功"),
                    @ApiResponse(responseCode = "404", description = "停靠站不存在")
            }
    )
    @GetMapping("/{id}")
    public BaseResponse<TrainStopResponse> getById(@PathVariable Long id) {
        return BaseResponse.success(trainStopService.getById(id));
    }

    /**
     * 查询所有列车停靠站信息
     * 查询并返回系统中所有的列车停靠站信息列表。
     *
     * @return 返回包含所有列车停靠站信息的响应对象列表
     */
    @GetMapping
    @Operation(
            summary = "查询所有列车停靠站信息",
            description = "获取系统中所有列车停靠站的详细信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "停靠站列表获取成功")
            }
    )
    public BaseResponse<List<TrainStopResponse>> getAll() {
        return BaseResponse.success(trainStopService.getAll());
    }

    /**
     * 更新指定 ID 的列车停靠站信息
     * 根据路径参数中的停靠站 ID 和请求体中的更新数据，更新对应停靠站信息。
     *
     * @param id      列车停靠站的唯一标识符
     * @param request 包含更新信息的请求对象
     * @return 返回更新后的列车停靠站响应对象
     */
    @Operation(
            summary = "更新列车停靠站信息",
            description = "根据列车停靠站ID和传入的停靠站信息更新停靠站的详细信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "停靠站更新成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数无效"),
                    @ApiResponse(responseCode = "404", description = "停靠站不存在")
            }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public BaseResponse<TrainStopResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TrainStopRequest request) {
        return BaseResponse.success(trainStopService.update(id, request));
    }

    /**
     * 删除指定 ID 的列车停靠站信息
     * 根据路径参数中的停靠站 ID 删除对应的停靠站信息。
     *
     * @param id 列车停靠站的唯一标识符
     * @return 返回 HTTP 200 表示删除成功
     */
    @Operation(
            summary = "删除列车停靠站信息",
            description = "根据列车停靠站ID删除停靠站信息",
            responses = {
                    @ApiResponse(responseCode = "204", description = "停靠站删除成功"),
                    @ApiResponse(responseCode = "404", description = "停靠站不存在")
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public BaseResponse<Object> delete(@PathVariable Long id) {
        trainStopService.delete(id);
        return BaseResponse.success(null, "删除成功");
    }

    /**
     * 批量更新列车停靠站信息
     * 接收一个 JSON 格式的批量更新请求对象，更新多个停靠站信息。
     *
     * @param request 包含批量更新信息的请求对象
     * @return 返回更新后的列车停靠站响应对象列表
     */
    @Operation(
            summary = "批量更新列车停靠站信息",
            description = "根据传入的批量更新请求对象，更新多个列车停靠站信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "批量更新成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数无效"),
                    @ApiResponse(responseCode = "404", description = "部分停靠站不存在")
            }
    )
    @PutMapping("/batch-update")
    @PreAuthorize("hasRole('ADMIN')")
    public BaseResponse<List<TrainStopResponse>> batchUpdate(@Valid @RequestBody TrainStopBatchUpdateRequest request) {
        return BaseResponse.success(trainStopService.batchUpdate(request));
    }
}
