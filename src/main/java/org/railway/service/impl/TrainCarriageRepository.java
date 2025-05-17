package org.railway.service.impl;

import org.railway.entity.TrainCarriage;
import org.railway.entity.TrainModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 车厢数据访问接口
 * 提供对 train_carriages 表的 CRUD 操作
 */
@Repository
public interface TrainCarriageRepository extends JpaRepository<TrainCarriage, Long> {
    /**
     * 根据车型查询所有车厢
     * @param modelId 车型对象Id
     * @return 车厢列表
     */
    List<TrainCarriage> findByModelId(Integer modelId);

}
