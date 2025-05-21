package org.railway.service.impl;

import org.railway.entity.WaitingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 候补订单数据访问接口
 */
@Repository
public interface WaitingOrderRepository extends JpaRepository<WaitingOrder, Long> {

    /**
     * 根据用户ID查找候补订单
     * @param userId 用户ID
     * @return 候补订单列表
     */
    List<WaitingOrder> findByUserId(Long userId);

    /**
     * 根据列车ID和日期查找候补订单
     * @param trainId 列车ID
     * @param date 日期
     * @return 候补订单列表
     */
    List<WaitingOrder> findByTrainIdAndDate(Long trainId, LocalDate date);

    /**
     * 查找过期的候补订单
     * @param now 当前时间
     * @return 过期的候补订单列表
     */
    List<WaitingOrder> findByExpireTimeBefore(LocalDateTime now);

    /**
     * 根据状态查找候补订单
     * @param status 状态
     * @return 状态为指定值的候补订单列表
     */
    List<WaitingOrder> findByStatusOrderByCreateTimeAsc(Integer status);
}