package org.railway.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.railway.dto.request.TicketRequest;
import org.railway.dto.request.TicketUpdateRequest;
import org.railway.dto.request.TicketUpdateStatusRequest;
import org.railway.dto.response.TicketDetailResponse;
import org.railway.dto.response.TicketResponse;
import org.railway.entity.*;
import org.railway.service.impl.*;
import org.railway.utils.IntervalOverlapChecker;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 车票业务逻辑服务类
 * 提供对 tickets 表的创建、更新、删除和查询操作
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final OrderRepository orderRepository;
    private final PassengerRepository passengerRepository;
    private final TrainRepository trainRepository;
    private final StationViewRepository stationViewRepository;
    private final TrainCarriageRepository trainCarriageRepository;
    private final SeatRepository seatRepository;
    private final TrainStopRepository trainStopRepository;
    private final SeatLockRepository seatLockRepository;
    private final SeatLockService seatLockService;
    /**
     * 查询所有车票信息
     *
     * @return 所有车票的 DTO 列表
     */
    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 根据 ID 查询车票信息
     *
     * @param id 车票唯一标识
     * @return 对应的 DTO 数据
     * @throws EntityNotFoundException 如果未找到对应记录
     */
    public TicketResponse getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("车票未找到"));
        return convertToResponse(ticket);
    }
    /**
     *  根据 ID 查询车票详细信息
     *
     * @param id 车票唯一标识
     * @return 创建后的响应数据
     */
    public TicketDetailResponse createTicketDetail(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("车票未找到"));
        TicketDetailResponse ticketDetailResponse = new TicketDetailResponse();
        BeanUtils.copyProperties(ticket, ticketDetailResponse);
        return ticketDetailResponse;
    }

    /**
     * 创建一个新的车票记录
     *
     * @param ticketRequest 包含车票信息的请求数据
     * @return 创建后的响应数据
     */
    public TicketResponse createTicket(@Valid TicketRequest ticketRequest) throws SQLException {
        Ticket ticket = new Ticket();
        BeanUtils.copyProperties(ticketRequest, ticket);

        Order order = orderRepository.findById(ticketRequest.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("订单未找到"));
        ticket.setOrderId(order.getId());

        Passenger passenger = passengerRepository.findById(ticketRequest.getPassengerId())
                .orElseThrow(() -> new EntityNotFoundException("乘客未找到"));
        ticket.setPassenger(passenger);

        Train train = trainRepository.findById(ticketRequest.getTrainId())
                .orElseThrow(() -> new EntityNotFoundException("列车未找到"));
        ticket.setTrain(train);
        // 替换原有代码
        ticket.setTicketNo(UUID.randomUUID().toString().replace("-", ""));  // 生成32位字符串
        return checkRequestReasonable(ticketRequest, ticket,true);
    }

    /**
     * 更新指定 ID 的车票记录
     *
     * @param id            车票唯一标识
     * @param ticketRequest 包含新数据的请求对象
     * @return 更新后的车票信息
     * @throws EntityNotFoundException 如果未找到对应记录
     */
    public TicketResponse updateTicket(Long id, @Valid TicketUpdateRequest ticketRequest) throws SQLException {
        Ticket existingTicket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("车票未找到"));
        boolean ifStatusChanged = !Objects.equals(existingTicket.getStatus(), ticketRequest.getStatus());
        BeanUtils.copyProperties(ticketRequest, existingTicket);

        if (ticketRequest.getOrderId() != null) {
            Order order = orderRepository.findById(ticketRequest.getOrderId())
                    .orElseThrow(() -> new EntityNotFoundException("订单未找到"));
            existingTicket.setOrderId(order.getId());
            if(!Objects.equals(order.getUser().getId(), ticketRequest.getUserId())){
                throw new EntityNotFoundException("无权限");
            }
        }

        if (ticketRequest.getPassengerId() != null) {
            Passenger passenger = passengerRepository.findById(ticketRequest.getPassengerId())
                    .orElseThrow(() -> new EntityNotFoundException("乘客未找到"));
            existingTicket.setPassenger(passenger);
        }

        if (ticketRequest.getTrainId() != null) {
            Train train = trainRepository.findById(ticketRequest.getTrainId())
                    .orElseThrow(() -> new EntityNotFoundException("列车未找到"));
            existingTicket.setTrain(train);
        }

        return checkRequestReasonable(ticketRequest, existingTicket,ifStatusChanged);
    }

    /*
    * 更新指定ID 的车票状态
    * @param ticketUpdateStatusRequest
    *
    * */
    public TicketResponse updateTicketStatus(TicketUpdateStatusRequest ticketUpdateStatusRequest) throws SQLException {
        Order order = orderRepository.findByUserIdAndId(ticketUpdateStatusRequest.getUserId(),ticketUpdateStatusRequest.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("订单未找到"));
        Ticket existingTicket = ticketRepository.findById(ticketUpdateStatusRequest.getId())
                .orElseThrow(() -> new EntityNotFoundException("车票未找到"));
        TrainCarriage carriage = trainCarriageRepository.findById(existingTicket.getCarriage().getId())
                .orElseThrow(() -> new EntityNotFoundException("车厢未找到"));
        boolean ifStatusChanged = !Objects.equals(existingTicket.getStatus(), ticketUpdateStatusRequest.getStatus());
        TicketRequest ticketRequest = new TicketRequest();
        ticketRequest.setStatus(ticketUpdateStatusRequest.getStatus());
        ticketRequest.setArrivalStationId(existingTicket.getArrivalStation().getId());
        ticketRequest.setDepartureStationId(existingTicket.getDepartureStation().getId());
        ticketRequest.setCarriageId(carriage.getId());
        seatLockTaskCheck(ticketRequest, existingTicket,ifStatusChanged);
        if(ifStatusChanged){
            //如果是已支付(1)/已改签(5)变为已退票(2)
            if(ticketUpdateStatusRequest.getStatus() == 2 && (existingTicket.getStatus() == 1 || existingTicket.getStatus() == 5)){
                //退钱
                existingTicket.setRefundAmount(existingTicket.getPrice());
                existingTicket.setRefundTime(LocalDateTime.now());
                existingTicket.setStatus(2);
                order.setTotalAmount(order.getTotalAmount().subtract(existingTicket.getPrice()));
                orderRepository.save(order);
            }
            existingTicket.setStatus(ticketUpdateStatusRequest.getStatus());
        }
        Ticket newTick = ticketRepository.save(existingTicket);
        return convertToResponse(newTick);
    }


    private TicketResponse checkRequestReasonable(TicketRequest ticketRequest, Ticket existingTicket, boolean ifStatusChanged) throws SQLException {
        boolean isDepartureStationValid = false;
        boolean isArrivalStationValid = false;

        for (TrainStop trainStop : existingTicket.getTrain().getTrainStops()) {
            if (trainStop.getStation().getId().longValue() == ticketRequest.getDepartureStationId()) {
                existingTicket.setDepartureStation(trainStop.getStation());
                isDepartureStationValid = true;
            }
            if (trainStop.getStation().getId().longValue() == ticketRequest.getArrivalStationId()) {
                existingTicket.setArrivalStation(trainStop.getStation());
                isArrivalStationValid = true;
            }
        }
        if(Objects.equals(existingTicket.getTrain().getStartStation().getId(), ticketRequest.getDepartureStationId())){
            isDepartureStationValid = true;
            existingTicket.setDepartureStation(existingTicket.getTrain().getStartStation());
        }
        if(Objects.equals(existingTicket.getTrain().getEndStation().getId(), ticketRequest.getArrivalStationId())){
            isArrivalStationValid = true;
            existingTicket.setArrivalStation(existingTicket.getTrain().getEndStation());
        }

        if (!isDepartureStationValid || !isArrivalStationValid) {
            throw new IllegalArgumentException("出发站或到达站不在列车的停靠站列表中");
        }

        if (ticketRequest.getCarriageId() != null) {
            TrainCarriage carriage = trainCarriageRepository.findById(ticketRequest.getCarriageId())
                    .orElseThrow(() -> new EntityNotFoundException("车厢未找到"));
            existingTicket.setCarriage(carriage);
        }

        if (ticketRequest.getSeatId() != null) {
            Seat seat = seatRepository.findById(ticketRequest.getSeatId())
                    .orElseThrow(() -> new EntityNotFoundException("座位未找到"));
            existingTicket.setSeat(seat);
            //判断改时间段座位是否可用
            existingTicket.setSeatLockId(seatLockTaskCheck(ticketRequest, existingTicket,ifStatusChanged));
        }

        Ticket updatedTicket = ticketRepository.save(existingTicket);
        return convertToResponse(updatedTicket);
    }

    /*
     *   这个用来检查锁定任务是否应该添加
     *  在新状态为2取消时清除任务
     *   新状态为0待支付/4改签待支付时添加
     * */
    private Long seatLockTaskCheck(TicketRequest ticketRequest, Ticket existingTicket, boolean ifStatusChanged) throws SQLException {
        if(ifStatusChanged && ticketRequest.getCarriageId() != null){
            if(ticketRequest.getStatus() == 2){
                seatLockService.deleteBySeatId(existingTicket.getSeatLockId());
                return null;
            }
            if(ticketRequest.getStatus() != 0 && ticketRequest.getStatus() != 4) return null;
        }
        LocalTime startTime = null;
        LocalTime endTime = null;
        TrainStop startStation = trainStopRepository.findByTrainIdAndStationId(existingTicket.getTrain().getId(), existingTicket.getDepartureStation().getId());
        TrainStop endStation = trainStopRepository.findByTrainIdAndStationId(existingTicket.getTrain().getId(), existingTicket.getArrivalStation().getId());
        LocalDate day = existingTicket.getTrain().getDate();
        if (startStation == null) {
            if (Objects.equals(existingTicket.getTrain().getStartStation().getId(), ticketRequest.getDepartureStationId())) {
                startTime = existingTicket.getTrain().getDepartureTime();
            }
        }
        if (endStation == null) {
            if (Objects.equals(existingTicket.getTrain().getEndStation().getId(), ticketRequest.getArrivalStationId())) {
                endTime = existingTicket.getTrain().getArrivalTime();
            }
        }
        if (startStation != null) {
            startTime = startStation.getArrivalTime();
        }
        if (endStation != null) {
            endTime = endStation.getArrivalTime();
        }
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("该列车没有停靠该站点");
        }
        LocalDateTime startDateTime = LocalDateTime.of(day, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(day, endTime);
        List<SeatLock> existingLock = seatLockRepository.findAllBySeatIdAndFinish(existingTicket.getSeat().getId(), 0);
        List<LocalDateTime[]> intervals = existingLock
                .stream()
                .map(lock -> new LocalDateTime[]{lock.getLockStart(), lock.getExpireTime()})
                .toList();
        boolean hasOverlap = IntervalOverlapChecker.hasOverlap(startDateTime, endDateTime, intervals);
        if (hasOverlap) throw new SQLException("该时刻次座位已有未完成的锁定任务");

        //改签待支付，或者，未支付
        return createSeatLockTask(existingTicket.getSeat().getId(), startDateTime, endDateTime);
    }
    /**
     * 用户购买车票后添加锁定任务
     *
     * @return 座位锁定任务的 ID
     */
    private Long createSeatLockTask(Long seatId, LocalDateTime startDateTime, LocalDateTime endDateTime) throws SQLException {
        SeatLock lock = new SeatLock();
        lock.setSeatId(seatId);
        lock.setLockStart(startDateTime);
        lock.setExpireTime(endDateTime);
        lock.setFinish(0);
        lock.setType(1);
        lock.setReason("用户购买");
        SeatLock saved = seatLockRepository.save(lock);
        seatLockService.scheduleStatusUpdate(saved.getId(), seatId, startDateTime, endDateTime);
        return saved.getId();
    }

    /**
     * 删除指定 ID 的车票记录
     *
     * @param id 车票唯一标识
     * @throws EntityNotFoundException 如果未找到对应记录
     */
    public void deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new EntityNotFoundException("车票记录不存在");
        }
        ticketRepository.deleteById(id);
    }

    /**
     * 将 Ticket 实体转换为 TicketResponse DTO
     *
     * @param ticket 车票实体
     * @return 车票响应 DTO
     */
    private TicketResponse convertToResponse(Ticket ticket) {
        TicketResponse response = new TicketResponse();
        BeanUtils.copyProperties(ticket, response);
        response.setOrderId(ticket.getOrderId());
        response.setPassengerId(ticket.getPassenger().getId());
        response.setTrain(ticket.getTrain());
        response.setDepartureStationId(ticket.getDepartureStation().getId());
        response.setArrivalStationId(ticket.getArrivalStation().getId());
        if (ticket.getCarriage() != null) {
            response.setCarriageId(ticket.getCarriage().getId());
        }
        if (ticket.getSeat() != null) {
            response.setSeatId(ticket.getSeat().getId());
        }
        return response;
    }
}
