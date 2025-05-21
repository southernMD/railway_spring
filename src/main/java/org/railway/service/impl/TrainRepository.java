package org.railway.service.impl;

import org.railway.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TrainRepository extends JpaRepository<Train, Long> {
    List<Train> findByModelIdAndDate(Integer model_id, LocalDate date);
}
