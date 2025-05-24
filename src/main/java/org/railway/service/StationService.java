package org.railway.service;

import org.railway.entity.Station;
import org.railway.service.impl.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }
    /**
     * 查询开放车站
     * @return List<Station>
     * */
    public List<Station> getAllOpenStations() {
        return stationRepository.getAllByStatus(1);
    }

    public Optional<Station> getStationById(Integer id) {
        return stationRepository.findById(id);
    }

    public Station createStation(Station station) {
        return stationRepository.save(station);
    }

    public Station updateStation(Station station) {
        return stationRepository.save(station);
    }

    public void deleteStation(Integer id) {
        stationRepository.deleteById(id);
    }
}
