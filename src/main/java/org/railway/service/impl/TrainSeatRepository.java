package org.railway.service.impl;

import org.railway.entity.TrainSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainSeatRepository extends JpaRepository<TrainSeat, Long> {

}
