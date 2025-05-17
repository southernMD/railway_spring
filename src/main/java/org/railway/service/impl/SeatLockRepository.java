package org.railway.service.impl;

import org.railway.entity.SeatLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatLockRepository extends JpaRepository<SeatLock, Long> {
    /**
     * 根据 seat_id 查询锁信息
     */
    Optional<SeatLock> findBySeatId(Long seatId);
    List<SeatLock> findAllByFinish(Integer finish);
    Optional<SeatLock> findBySeatIdAndFinish(Long seatId, Integer finish);

}
