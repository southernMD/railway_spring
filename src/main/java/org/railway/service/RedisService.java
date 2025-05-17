package org.railway.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 存储数据到 Redis
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // 从 Redis 获取数据
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // 删除数据
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    // 将元素添加到黑名单中
    public void addToBlacklist(String blacklistKey, String value) {
        redisTemplate.opsForSet().add(blacklistKey, value);
    }

    // 检查某个值是否在黑名单中
    public boolean isBlacklisted(String blacklistKey, String value) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(blacklistKey, value));
    }

    // 从黑名单中移除指定值
    public void removeFromBlacklist(String blacklistKey, String value) {
        redisTemplate.opsForSet().remove(blacklistKey, value);
    }

    // 获取整个黑名单
    public Set<Object> getBlacklistMembers(String blacklistKey) {
        return redisTemplate.opsForSet().members(blacklistKey);
    }

}
