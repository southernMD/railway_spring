package org.railway.task;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.railway.entity.Seat;
import org.railway.entity.SeatLock;
import org.railway.service.impl.SeatLockRepository;
import org.railway.service.impl.SeatRepository;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SeatLockTaskRecovery {

    private final SeatLockRepository seatLockRepository;
    private final SeatRepository seatRepository;
    private final TaskScheduler taskScheduler;

    @PostConstruct
    public void recoverTasks() {
        LocalDateTime now = LocalDateTime.now();

        // 只恢复 finish = 0 的任务
        seatLockRepository.findAllByFinish(0).forEach(lock -> {
            Long taskId = lock.getId();
            Long seatId = lock.getSeatId();
            LocalDateTime lockStart = lock.getLockStart();
            LocalDateTime expireTime = lock.getExpireTime();

            boolean isLockStartInFuture = now.isBefore(lockStart);
            boolean isExpireTimeInFuture = now.isBefore(expireTime);

            // ========== 处理锁定任务 ========== //
            if (isLockStartInFuture) {
                Runnable lockTask = () -> updateStatus(taskId, seatId, 0);
                scheduleTask(lockTask, lockStart);
            }

            // ========== 处理解锁任务 ========== //
            if (isExpireTimeInFuture) {
                Runnable releaseTask = () -> updateStatus(taskId, seatId, 1);
                scheduleTask(releaseTask, expireTime);
            } else {
                // expireTime 已过，立刻释放
                updateStatus(taskId, seatId, 1);
            }
        });
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
     * 更新座位状态，并标记任务为已完成
     */
    private void updateStatus(Long taskId, Long seatId, Integer newStatus) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("找不到座位 ID：" + seatId));
        seat.setStatus(newStatus);
        seatRepository.save(seat);

        // 标记任务为已完成
        SeatLock lock = seatLockRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("找不到任务 ID：" + taskId));
        lock.setFinish(1);
        seatLockRepository.save(lock);
    }
}
