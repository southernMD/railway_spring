package org.railway.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.railway.dto.request.TicketRequest;
import org.railway.dto.response.TicketResponse;
import org.railway.entity.*;
import org.railway.service.impl.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
     * 创建一个新的车票记录
     *
     * @param ticketRequest 包含车票信息的请求数据
     * @return 创建后的响应数据
     */
    public TicketResponse createTicket(@Valid TicketRequest ticketRequest) {
        Ticket ticket = new Ticket();
        BeanUtils.copyProperties(ticketRequest, ticket);

        Order order = orderRepository.findById(ticketRequest.getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("订单未找到"));
        ticket.setOrder_id(order.getId());

        Passenger passenger = passengerRepository.findById(ticketRequest.getPassengerId())
                .orElseThrow(() -> new EntityNotFoundException("乘客未找到"));
        ticket.setPassenger(passenger);

        Train train = trainRepository.findById(ticketRequest.getTrainId())
                .orElseThrow(() -> new EntityNotFoundException("列车未找到"));
        ticket.setTrain(train);

        StationView departureStation = stationViewRepository.findById(Math.toIntExact(ticketRequest.getDepartureStationId()))
                .orElseThrow(() -> new EntityNotFoundException("出发站未找到"));
        ticket.setDepartureStation(departureStation);

        StationView arrivalStation = stationViewRepository.findById(Math.toIntExact(ticketRequest.getArrivalStationId()))
                .orElseThrow(() -> new EntityNotFoundException("到达站未找到"));
        ticket.setArrivalStation(arrivalStation);

        return checkRequestReasonable(ticketRequest, ticket);
    }

    /**
     * 更新指定 ID 的车票记录
     *
     * @param id            车票唯一标识
     * @param ticketRequest 包含新数据的请求对象
     * @return 更新后的车票信息
     * @throws EntityNotFoundException 如果未找到对应记录
     */
    public TicketResponse updateTicket(Long id, @Valid TicketRequest ticketRequest) {
        Ticket existingTicket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("车票未找到"));
        BeanUtils.copyProperties(ticketRequest, existingTicket);

        if (ticketRequest.getOrderId() != null) {
            Order order = orderRepository.findById(ticketRequest.getOrderId())
                    .orElseThrow(() -> new EntityNotFoundException("订单未找到"));
            existingTicket.setOrder_id(order.getId());
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

        if (ticketRequest.getDepartureStationId() != null) {
            StationView departureStation = stationViewRepository.findById(Math.toIntExact(ticketRequest.getDepartureStationId()))
                    .orElseThrow(() -> new EntityNotFoundException("出发站未找到"));
            existingTicket.setDepartureStation(departureStation);
        }

        if (ticketRequest.getArrivalStationId() != null) {
            StationView arrivalStation = stationViewRepository.findById(Math.toIntExact(ticketRequest.getArrivalStationId()))
                    .orElseThrow(() -> new EntityNotFoundException("到达站未找到"));
            existingTicket.setArrivalStation(arrivalStation);
        }

        return checkRequestReasonable(ticketRequest, existingTicket);
    }

    private TicketResponse checkRequestReasonable(@Valid TicketRequest ticketRequest, Ticket existingTicket) {
        if (ticketRequest.getCarriageId() != null) {
            TrainCarriage carriage = trainCarriageRepository.findById(ticketRequest.getCarriageId())
                    .orElseThrow(() -> new EntityNotFoundException("车厢未找到"));
            existingTicket.setCarriage(carriage);
        }

        if (ticketRequest.getSeatId() != null) {
            Seat seat = seatRepository.findById(ticketRequest.getSeatId())
                    .orElseThrow(() -> new EntityNotFoundException("座位未找到"));
            existingTicket.setSeat(seat);
        }
        Ticket updatedTicket = ticketRepository.save(existingTicket);
        return convertToResponse(updatedTicket);
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
        response.setOrderId(ticket.getOrder_id());
        response.setPassengerId(ticket.getPassenger().getId());
        response.setTrain(ticket.getTrain());
        response.setDepartureStationId(Long.valueOf(ticket.getDepartureStation().getId()));
        response.setArrivalStationId(Long.valueOf(ticket.getArrivalStation().getId()));
        if (ticket.getCarriage() != null) {
            response.setCarriageId(ticket.getCarriage().getId());
        }
        if (ticket.getSeat() != null) {
            response.setSeatId(ticket.getSeat().getId());
        }
        return response;
    }
}
