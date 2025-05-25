package org.railway.service.impl;

import org.railway.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TrainRepository extends JpaRepository<Train, Long> {
    List<Train> findByModelIdAndDate(Integer model_id, LocalDate date);

    @Query("SELECT t FROM Train t " +
            "WHERE t.date = :date " +
            "AND (" +
            "   (t.startStation.id = :startStationId AND t.endStation.id = :endStationId AND t.departureTime < t.arrivalTime) " +
            "   OR EXISTS (" +
            "       SELECT 1 FROM TrainStop ts1 WHERE ts1.trainId = t.id AND ts1.station.id = :startStationId " +
            "       AND EXISTS (" +
            "           SELECT 1 FROM TrainStop ts2 WHERE ts2.trainId = t.id AND ts2.station.id = :endStationId " +
            "           AND ts1.arrivalTime < ts2.arrivalTime" +
            "       )" +
            "   )" +
            "   OR (t.startStation.id = :startStationId AND EXISTS (" +
            "       SELECT 1 FROM TrainStop ts2 WHERE ts2.trainId = t.id AND ts2.station.id = :endStationId " +
            "       AND t.departureTime < ts2.arrivalTime" +
            "   ))" +
            "   OR (t.endStation.id = :endStationId AND EXISTS (" +
            "       SELECT 1 FROM TrainStop ts1 WHERE ts1.trainId = t.id AND ts1.station.id = :startStationId " +
            "       AND ts1.arrivalTime < t.arrivalTime" +
            "   ))" +
            ") " +
            "ORDER BY CASE " +
            "   WHEN t.startStation.id = :startStationId AND t.endStation.id = :endStationId THEN t.departureTime " +
            "   WHEN t.startStation.id = :startStationId THEN t.departureTime " +
            "   ELSE (SELECT ts1.arrivalTime FROM TrainStop ts1 WHERE ts1.trainId = t.id AND ts1.station.id = :startStationId) " +
            "END ASC")
    List<Train> findTrainsByRoute(
            @Param("startStationId") Long startStationId,
            @Param("endStationId") Long endStationId,
            @Param("date") LocalDate date);


}
