package org.railway.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.railway.entity.SeatLock;

/**
 * 座位锁定返回数据
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SeatLockResponse extends SeatLock {

}
