package org.railway.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.railway.dto.request.SeatRequest;
import org.railway.dto.response.SeatResponse;
import org.railway.entity.Seat;
import org.railway.entity.TrainModel;
import org.railway.service.impl.SeatRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 座位业务逻辑服务类
 * 提供对座位实体的完整 CRUD 操作
 */
@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;

    /**
     * 创建一个新的座位记录
     *
     * @param dto 包含座位信息的请求数据
     * @return 创建后的座位响应数据
     */
    public SeatResponse create(SeatRequest dto) {
        Seat seat = new Seat();
        BeanUtils.copyProperties(dto,seat); // DTO -> Entity
        Seat saved = seatRepository.save(seat);
        return convertToResponse(saved);
    }

    /**
     * 更新指定ID的座位信息
     *
     * @param id  座位唯一标识
     * @param dto 新的座位数据
     * @return 更新后的座位响应数据
     * @throws EntityNotFoundException 如果座位不存在
     */
    public SeatResponse update(Long id, SeatRequest dto) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("座位未找到"));

        BeanUtils.copyProperties(dto,seat); // DTO -> Entity
        return convertToResponse(seatRepository.save(seat));
    }

    /**
     * 删除指定ID的座位
     *
     * @param id 座位唯一标识
     */
    public void delete(Long id) {
        if (!seatRepository.existsById(id)) {
            throw new EntityNotFoundException("座位未找到");
        }
        seatRepository.deleteById(id);
    }

    /**
     * 获取指定ID的座位详情
     *
     * @param id 座位唯一标识
     * @return 座位响应数据
     * @throws EntityNotFoundException 如果座位不存在
     */
    public SeatResponse getById(Long id) {
        return seatRepository.findById(id)
                .map(this::convertToResponse)
                .orElseThrow(() -> new EntityNotFoundException("座位未找到"));
    }


    /**
     * 根据车厢ID获取所有关联的座位
     *
     * @param carriageId 车厢唯一标识
     * @return 座位响应数据列表
     */
    public List<SeatResponse> getByCarriageId(Long carriageId) {
        return seatRepository.findByCarriageId(carriageId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 将座位实体转换为响应 DTO
     *
     * @param seat 座位实体对象
     * @return 响应数据
     */
    private SeatResponse convertToResponse(Seat seat) {
        SeatResponse response = new SeatResponse();
        BeanUtils.copyProperties(seat,response); // Entity -> Response
        return response;
    }
}
