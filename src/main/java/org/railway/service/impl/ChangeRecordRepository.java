package org.railway.service.impl;

import org.railway.entity.ChangeRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChangeRecordRepository extends JpaRepository<ChangeRecord, Long> {
    /**
     * 根据原票ID查询改签记录
     * @param ticketId 原票ID
     * @return 返回该原票的所有改签记录列表
     */
    List<ChangeRecord> findByOriginalTicketId(Long ticketId);
    /**
     * 根据新票ID查询改签记录
     * @param ticketId 原票ID
     * @return 返回该原票的所有改签记录列表
     */
    List<ChangeRecord> findByNewTicketId(Long ticketId);
}
