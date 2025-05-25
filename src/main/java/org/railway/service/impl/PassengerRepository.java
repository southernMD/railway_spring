package org.railway.service.impl;

import org.railway.entity.Passenger;
import org.railway.entity.SeatLock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    List<Passenger> findByUserId(Long userId);

}
