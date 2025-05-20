package org.railway.service;

import lombok.RequiredArgsConstructor;
import org.railway.dto.request.PassengerRequest;
import org.railway.dto.response.PassengerResponse;
import org.railway.entity.Passenger;
import org.railway.entity.User;
import org.railway.service.impl.PassengerRepository;
import org.railway.service.impl.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 乘客服务类，提供乘客的CRUD操作。
 * 包括创建乘客、获取乘客信息、更新乘客信息、删除乘客等功能。
 */
@Service
@RequiredArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final UserRepository userRepository;

    /**
     * 创建乘客
     * @param PassengerRequest 乘客请求DTO，包含乘客的详细信息
     * @return 返回创建的乘客响应DTO
     * @throws RuntimeException 如果用户不存在，则抛出异常
     */
    public PassengerResponse createPassenger(PassengerRequest PassengerRequest) {
        Passenger passenger = new Passenger();
        BeanUtils.copyProperties(PassengerRequest, passenger);
        User user = userRepository.findById(PassengerRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        passenger.setUser(user);
        passenger = passengerRepository.save(passenger);
        return convertToResponseDTO(passenger);
    }

    /**
     * 根据ID获取乘客信息
     * @param id 乘客ID
     * @return 返回乘客响应DTO
     * @throws RuntimeException 如果乘客不存在，则抛出异常
     */
    public PassengerResponse getPassengerById(Long id) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("乘客不存在"));
        return convertToResponseDTO(passenger);
    }

    /**
     * 获取所有乘客信息
     * @return 返回所有乘客的响应DTO列表
     */
    public List<PassengerResponse> getAllPassengers() {
        return passengerRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * 更新乘客信息
     * @param id 乘客ID
     * @param PassengerRequest 乘客请求DTO，包含更新的乘客信息
     * @return 返回更新后的乘客响应DTO
     * @throws RuntimeException 如果乘客或用户不存在，则抛出异常
     */
    public PassengerResponse updatePassenger(Long id, PassengerRequest PassengerRequest) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("乘客不存在"));
        BeanUtils.copyProperties(PassengerRequest, passenger);
        User user = userRepository.findById(PassengerRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        passenger.setUser(user);
        passenger = passengerRepository.save(passenger);
        return convertToResponseDTO(passenger);
    }

    /**
     * 删除乘客
     * @param id 乘客ID
     */
    public void deletePassenger(Long id) {
        passengerRepository.deleteById(id);
    }

    /**
     * 根据用户ID查找所有乘客信息
     * @param userId 用户ID
     * @return 返回该用户的所有乘客响应DTO列表
     * @throws RuntimeException 如果用户不存在，则抛出异常
     */
    public List<PassengerResponse> getPassengersByUserId(Long userId) {
        // 根据userId查找所有乘客
        List<Passenger> passengers = passengerRepository.findByUserId(userId);

        // 将Passenger实体转换为PassengerResponse
        return passengers.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }


    /**
     * 将Passenger实体转换为PassengerResponse
     * @param passenger 乘客实体
     * @return 返回乘客响应DTO
     */
    private PassengerResponse convertToResponseDTO(Passenger passenger) {
        PassengerResponse responseDTO = new PassengerResponse();
        BeanUtils.copyProperties(passenger, responseDTO);
        responseDTO.setUserId(passenger.getUser().getId());
        return responseDTO;
    }
}
