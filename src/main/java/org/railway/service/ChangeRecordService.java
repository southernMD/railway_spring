package org.railway.service;

import lombok.RequiredArgsConstructor;
import org.railway.annotation.CheckUserId;
import org.railway.dto.request.ChangeRecordRequest;
import org.railway.dto.request.TicketRequest;
import org.railway.dto.response.ChangeRecordResponse;
import org.railway.entity.ChangeRecord;
import org.railway.entity.Ticket;
import org.railway.entity.Order;
import org.railway.service.impl.ChangeRecordRepository;
import org.railway.service.impl.OrderRepository;
import org.railway.service.impl.TicketRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 改签服务类，提供改签的CRUD操作。
 * 包括创建改签记录、获取改签信息、更新改签信息、删除改签记录等功能。
 */
@Service
@RequiredArgsConstructor
public class ChangeRecordService {

    private final ChangeRecordRepository changeRecordRepository;
    private final TicketRepository ticketRepository;
    private final OrderRepository orderRepository;
    private final TicketService tickService;
    private final SeatLockService seatLockService;

    /**
     * 创建改签记录
     * @param request 改签请求DTO，包含改签的详细信息
     * @return 返回创建的改签响应DTO
     * @throws RuntimeException 如果原票、新票或订单不存在，则抛出异常
     */
    @Transactional
    public ChangeRecordResponse createChangeRecord(ChangeRecordRequest request) throws SQLException {
        ChangeRecord changeRecord = new ChangeRecord();
        BeanUtils.copyProperties(request, changeRecord);

        // 设置原票
        Ticket originalTicket = ticketRepository.findById(request.getTicketId())
                .orElseThrow(() -> new RuntimeException("原票不存在"));

        // 设置新票
//        Ticket newTicket = ticketRepository.findById(request.getNewTicketId())
//                .orElseThrow(() -> new RuntimeException("新票不存在"));
        TicketRequest newTicket = request.getNewTicket();
        // 设置订单
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        changeRecord.setOrder(order);
        Long newTicketId = 0L;
        BigDecimal val = originalTicket.getPrice().subtract(newTicket.getPrice());
        if(val.compareTo(BigDecimal.ZERO) >= 0){
            //退钱 原票直接退款(2),新票直接成功(5)
            // 因为状态5不会锁定座位，所以先切换到4创建票以后再切回状态5
            originalTicket.setRefundAmount(val);
            originalTicket.setRefundTime(LocalDateTime.now());
            originalTicket.setStatus(2);
            newTicket.setStatus(4);
            changeRecord.setOriginalTicket(originalTicket);
            changeRecord.setStatus(1);
            //删除原票的锁定记录，添加新票的锁定记录
            seatLockService.deleteBySeatId(originalTicket.getSeatLockId());
            newTicketId = tickService.createTicket(newTicket).getId();
            ticketRepository.updateStatusById(newTicketId,5);
        }else{
            //补钱 原票3-改签处理中，新票4-改签待支付
            originalTicket.setStatus(3);
            newTicket.setStatus(4);
            changeRecord.setStatus(0);
            newTicketId = tickService.createTicket(newTicket).getId();
        }
        Ticket newTicketEntity = ticketRepository.findById(newTicketId).orElseThrow( () -> new RuntimeException("新票不存在"));
        changeRecord.setOriginalTicket(originalTicket);
        changeRecord.setNewTicket(newTicketEntity);
        changeRecord.setPriceDifference(val);
        // 保存改签记录
        changeRecord.setChangeFee(BigDecimal.valueOf(Math.abs(val.doubleValue()) * 0.1));
        changeRecord = changeRecordRepository.save(changeRecord);
        ticketRepository.save(originalTicket);
        //修改订单的总金额
        if(val.compareTo(BigDecimal.ZERO) > 0){
            order.setTotalAmount(order.getTotalAmount().subtract(val));
        }else {
            order.setTotalAmount(order.getTotalAmount().add(val));
        }
        orderRepository.save(order);
        return convertToResponseDTO(changeRecord);
    }

    /**
     * 根据ID获取改签记录
     * @param id 改签记录ID
     * @return 返回改签响应DTO
     * @throws RuntimeException 如果改签记录不存在，则抛出异常
     */
    public ChangeRecordResponse getChangeRecordById(Long id) {
        ChangeRecord changeRecord = changeRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("改签记录不存在"));
        return convertToResponseDTO(changeRecord);
    }

    /**
     * 获取所有改签记录
     * @return 返回所有改签记录的响应DTO列表
     */
    public List<ChangeRecordResponse> getAllChangeRecords() {
        var a = changeRecordRepository.findAll();
        return changeRecordRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * 删除改签记录
     * @param id 改签记录ID
     */
    public void deleteChangeRecord(Long id) {
        changeRecordRepository.deleteById(id);
    }

    /**
     * 根据原票ID查询改签记录
     * @param ticketId 原票ID
     * @return 返回该原票的所有改签记录响应DTO列表
     */
    public List<ChangeRecordResponse> getChangeRecordsByTicketId(Long ticketId) {
        List<ChangeRecord> changeRecords = changeRecordRepository.findByOriginalTicketId(ticketId);
        return changeRecords.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * 根据ID和状态更新改签记录的状态
     * @param id 改签记录ID
     * @param status 新的状态值
     * @return 返回更新后的改签响应DTO
     * @throws RuntimeException 如果改签记录不存在，则抛出异常
     */
    @Transactional
    public ChangeRecordResponse updateChangeRecordStatus(Long id, Integer status,Long userId) {
        ChangeRecord changeRecord = changeRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("改签记录不存在"));
        Order order = orderRepository.findById(changeRecord.getOrder().getId()).orElseThrow(() -> new RuntimeException("订单不存在"));
        if(!Objects.equals(order.getUser().getId(), userId)){
            throw new RuntimeException("无权限");
        }
        // 更新状态
        changeRecord.setStatus(status);

        // 保存更新后的改签记录
        changeRecord = changeRecordRepository.save(changeRecord);
        return convertToResponseDTO(changeRecord);
    }


    /**
     * 将ChangeRecord实体转换为ChangeRecordResponse
     * @param changeRecord 改签记录实体
     * @return 返回改签响应DTO
     */
    private ChangeRecordResponse convertToResponseDTO(ChangeRecord changeRecord) {
        ChangeRecordResponse responseDTO = new ChangeRecordResponse();
        BeanUtils.copyProperties(changeRecord, responseDTO);
        responseDTO.setOrderId(changeRecord.getOrder().getId());
        return responseDTO;
    }
}
