package org.railway.service.impl;

import org.railway.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Integer> {
}
