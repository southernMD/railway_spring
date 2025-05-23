package org.railway.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.railway.dto.request.TrainModelRequest;
import org.railway.dto.request.TrainModelUpdateRequest;
import org.railway.dto.response.TrainModelResponse;
import org.railway.entity.TrainModel;
import org.railway.service.impl.TrainModelRepository;
import org.railway.utils.EntitySpecifications;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainModelService {
    private final TrainModelRepository repository;

    // 创建车型
    public TrainModelResponse create(TrainModelRequest dto) throws SQLException {
        // 检查车型代码是否已存在
        if (repository.existsByModelCode(dto.getModelCode())) {
            throw new SQLException("车型代码已存在");
        }

        TrainModel model = new TrainModel();
        BeanUtils.copyProperties(dto, model);
        TrainModel saved = repository.save(model);
        return convertToResponse(saved);
    }

    // 更新车型
    public TrainModelResponse update(Integer id, @Valid TrainModelUpdateRequest dto) throws SQLException {
        TrainModel existing = repository.findById(id)
                .orElseThrow(() -> new SQLException("车型不存在"));

        // 检查车型代码是否被其他记录使用
        if (!existing.getModelCode().equals(dto.getModelCode()) &&
                repository.existsByModelCode(dto.getModelCode())) {
            throw new SQLException("车型代码已被其他车型使用");
        }
        int existingSeatCount = existing.getCarriages().stream()
                .mapToInt(carriage -> carriage.getSeats().size())
                .sum();
        if(existingSeatCount > dto.getMaxCapacity()){
            throw new SQLException("载客量至少需要为%s".formatted(existingSeatCount));
        }
        BeanUtils.copyProperties(dto, existing, "carriages");
        TrainModel updated = repository.save(existing);
        return convertToResponse(updated);
    }

    // 获取单个车型
    public TrainModelResponse getById(Integer id) throws SQLException {
        return repository.findById(id)
                .map(this::convertToResponse)
                .orElseThrow(() -> new SQLException("车型不存在"));
    }

    // 获取所有车型
    public List<TrainModelResponse> getAll() {
        return repository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 删除车型
    public void delete(Integer id) throws SQLException {
        if (!repository.existsById(id)) {
            throw new SQLException("车型不存在");
        }
        repository.deleteById(id);
    }

    //转换模型为响应对象
    private TrainModelResponse convertToResponse(TrainModel model) {
        TrainModelResponse response = new TrainModelResponse();
        BeanUtils.copyProperties(model, response);
        return response;
    }
    // 根据条件搜索车型
    public List<TrainModelResponse> searchByCriteria(TrainModel criteria) {
        Specification<TrainModel> spec = EntitySpecifications.withEqualAllFields(criteria);
        return repository.findAll(spec).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

}