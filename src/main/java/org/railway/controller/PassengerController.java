package org.railway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.railway.annotation.CheckUserId;
import org.railway.dto.request.PassengerRequest;
import org.railway.dto.request.SetDefaultPassengerRequest;
import org.railway.dto.response.BaseResponse;
import org.railway.dto.response.PassengerResponse;
import org.railway.service.PassengerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 乘客控制器，提供乘客相关的API接口。
 * 包括创建乘客、获取乘客信息、更新乘客信息、删除乘客等功能。
 */
@RestController
@RequestMapping("/passengers")
@Tag(name = "乘客管理", description = "提供乘客的CRUD操作")
public class PassengerController {

    private final PassengerService passengerService;

    public PassengerController(PassengerService service) {
        this.passengerService = service;
    }

    /**
     * 创建乘客
     * @param passengerRequest 乘客请求DTO，包含乘客的详细信息
     * @return 返回创建的乘客响应DTO
     */
    @Operation(
            summary = "创建乘客",
            description = "根据传入的乘客信息创建新的乘客",
            responses = {
                    @ApiResponse(responseCode = "200", description = "乘客创建成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数无效")
            }
    )
    @PostMapping
    @CheckUserId
    public BaseResponse<PassengerResponse> createPassenger(
            @Parameter(description = "乘客请求信息", required = true)
            @Valid @RequestBody PassengerRequest passengerRequest) {
        PassengerResponse responseDTO = passengerService.createPassenger(passengerRequest);
        return BaseResponse.success(responseDTO);
    }

    /**
     * 根据ID获取乘客信息
     * @param id 乘客ID
     * @return 返回乘客响应DTO
     */
    @Operation(
            summary = "根据ID获取乘客信息",
            description = "根据乘客ID获取乘客的详细信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "乘客信息获取成功"),
                    @ApiResponse(responseCode = "404", description = "乘客不存在")
            }
    )
    @GetMapping("/{id}")
    @CheckUserId
    public BaseResponse<PassengerResponse> getPassengerById(
            @Parameter(description = "乘客ID", required = true)
            @PathVariable Long id) {
        PassengerResponse responseDTO = passengerService.getPassengerById(id);
        return BaseResponse.success(responseDTO);
    }

    /**
     * 获取所有乘客信息
     * @return 返回所有乘客的响应DTO列表
     */
    @Operation(
            summary = "获取所有乘客信息",
            description = "获取系统中所有乘客的详细信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "乘客列表获取成功")
            }
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public BaseResponse<List<PassengerResponse>> getAllPassengers() {
        List<PassengerResponse> responseDTOs = passengerService.getAllPassengers();
        return BaseResponse.success(responseDTOs);
    }

    /**
     * 更新乘客信息
     * @param id 乘客ID
     * @param passengerRequest 乘客请求DTO，包含更新的乘客信息
     * @return 返回更新后的乘客响应DTO
     */
    @Operation(
            summary = "更新乘客信息",
            description = "根据乘客ID和传入的乘客信息更新乘客的详细信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "乘客信息更新成功"),
                    @ApiResponse(responseCode = "400", description = "请求参数无效"),
                    @ApiResponse(responseCode = "404", description = "乘客不存在")
            }
    )
    @PutMapping("/{id}")
    @CheckUserId
    public BaseResponse<PassengerResponse> updatePassenger(
            @Parameter(description = "乘客ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "乘客请求信息", required = true)
            @Valid @RequestBody PassengerRequest passengerRequest) {
        PassengerResponse responseDTO = passengerService.updatePassenger(id, passengerRequest);
        return BaseResponse.success(responseDTO);
    }

    /**
     * 删除乘客
     * @param id 乘客ID
     * @return 返回删除操作的结果
     */
    @Operation(
            summary = "删除乘客",
            description = "根据乘客ID删除乘客",
            responses = {
                    @ApiResponse(responseCode = "200", description = "乘客删除成功"),
                    @ApiResponse(responseCode = "404", description = "乘客不存在")
            }
    )
    @DeleteMapping("/{id}")
    @CheckUserId
    public BaseResponse<Object> deletePassenger(
            @Parameter(description = "乘客ID", required = true)
            @PathVariable Long id) {
        passengerService.deletePassenger(id);
        return BaseResponse.success(null, "删除成功");
    }

    /**
     * 根据用户ID查找所有乘客信息
     * @param id 用户ID
     * @return 返回该用户的所有乘客响应DTO列表
     */
    @Operation(
            summary = "根据用户ID查找所有乘客信息",
            description = "根据用户ID获取该用户的所有乘客信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "乘客列表获取成功"),
                    @ApiResponse(responseCode = "404", description = "用户不存在")
            }
    )
    @CheckUserId
    @GetMapping("/user/{id}")
    public BaseResponse<List<PassengerResponse>> getPassengersByUserId(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long id) {
        List<PassengerResponse> responseDTOs = passengerService.getPassengersByUserId(id);
        return BaseResponse.success(responseDTOs);
    }
    /**
     * 将指定id乘客设置为默认乘客
     */
    @Operation(
            summary = "将指定id乘客设置为默认乘客",
            description = "将指定id乘客设置为默认乘客",
            responses = {
                    @ApiResponse(responseCode = "200", description = "乘客设置为默认乘客成功"),
                    @ApiResponse(responseCode = "404", description = "乘客不存在")
            }
    )
    @PutMapping("/default")
    public BaseResponse<Object> setDefaultPassenger(@Valid @RequestBody SetDefaultPassengerRequest setDefaultPassengerRequest) {
        passengerService.setDefaultPassenger(setDefaultPassengerRequest);
        return BaseResponse.success(null, "设置成功");
    }
}
