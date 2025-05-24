package org.railway.service.impl;

import org.railway.entity.TrainModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TrainModelRepository extends JpaRepository<TrainModel, Integer>, JpaSpecificationExecutor<TrainModel> {
    // 检查车型代码是否已存在
    boolean existsByModelCode(String modelCode);
    // 检查车型名称是否已存在
    boolean existsByModelName(String modelName);
    List<TrainModel> getAllByStatus(Integer status);
}