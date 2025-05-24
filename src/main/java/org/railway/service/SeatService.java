package org.railway.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.railway.dto.request.SeatRequest;
import org.railway.dto.response.BatchDeleteResponse;
import org.railway.dto.response.SeatResponse;
import org.railway.entity.Seat;
import org.railway.service.impl.SeatRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 座位业务逻辑服务类
 * 提供对座位实体的完整 CRUD 操作
 */
@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private final PlatformTransactionManager transactionManager;
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
    /**
     * 批量删除座位
     *
     * @param seatIds 座位Id数据列表
     * @return 成功删除的结果和失败删除的结果
     */
    public BatchDeleteResponse deleteBatch(Long[] seatIds) {
        BatchDeleteResponse result = new BatchDeleteResponse();

        for (Long seatId : seatIds) {
            try {
                // 使用单独事务删除每个座位
                boolean success = executeInNewTransaction(() -> {
                    try {
                        Optional<Seat> seatOpt = seatRepository.findById(seatId);
                        if (seatOpt.isPresent()) {
                            seatRepository.deleteById(seatId);
                            entityManager.flush();
                            return true;
                        }
                        return false;
                    } catch (Exception e) {
                        return false;
                    }
                });
                if (success) {
                    result.getSuccess().add(seatId);
                } else {
                    result.getFailed().add(seatId);
                }
            } catch (Exception e) {
                result.getFailed().add(seatId);
            }
        }
        return result;
    }
    // 编程式事务管理辅助方法
    private <T> T executeInNewTransaction(Supplier<T> action) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return transactionTemplate.execute(status -> action.get());
    }

}
