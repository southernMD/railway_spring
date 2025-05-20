package org.railway.controller;

import jakarta.validation.Valid;
import org.railway.dto.request.PassengerRequest;
import org.railway.dto.response.BaseResponse;
import org.railway.dto.response.PassengerResponse;
import org.railway.service.PassengerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passengers")
public class PassengerController {

    private final PassengerService passengerService;

    public PassengerController(PassengerService service) {
        this.passengerService = service;
    }
    @PostMapping
    public BaseResponse<PassengerResponse> createPassenger(@Valid @RequestBody PassengerRequest passengerRequest) {
        PassengerResponse responseDTO = passengerService.createPassenger(passengerRequest);
        return BaseResponse.success(responseDTO);
    }

    @GetMapping("/{id}")
    public BaseResponse<PassengerResponse> getPassengerById(@PathVariable Long id) {
        PassengerResponse responseDTO = passengerService.getPassengerById(id);
        return BaseResponse.success(responseDTO);
    }

    @GetMapping
    public BaseResponse<List<PassengerResponse>> getAllPassengers() {
        List<PassengerResponse> responseDTOs = passengerService.getAllPassengers();
        return BaseResponse.success(responseDTOs);
    }

    @PutMapping("/{id}")
    public BaseResponse<PassengerResponse> updatePassenger(@Valid @PathVariable Long id,@Valid @RequestBody PassengerRequest passengerRequest) {
        PassengerResponse responseDTO = passengerService.updatePassenger(id, passengerRequest);
        return BaseResponse.success(responseDTO);
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Object> deletePassenger(@PathVariable Long id) {
        passengerService.deletePassenger(id);
        return BaseResponse.success(null,"删除成功");
    }

    /**
     * 根据用户ID查找所有乘客信息
     * @param userId 用户ID
     * @return 返回该用户的所有乘客响应DTO列表
     */
    @GetMapping("/user/{userId}")
    public BaseResponse<List<PassengerResponse>> getPassengersByUserId(@PathVariable Long userId) {
        List<PassengerResponse> responseDTOs = passengerService.getPassengersByUserId(userId);
        return BaseResponse.success(responseDTOs);
    }

}
