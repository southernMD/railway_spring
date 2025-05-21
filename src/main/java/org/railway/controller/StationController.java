package org.railway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.railway.dto.response.BaseResponse;
import org.railway.entity.Station;
import org.railway.service.StationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车站控制器，提供对车站的 CRUD 操作。
 * 包括创建车站、查询车站、更新车站、删除车站等功能。
 */
@RestController
@RequestMapping("/station")
@RequiredArgsConstructor
@Tag(name = "车站管理", description = "提供车站的CRUD操作")
public class StationController {

    private final StationService stationService;


    /**
     * 创建车站
     * @param station 车站实体对象，包含车站的详细信息
     * @return 返回创建的车站实体对象
     */
    @Operation(
            summary = "创建车站",
            description = "根据传入的车站信息创建新的车站",
            responses = {
                    @ApiResponse(responseCode = "200", description = "车站创建成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数无效")
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BaseResponse<Station> createStation(@Valid @RequestBody Station station) {
        return BaseResponse.success(stationService.createStation(station));
    }

    /**
     * 查询所有车站
     * @return 返回所有车站的列表
     */
    @Operation(
            summary = "查询所有车站",
            description = "获取系统中所有车站的详细信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "车站列表获取成功")
            }
    )
    @GetMapping
    public BaseResponse<List<Station>> getAllStations() {
        return BaseResponse.success(stationService.getAllStations());
    }

    /**
     * 根据ID查询车站
     * @param id 车站唯一标识
     * @return 返回对应的车站实体对象
     */
    @Operation(
            summary = "根据ID查询车站",
            description = "根据车站ID获取车站的详细信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "车站获取成功"),
                    @ApiResponse(responseCode = "404", description = "车站不存在")
            }
    )
    @GetMapping("/{id}")
    public BaseResponse<Station> getStationById(@PathVariable Integer id) {
        return stationService.getStationById(id)
                .map(BaseResponse::success)
                .orElseThrow(() -> new RuntimeException("Station not found"));
    }

    /**
     * 更新车站信息
     * @param station 车站实体对象，包含更新的车站信息
     * @return 返回更新后的车站实体对象
     */
    @Operation(
            summary = "更新车站信息",
            description = "根据传入的车站信息更新车站的详细信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "车站更新成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数无效"),
                    @ApiResponse(responseCode = "404", description = "车站不存在")
            }
    )
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BaseResponse<Station> updateStation(@Valid @RequestBody Station station) {
        return BaseResponse.success(stationService.updateStation(station));
    }

    /**
     * 删除车站
     * @param id 车站唯一标识
     * @return 返回成功信息
     */
    @Operation(
            summary = "删除车站",
            description = "根据车站ID删除车站",
            responses = {
                    @ApiResponse(responseCode = "200", description = "车站删除成功"),
                    @ApiResponse(responseCode = "404", description = "车站不存在")
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BaseResponse<Void> deleteStation(@PathVariable Integer id) {
        stationService.deleteStation(id);
        return BaseResponse.success(null);
    }

}
