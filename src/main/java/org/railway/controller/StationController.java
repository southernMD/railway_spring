package org.railway.controller;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.railway.dto.response.BaseResponse;
import org.railway.entity.Station;
import org.railway.service.StationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/station")
@RequiredArgsConstructor
public class StationController {

    private final StationService stationService;

    // 创建车站
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BaseResponse<Station> createStation(@RequestBody Station station) {
        return BaseResponse.success(stationService.createStation(station));
    }

    // 查询所有车站
    @GetMapping
    public BaseResponse<List<Station>> getAllStations() {
        return BaseResponse.success(stationService.getAllStations());
    }

    // 根据ID查询车站
    @GetMapping("/{id}")
    public BaseResponse<Station> getStationById(@PathVariable Integer id) {
        return stationService.getStationById(id)
                .map(BaseResponse::success)
                .orElseThrow(() -> new RuntimeException("Station not found"));
    }

    // 更新车站信息
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BaseResponse<Station> updateStation(@RequestBody Station station) {
        return BaseResponse.success(stationService.updateStation(station));
    }

    // 删除车站
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public BaseResponse<Void> deleteStation(@PathVariable Integer id) {
        stationService.deleteStation(id);
        return BaseResponse.success(null);
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('ADMIN')")
    public BaseResponse<String> test() {
        return BaseResponse.success("123");
    }
}
