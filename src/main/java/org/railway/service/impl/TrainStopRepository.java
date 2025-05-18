package org.railway.service.impl;

import org.railway.entity.TrainStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainStopRepository extends JpaRepository<TrainStop, Long> {
    //更具train_id查询所有数据
    List<TrainStop> findAllByTrainId(Long trainId);
}
