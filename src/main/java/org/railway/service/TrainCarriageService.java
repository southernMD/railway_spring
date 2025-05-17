package org.railway.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.railway.dto.request.TrainCarriageRequest;
import org.railway.dto.response.TrainCarriageResponse;
import org.railway.entity.TrainCarriage;
import org.railway.service.impl.TrainCarriageRepository;
import org.railway.service.impl.TrainModelRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 车厢业务逻辑服务类
 * 提供对车厢实体的完整 CRUD 操作，并支持按车型查询
 */
@Service
@RequiredArgsConstructor
public class TrainCarriageService {

    private final TrainCarriageRepository carriageRepository;

    /**
     * 创建一个新的车厢记录
     *
     * @param dto 包含车厢信息的请求数据
     * @return 创建后的车厢响应数据
     * @throws EntityNotFoundException 如果指定的车型不存在
     */
    public TrainCarriageResponse create(TrainCarriageRequest dto) {
        TrainCarriage carriage = new TrainCarriage();
        BeanUtils.copyProperties(dto, carriage);
        TrainCarriage saved = carriageRepository.save(carriage);
        return convertToResponse(saved);
    }

    /**
     * 更新指定ID的车厢信息
     *
     * @param id  车厢唯一标识
     * @param dto 包含新车厢信息的请求数据
     * @return 更新后的车厢响应数据
     * @throws EntityNotFoundException 如果车厢或车型不存在
     */
    public TrainCarriageResponse update(Long id, TrainCarriageRequest dto) {
        TrainCarriage carriage = carriageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("车厢未找到"));

        BeanUtils.copyProperties(dto, carriage);
        return convertToResponse(carriageRepository.save(carriage));
    }

    /**
     * 删除指定ID的车厢
     *
     * @param id 车厢唯一标识
     */
    public void delete(Long id) {
        if (!carriageRepository.existsById(id)) {
            throw new EntityNotFoundException("车厢未找到");
        }
        carriageRepository.deleteById(id);
    }

    /**
     * 获取指定ID的车厢详情
     *
     * @param id 车厢唯一标识
     * @return 车厢响应数据
     * @throws EntityNotFoundException 如果车厢不存在
     */
    public TrainCarriageResponse getById(Long id) {
        return carriageRepository.findById(id)
                .map(this::convertToResponse)
                .orElseThrow(() -> new EntityNotFoundException("车厢未找到"));
    }

    /**
     * 根据车型ID获取所有关联的车厢
     *
     * @param modelId 车型唯一标识
     * @return 车厢响应数据列表
     * @throws EntityNotFoundException 如果车型不存在
     */
    public List<TrainCarriageResponse> getByModelId(Integer modelId) {

        return carriageRepository.findByModelId(modelId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 将车厢实体转换为响应 DTO
     *
     * @param carriage 车厢实体对象
     * @return 响应数据
     */
    private TrainCarriageResponse convertToResponse(TrainCarriage carriage) {
        TrainCarriageResponse response = new TrainCarriageResponse();
        BeanUtils.copyProperties(carriage, response);
        return response;
    }
}
