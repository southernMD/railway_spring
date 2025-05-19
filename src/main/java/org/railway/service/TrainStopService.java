package org.railway.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.railway.dto.request.TrainStopBatchUpdateRequest;
import org.railway.dto.request.TrainStopRequest;
import org.railway.dto.response.TrainStopResponse;
import org.railway.entity.StationView;
import org.railway.entity.Train;
import org.railway.entity.TrainStop;
import org.railway.service.impl.StationViewRepository;
import org.railway.service.impl.TrainRepository;
import org.railway.service.impl.TrainStopRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainStopService {

    private final TrainStopRepository trainStopRepository;
    private final TrainRepository trainRepository;
    private final StationViewRepository stationViewRepository;

    /**
     * 创建 TrainStop
     */
    @Transactional
    public TrainStopResponse create(@Valid TrainStopRequest dto) {
        // 获取对应的 Train
        Train train = trainRepository.findById(dto.getTrainId())
                .orElseThrow(() -> new EntityNotFoundException("Train 未找到"));

        TrainStop trainStop = new TrainStop();
        BeanUtils.copyProperties(dto, trainStop);

        validateTrainStop(trainStop, train);

        StationView station = stationViewRepository.findById(Math.toIntExact(dto.getStationId()))
                .orElseThrow(() -> new EntityNotFoundException("站点未找到或不可用"));

        trainStop.setStation(station);
        // 校验 TrainStop

        TrainStop saved = trainStopRepository.save(trainStop);
        return convertToResponse(saved);
    }

    /**
     * 根据 ID 查询 TrainStop
     */
    public TrainStopResponse getById(Long id) {
        return trainStopRepository.findById(id)
                .map(this::convertToResponse)
                .orElseThrow(() -> new EntityNotFoundException("TrainStop 未找到"));
    }

    /**
     * 查询所有 TrainStop
     */
    public List<TrainStopResponse> getAll() {
        return trainStopRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 更新 TrainStop
     */
    @Transactional
    public TrainStopResponse update(Long id, @Valid TrainStopRequest dto) {
        TrainStop trainStop = trainStopRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TrainStop 未找到"));

        Train train = trainRepository.findById(dto.getTrainId())
                .orElseThrow(() -> new EntityNotFoundException("Train 未找到"));

        BeanUtils.copyProperties(dto, trainStop);
        trainStop.setId(id);
        // 校验 TrainStop
        validateTrainStop(trainStop, train);

        StationView station = stationViewRepository.findById(Math.toIntExact(dto.getStationId()))
                .orElseThrow(() -> new EntityNotFoundException("站点未找到"));
        trainStop.setStation(station);

        TrainStop updated = trainStopRepository.save(trainStop);
        return convertToResponse(updated);
    }

    /**
     * 删除 TrainStop
     */
    @Transactional
    public void delete(Long id) {
        if (!trainStopRepository.existsById(id)) {
            throw new EntityNotFoundException("TrainStop 未找到");
        }
        trainStopRepository.deleteById(id);
    }

    /**
     * 批量更新 TrainStop
     */
    @Transactional
    public List<TrainStopResponse> batchUpdate(@Valid TrainStopBatchUpdateRequest dto) {
        Train train = trainRepository.findById(dto.getTrainId())
                .orElseThrow(() -> new EntityNotFoundException("Train 未找到"));
        List<TrainStop> originTrainStop = trainStopRepository.findAllByTrainId(dto.getTrainId());
        // dto.getTrainStops() 与 originTrainStop合并，忽略掉所有trainId不为dto.getTrainId()的trainStop
        Map<Long, TrainStopRequest> requestMap = dto.getTrainStops().stream()
                .collect(Collectors.toMap(TrainStopRequest::getId, Function.identity()));

        // 合并传入的修改到原 trainStops
        List<TrainStop> updatedTrainStops = originTrainStop.stream()
                .peek(trainStop -> {
                    // 如果传入的 trainStops 中包含该 trainStop，则更新字段
                    if (requestMap.containsKey(trainStop.getId())) {
                        TrainStopRequest request = requestMap.get(trainStop.getId());
                        BeanUtils.copyProperties(request, trainStop);
                        StationView station = stationViewRepository.findById(Math.toIntExact(request.getStationId()))
                                .orElseThrow(() -> new EntityNotFoundException("站点未找到或不可用"));
                        trainStop.setStation(station);
                    }
                })
                .sorted(Comparator.comparingInt(TrainStop::getSequence))  // 按 sequence 升序排序
                .toList();

        if (updatedTrainStops.getFirst().getArrivalTime().isBefore(train.getDepartureTime()) ||
                updatedTrainStops.getLast().getArrivalTime().isAfter(train.getArrivalTime())) {
            throw new IllegalArgumentException("arrivalTime 必须在 train 的 departureTime 和 arrivalTime 之间");
        }

        for (int i = 1; i < updatedTrainStops.size(); i++) {
            TrainStop current = updatedTrainStops.get(i);
            TrainStop previous = updatedTrainStops.get(i - 1);

            if (!current.getArrivalTime().isAfter(previous.getArrivalTime())) {
                throw new IllegalArgumentException(
                        String.format("停靠站到达时间必须递增。第%d站(%s)的到达时间%s 不晚于第%d站(%s)的到达时间%s",
                                current.getSequence(), current.getStation().getStationName(), current.getArrivalTime(),
                                previous.getSequence(), previous.getStation().getStationName(), previous.getArrivalTime())
                );
            }
        }
        List<TrainStop> savedStops = trainStopRepository.saveAll(updatedTrainStops);
        return savedStops.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 将 TrainStop 实体转换为 TrainStopResponse
     */
    private TrainStopResponse convertToResponse(TrainStop trainStop) {
        TrainStopResponse response = new TrainStopResponse();
        BeanUtils.copyProperties(trainStop, response);
        return response;
    }
    /**
     * 检验TrainStop是否是一个合法的时间区间
     */
    private void validateTrainStop(TrainStop trainStop, Train train) {
        // 校验 arrivalTime 是否在 train 的 departureTime 和 arrivalTime 之间
        if (trainStop.getArrivalTime().isBefore(train.getDepartureTime()) ||
                trainStop.getArrivalTime().isAfter(train.getArrivalTime())) {
            throw new IllegalArgumentException("arrivalTime 必须在 train 的 departureTime 和 arrivalTime 之间");
        }

        // 获取该 train 的所有 trainStops
        List<TrainStop> existingTrainStops = train.getTrainStops();

        // 校验 sequence 和 arrivalTime 的趋向
        for (TrainStop existing : existingTrainStops) {
            if (trainStop.getSequence() < existing.getSequence() &&
                    trainStop.getArrivalTime().isAfter(existing.getArrivalTime())) {
                throw new IllegalArgumentException("sequence 越小，arrivalTime 必须越早");
            }
            if (trainStop.getSequence() > existing.getSequence() &&
                    trainStop.getArrivalTime().isBefore(existing.getArrivalTime())) {
                throw new IllegalArgumentException("sequence 越大，arrivalTime 必须越晚");
            }
        }
    }

}
