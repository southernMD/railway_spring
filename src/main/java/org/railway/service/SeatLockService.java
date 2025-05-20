package org.railway.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.railway.dto.request.SeatLockRequest;
import org.railway.dto.response.SeatLockResponse;
import org.railway.entity.Seat;
import org.railway.entity.SeatLock;
import org.railway.service.impl.SeatLockRepository;
import org.railway.service.impl.SeatRepository;
import org.railway.utils.IntervalOverlapChecker;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 座位锁定业务逻辑服务类
 * 提供对 seat_locks 表的创建和删除操作
 */
@Service
@RequiredArgsConstructor
public class SeatLockService {

    private final SeatLockRepository seatLockRepository;
    private final SeatRepository seatRepository;
    private final TaskScheduler taskScheduler;
    /**
     * 创建一个新的座位锁定记录
     *
     * @param dto 包含座位锁定信息的请求数据
     * @return 创建后的响应数据
     * @throws EntityNotFoundException 如果对应的座位不存在
     */
    public SeatLockResponse create(SeatLockRequest dto) throws SQLException {
        // 校验座位是否存在
        Seat seat = seatRepository.findById(dto.getSeatId().longValue())
                .orElseThrow(() -> new EntityNotFoundException("座位未找到"));
        Long seatId = dto.getSeatId().longValue();
        Optional<SeatLock> existingLock = seatLockRepository.findBySeatIdAndFinish(seatId, 0);
        List<LocalDateTime[]> intervals = existingLock
                .stream()
                .map(lock -> new LocalDateTime[]{lock.getLockStart(), lock.getExpireTime()})
                .toList();
        boolean hasOverlap = IntervalOverlapChecker.hasOverlap(dto.getLockStart(), dto.getExpireTime(), intervals);
        if (hasOverlap) throw new SQLException("该时刻次座位已有未完成的锁定任务");

        SeatLock lock = new SeatLock();
        BeanUtils.copyProperties(dto, lock); // DTO -> Entity
        SeatLock saved = seatLockRepository.save(lock);
        scheduleStatusUpdate(saved.getId(),saved.getSeatId(), lock.getLockStart(), lock.getExpireTime());
        return convertToResponse(saved);
    }

    /**
     * 删除指定 seatId 的座位锁定记录
     *
     * @param seatId 座位唯一标识（seat_id）
     */
    public void deleteBySeatId(Long seatId) {
        Optional<SeatLock> existing = seatLockRepository.findBySeatId(seatId);
        existing.ifPresent(seatLockRepository::delete);
    }

    /**
     * 设置定时任务，在指定时间更新状态
     */
    public void scheduleStatusUpdate(Long taskId,Long seatId, LocalDateTime lockStart, LocalDateTime expireTime) {
        boolean isLockStartInPast = LocalDateTime.now().isAfter(lockStart);
        boolean isExpireTimeInFuture = LocalDateTime.now().isBefore(expireTime);

        // 如果 lockStart 是过去时间，并且 expireTime 是未来时间，则立刻执行锁定任务
        if (isLockStartInPast && isExpireTimeInFuture) {
            updateStatus(taskId,seatId, 0); // 立即设置为锁定状态
        }

        // 只有当 lockStart 是未来时间时才安排锁定任务
        if (!isLockStartInPast) {
            Runnable lockTask = () -> updateStatus(taskId,seatId, 0);
            scheduleTask(lockTask, lockStart);
        }

        // 只有当 expireTime 是未来时间时才安排释放任务
        if (isExpireTimeInFuture) {
            Runnable releaseTask = () -> updateStatus(taskId,seatId, 1);
            scheduleTask(releaseTask, expireTime);
        }
    }


    /**
     * 封装定时任务提交逻辑
     */
    private void scheduleTask(Runnable task, LocalDateTime runAt) {
        long delay = Duration.between(LocalDateTime.now(), runAt).toMillis();
        if (delay < 0) return;

        taskScheduler.schedule(task, Instant.now().plusMillis(delay));
    }

    /**
     * 更新锁的状态
     */
    private void updateStatus(Long taskId ,Long seatId, Integer newStatus) {
        Optional<Seat> seatOpt = seatRepository.findById(seatId);
        if (seatOpt.isEmpty()) {
            return; // 座位不存在，静默退出
        }
        Optional<SeatLock> lockOpt = seatLockRepository.findById(taskId);
        if (lockOpt.isEmpty()) {
            return; // 任务不存在，静默退出
        }
        Seat lock = seatOpt.get();
        SeatLock existing = lockOpt.get();
        lock.setStatus(newStatus);
        existing.setFinish(1);

        // 保存更改
        seatRepository.save(lock);
    }

    /**
     * 将座位锁定实体转换为响应 DTO
     *
     * @param lock 座位锁定实体对象
     * @return 响应数据
     */
    private SeatLockResponse convertToResponse(SeatLock lock) {
        SeatLockResponse response = new SeatLockResponse();
        BeanUtils.copyProperties(lock,response); // Entity -> Response
        return response;
    }
}
