package org.railway.utils;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntitySpecifications {

    /**
     * 构建一个 Specification，自动匹配所有非空字段
     */
    public static <T> Specification<T> withEqualAllFields(T criteria) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Field[] fields = criteria.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true); // 允许访问私有字段
                try {
                    Object value = field.get(criteria);
                    if (value == null) continue;
                    if (value instanceof String) {
                        // 对字符串字段进行模糊查询
                        predicates.add(cb.like(cb.lower(root.get(field.getName())), "%" + ((String) value).toLowerCase() + "%"));
                    } else {
                        // 对其他字段进行精确匹配（可以扩展为范围查询）
                        predicates.add(cb.equal(root.get(field.getName()), value));
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("字段读取失败: " + field.getName(), e);
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
