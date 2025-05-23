package org.railway.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.railway.dto.request.TrainCarriageRequest;
import org.railway.dto.response.TrainCarriageResponse;
import org.railway.entity.TrainCarriage;
import org.railway.entity.TrainModel;
import org.railway.service.impl.TrainCarriageRepository;
import org.railway.service.impl.TrainModelRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 车厢业务逻辑服务类
 * 提供对车厢实体的完整 CRUD 操作，并支持按车型查询
 */
@Service
@RequiredArgsConstructor
public class TrainCarriageService {

    private final TrainCarriageRepository carriageRepository;
    private final TrainModelRepository trainModelRepository;
    private final TransactionTemplate template;
    /**
     * 创建一个新的车厢记录
     *
     * @param dto 包含车厢信息的请求数据
     * @return 创建后的车厢响应数据
     * @throws EntityNotFoundException 如果指定的车型不存在
     */
    public TrainCarriageResponse create(TrainCarriageRequest dto) {
        trainModelRepository.findById(dto.getModelId())
                .orElseThrow(() -> new EntityNotFoundException("车型未找到"));

        TrainCarriage carriage = new TrainCarriage();
        BeanUtils.copyProperties(dto, carriage,"seats");
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
        if(!Objects.equals(carriage.getCarriageType(), dto.getCarriageType()) && !carriage.getSeats().isEmpty()){
            throw new IllegalArgumentException("车厢类型不能在座位数非空时更改");
        }
        carriage.setCarriageType(dto.getCarriageType());
//        BeanUtils.copyProperties(dto, carriage,"seats");
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
     * 交换两个车厢的位置
     * @param carriageId1  第一个车厢的ID
     * @param carriageId2  第二个车厢的ID
     * @throws EntityNotFoundException 如果车厢未找到
     * @return  void
     * */
    public List<TrainCarriage> swapCarriages(Long carriageId1, Long carriageId2) {
        TrainCarriage carriage1 = carriageRepository.findById(carriageId1)
                .orElseThrow(() -> new EntityNotFoundException("车厢未找到"));
        TrainCarriage carriage2 = carriageRepository.findById(carriageId2)
                .orElseThrow(() -> new EntityNotFoundException("车厢未找到"));
        if(!Objects.equals(carriage1.getModelId(), carriage2.getModelId())){
            throw new IllegalArgumentException("车厢不属于同一车型");
        }
        Integer carriage1CarriageNumber = carriage1.getCarriageNumber();
        Integer carriage2CarriageNumber = carriage2.getCarriageNumber();
        carriage1.setCarriageNumber(-1);
        carriageRepository.save(carriage1);
        carriage2.setCarriageNumber(-2);
        carriageRepository.save(carriage2);
        carriage1.setCarriageNumber(carriage2CarriageNumber);
        carriage2.setCarriageNumber(carriage1CarriageNumber);
        TrainCarriage c1 = carriageRepository.save(carriage1);
        TrainCarriage c2 = carriageRepository.save(carriage2);
        return List.of(c1,c2);
    }
    /**
     * 批量更新车厢信息
     *
     * @param dtos 包含多个车厢更新请求的列表
     * @return 更新后的车厢响应数据列表
     * @throws EntityNotFoundException 如果车厢或车型不存在
     */
    @Transactional
    public List<TrainCarriageResponse> batchUpdate(List<TrainCarriageRequest> dtos) {
        return dtos.stream()
                .map(dto -> {
                    TrainCarriage carriage = carriageRepository.findById(dto.getId())
                            .orElseThrow(() -> new EntityNotFoundException("车厢未找到"));
                    if (!Objects.equals(carriage.getCarriageType(), dto.getCarriageType()) && !carriage.getSeats().isEmpty()) {
                        throw new IllegalArgumentException("车厢类型不能在座位数非空时更改");
                    }
                    carriage.setCarriageType(dto.getCarriageType());
                    return convertToResponse(carriageRepository.save(carriage));
                })
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
