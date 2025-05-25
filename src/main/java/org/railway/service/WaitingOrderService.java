package org.railway.service;

import lombok.RequiredArgsConstructor;
import org.railway.dto.request.WaitingOrderRequest;
import org.railway.entity.*;
import org.railway.service.impl.*;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.railway.utils.IntervalOverlapChecker.hasOverlap;

/**
 * 候补订单服务实现
 */
@Service
@RequiredArgsConstructor
public class WaitingOrderService {

    private final WaitingOrderRepository waitingOrderRepository;
    private final TrainSeatRepository trainSeatRepository;
    private final SeatLockRepository seatLockRepository;
    private final TrainRepository trainRepository;
    private final SeatLockService seatLockService;
    private final TrainCarriageRepository trainCarriageRepository;

    /**
     * 创建候补订单
     * @param request 候补订单请求DTO
     * @return 创建的候补订单
     */
    @Transactional
    public WaitingOrder createWaitingOrder(WaitingOrderRequest request) {
        WaitingOrder order = new WaitingOrder();
        BeanUtils.copyProperties(request, order);
        return waitingOrderRepository.save(order);
    }
    /**
     * 获取所有的候补订单
     * @return 候补订单
     */
    public List<WaitingOrder> getAllWaitingOrders() {
        return waitingOrderRepository.findAll();
    }

    /**
     * 获取候补订单详情
     * @param id 订单ID
     * @return 候补订单
     */
    public WaitingOrder getWaitingOrder(Long id) {
        return waitingOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("候补订单不存在"));
    }

    /**
     * 获取用户的所有候补订单
     * @param userId 用户ID
     * @return 候补订单列表
     */
    public List<WaitingOrder> getUserWaitingOrders(Long userId) {
        return waitingOrderRepository.findByUserId(userId);
    }

    /**
     * 更新候补订单
     * @param id 订单ID
     * @param request 更新数据
     * @return 更新后的候补订单
     */
    @Transactional
    public WaitingOrder updateWaitingOrder(Long id, WaitingOrderRequest request) {
        WaitingOrder existing = getWaitingOrder(id);
        BeanUtils.copyProperties(request, existing);
        return waitingOrderRepository.save(existing);
    }

    /**
     * 取消候补订单
     * @param id 订单ID
     */
    @Transactional
    public void cancelWaitingOrder(Long id) {
        WaitingOrder order = getWaitingOrder(id);
        order.setStatus(2); // 2表示已取消
        waitingOrderRepository.save(order);
    }

    /**
     * 处理过期的候补订单
     */
    @Transactional
    public void processExpiredOrders() {
        List<WaitingOrder> expiredOrders = waitingOrderRepository.findByExpireTimeBefore(LocalDateTime.now());
        expiredOrders.forEach(order -> {
            order.setStatus(2); // 2表示已过期/失败
            waitingOrderRepository.save(order);
        });
    }

    // 每5分钟执行一次
    @Scheduled(fixedDelay = 300000)
    @Transactional
    public void processWaitingOrders() {
        System.out.println("执行候补任务");
        // 1. 获取所有未处理的候补订单
        List<WaitingOrder> activeOrders = waitingOrderRepository.findByStatusOrderByCreateTimeAsc(0);
        // 假设1表示活跃状态
        for (WaitingOrder order : activeOrders) {
            // 2. 获取该列车指定座位类型的座位列表
            int startId = Math.toIntExact(order.getDepartureStation().getId());
            int endId = Math.toIntExact(order.getArrivalStation().getId());
            Long trainId = order.getTrain().getId();
            Train train = trainRepository.findById(trainId).orElse(null);
            if(train == null)return;
            TrainStop startStop = train.getTrainStops().stream().
                    filter(stop -> stop.getStation().getId() == startId).findFirst()
                    .orElse(null);

            TrainStop endStop = train.getTrainStops().stream().
                    filter(stop -> stop.getStation().getId() == endId).findFirst()
                    .orElse(null);
            LocalTime startLocalTime = LocalTime.now();
            LocalTime endLocalTime = LocalTime.now();
            if(startStop != null){
                startLocalTime = startStop.getArrivalTime();
            }
            if(endStop != null){
                endLocalTime = endStop.getArrivalTime();
            }
            if(startStop == null || endStop == null){
                if(order.getTrain().getStartStation().getId() == startId){
                    startLocalTime = train.getDepartureTime();
                }else if(order.getTrain().getEndStation().getId() == endId){
                    endLocalTime = train.getArrivalTime();
                }else{
                    return ;
                }
            }

            LocalDate day = order.getTrain().getDate();
            LocalDateTime startTime = LocalDateTime.of(day, startLocalTime);
            LocalDateTime endTime = LocalDateTime.of(day, endLocalTime);
            //如果任务超过1小时，则标记该任务已经过期
            if(LocalDateTime.now().isAfter(startTime.minusHours(1))){
                order.setStatus(3);
                order.setExpireTime(LocalDateTime.now());
                waitingOrderRepository.save(order);
                continue;
            }
            //获取所有的可用座位
            //
            List<Seat> seats = trainCarriageRepository.findAllByModelIdAndCarriageType(
                            order.getTrain().getModel().getId(),
                            order.getSeatType()
                    ).stream()
                    .flatMap(carriage -> carriage.getSeats().stream()) // 关键修正：确保调用stream()
                    .toList(); // 或直接用.toList()

            for (Seat seat : seats) {
                List<LocalDateTime[]> lockPlanSeats = seatLockRepository.findAllBySeatIdAndFinish(seat.getId(), 0).stream().
                                                map(lock -> new LocalDateTime[]{lock.getLockStart(), lock.getExpireTime()})
                                                .toList();

                if (!hasOverlap(startTime,endTime,lockPlanSeats)) {
                    // 4. 找到可用座位，处理候补订单
                    handleAvailableSeat(order, seat.getId(),startTime,endTime);
                    break; // 找到一个可用座位即可
                }
            }
        }
    }

    private void handleAvailableSeat(WaitingOrder order, Long seatId,LocalDateTime startTime, LocalDateTime endTime) {
        // 1. 创建座位锁定记录
        order.setStatus(1); // 1表示已完成
        waitingOrderRepository.save(order);

        // 2. 更新候补订单状态为已完成
        SeatLock lock = new SeatLock();
        lock.setSeatId(seatId);
        lock.setLockStart(startTime);
        lock.setExpireTime(endTime);
        lock.setType(1);
        lock.setFinish(0);
        lock.setReason("候补订单分配座位");
        SeatLock saved = seatLockRepository.save(lock);
        seatLockService.scheduleStatusUpdate(saved.getId(), seatId, startTime, endTime);
        // 3. 这里可以添加其他业务逻辑，如发送通知等
        //创建车票
        Ticket ticket = new Ticket();
        ticket.setTicketNo(UUID.randomUUID().toString().replace("-", ""));  // 生成32位字符串

    }
}