package org.railway.service.impl;

import org.railway.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Integer> {
    List<Station> getAllByStatus(Integer status);
    Optional<Station> findByIdAndStatus(Integer id, Integer status);
}
