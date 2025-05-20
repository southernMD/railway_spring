package org.railway.utils;

import java.time.LocalDateTime;
import java.util.List;

public class IntervalOverlapChecker {

    /**
     * 检查给定区间是否与区间列表中的任何区间重合
     * @param targetStart 目标区间开始时间
     * @param targetEnd 目标区间结束时间
     * @param intervals 区间列表，每个区间用包含两个LocalDateTime的数组表示
     * @return true表示有重合，false表示无重合
     */
    public static boolean hasOverlap(LocalDateTime targetStart, LocalDateTime targetEnd,
                                     List<LocalDateTime[]> intervals) {
        // 验证输入参数有效性
        if (targetStart == null || targetEnd == null || targetStart.isAfter(targetEnd)) {
            throw new IllegalArgumentException("Invalid target interval");
        }

        for (LocalDateTime[] interval : intervals) {
            if (interval == null || interval.length != 2 || interval[0] == null || interval[1] == null) {
                throw new IllegalArgumentException("Invalid interval in list");
            }

            LocalDateTime existingStart = interval[0];
            LocalDateTime existingEnd = interval[1];

            // 检查区间是否有效
            if (existingStart.isAfter(existingEnd)) {
                throw new IllegalArgumentException("Existing interval start is after end");
            }

            // 判断两个区间是否重合的逻辑
            if (!(targetEnd.isBefore(existingStart) || targetStart.isAfter(existingEnd))) {
                return true; // 发现重合
            }
        }

        return false; // 没有发现任何重合
    }
}