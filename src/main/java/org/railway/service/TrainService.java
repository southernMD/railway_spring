package org.railway.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.railway.dto.request.TrainRequest;
import org.railway.dto.request.TrainUpdateRequest;
import org.railway.dto.response.TrainResponse;
import org.railway.entity.*;
import org.railway.service.impl.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 列车业务逻辑服务类
 * 提供对 trains 表的创建、更新、删除和查询操作
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TrainService {

    private final TrainRepository repository;
    private final TrainSeatRepository trainSeatRepository;
    private final TrainModelRepository trainModelRepository; // 新增
    private final StationViewRepository stationViewRepository; // 新增
    private final StationRepository stationRepository; // 新增
    /**
     * 查询所有列车信息
     *
     * @return 所有列车的 Dto 列表
     */
    public List<TrainResponse> getAll() {
        return repository.findAll().stream()
                .map(train -> {
                    TrainResponse response = convertToResponse(train);
                    response.setTotalSeats(calculateTotalSeats(train));
                    return response;
                })
                .collect(Collectors.toList());
    }

    private Integer calculateTotalSeats(Train train) {
        return train.getModel().getCarriages().stream()
                .mapToInt(carriage -> carriage.getSeats().size())
                .sum();
    }


    /**
     * 根据 ID 查询列车信息
     *
     * @param id 列车唯一标识
     * @return 对应的 Dto 数据
     * @throws EntityNotFoundException 如果未找到对应记录
     */
    public TrainResponse getById(@NotNull Long id) throws SQLException {
        return repository.findById(id)
                .map(this::convertToResponse)
                .orElseThrow(() -> new SQLException("车型不存在"));
    }

    /**
     * 创建一个新的列车记录
     *
     * @param dto 包含列车信息的请求数据
     * @return 创建后的响应数据
     */
    @Transactional
    public TrainResponse create(@Valid TrainRequest dto) throws SQLException {
        if(dto.getDepartureTime().isAfter(dto.getArrivalTime())){
            throw new SQLException("出发时间不能早于到达时间");
        }
        TrainModel model = trainModelRepository.findById(Math.toIntExact(dto.getModelId()))
                .orElseThrow(() -> new EntityNotFoundException("车型未找到"));
        if(model.getStatus() == 0) throw new EntityNotFoundException("该车型已停用");
        List<Train> existingList = repository.findByModelIdAndDate(Math.toIntExact(dto.getModelId()), dto.getDate());

        for (Train existing : existingList) {
            boolean hasOverlap = !dto.getArrivalTime().isBefore(existing.getDepartureTime()) &&
                    !dto.getDepartureTime().isAfter(existing.getArrivalTime());

            if (hasOverlap) {
                throw new SQLException("与现有班次时间冲突，请调整发车/到达时间");
            }
        }

        Train train = new Train();
        BeanUtils.copyProperties(dto, train);
        train.setModel(model);

        Station startStation = stationRepository.findByIdAndStatus(Math.toIntExact(dto.getStartStationId()),1)
                .orElseThrow(() -> new EntityNotFoundException("起始站未找到或不可用"));
        train.setStartStation(startStation);

        Station endStation = stationRepository.findByIdAndStatus(Math.toIntExact(dto.getEndStationId()),1)
                .orElseThrow(() -> new EntityNotFoundException("终点站未找到或不可用"));
        train.setEndStation(endStation);
        TrainSeat seatInfo = new TrainSeat();
        BeanUtils.copyProperties(dto.getTrainSeatInfo(), seatInfo);
        TrainSeat savedTrainSeat = trainSeatRepository.save(seatInfo);
        train.setTrainSeatInfo(savedTrainSeat);
        Train savedTrain = repository.save(train);

//        TrainSeat trainSeat = new TrainSeat();
//        BeanUtils.copyProperties(dto.getTrainSeatInfo(), trainSeat);
//        trainSeatRepository.save(trainSeat);
//
//        savedTrain.setTrainSeatInfo(trainSeat);

        return convertToResponse(savedTrain);
    }

    /**
     * 更新指定 ID 的列车记录
     *
     * @param id  列车唯一标识
     * @param dto 包含新数据的请求对象
     * @return 更新后的列车信息
     * @throws EntityNotFoundException 如果未找到对应记录
     */
    public TrainResponse update(Long id, @Valid TrainUpdateRequest dto) throws SQLException {
        if(dto.getDepartureTime().isAfter(dto.getArrivalTime())){
            throw new SQLException("出发时间不能早于到达时间");
        }
        Train entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("列车未找到"));


        //TODO 只有当 modelId 或 date 或时间发生变化时才校验

        List<Train> existingList = repository.findByModelIdAndDate(Math.toIntExact(dto.getModelId()), dto.getDate());

        for (Train existing : existingList) {
            // 排除自己
            if (existing.getId().equals(dto.getId())) continue;

            boolean hasOverlap = !dto.getArrivalTime().isBefore(existing.getDepartureTime()) &&
                    !dto.getDepartureTime().isAfter(existing.getArrivalTime());

            if (hasOverlap) {
                throw new SQLException("与现有班次时间冲突，请调整发车/到达时间");
            }
        }


        // 更新 Train 实体的基本信息
        BeanUtils.copyProperties(dto, entity);

        // 更新关联的 TrainModel
        if (entity.getModel().getId().longValue() != dto.getModelId()) {
            TrainModel model = trainModelRepository.findById(Math.toIntExact(dto.getModelId()))
                    .orElseThrow(() -> new EntityNotFoundException("车型未找到"));
            entity.setModel(model);
        }

        // 更新关联的起始站
        if (entity.getStartStation().getId().longValue() != dto.getStartStationId()) {
            Station startStation = stationRepository.findByIdAndStatus(Math.toIntExact(dto.getStartStationId()),1)
                    .orElseThrow(() -> new EntityNotFoundException("起始站未找到或不可用"));
            entity.setStartStation(startStation);
        }
        // 更新关联的终点站
        if (entity.getEndStation().getId().longValue() != dto.getEndStationId()) {
            Station endStation = stationRepository.findByIdAndStatus(Math.toIntExact(dto.getEndStationId()),1)
                    .orElseThrow(() -> new EntityNotFoundException("终点站未找到或不可用"));
            entity.setEndStation(endStation);
        }

        TrainSeat seatInfo = new TrainSeat();
        BeanUtils.copyProperties(dto.getTrainSeatInfo(), seatInfo);
        TrainSeat savedTrainSeat = trainSeatRepository.save(seatInfo);
        entity.setTrainSeatInfo(savedTrainSeat);
        // 保存更新后的 Train 实体
        Train savedTrain = repository.save(entity);

        // 返回更新后的响应
        return convertToResponse(savedTrain);
    }

    /**
     * 删除指定 ID 的列车记录
     *
     * @param id 列车唯一标识
     */
    public void deleteById(@NotNull Long id) throws SQLException {
        if (!repository.existsById(id)) {
            throw new SQLException("列车记录不存在");
        }
        repository.deleteById(id);
    }

    /**
     * 将 Train 实体转换为 TrainResponse
     */
    private TrainResponse convertToResponse(Train entity) {
        TrainResponse response = new TrainResponse();
        BeanUtils.copyProperties(entity, response); // Entity -> Dto
        return response;
    }
    /*
    * 更具选择区间查询列车
    * */
    public List<TrainResponse> getTrainsByRoute(Long startStationId, Long endStationId, LocalDate date) {
        List<Train> trains = repository.findTrainsByRoute(startStationId, endStationId, date);
        return trains.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

}
