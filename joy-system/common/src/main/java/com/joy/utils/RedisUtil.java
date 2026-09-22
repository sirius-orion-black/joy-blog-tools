package com.joy.utils;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Component
@RequiredArgsConstructor
@Slf4j
public class RedisUtil {

    private final RedisTemplate<String, Object> REDIS_TEMPLATE;

    /**
     * 设置String类型值
     */
    public boolean set(String key, Object value) {
        try {
            REDIS_TEMPLATE.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("Redis异步删除失败, key={}", key, e);
            return false;
        }
    }

    /**
     * 设置String类型值并指定过期时间
     */
    public void sets(String key, Object value, long time) {
        try {
            if (time > 0) {
                REDIS_TEMPLATE.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
        } catch (Exception e) {
            log.error("Redis异步删除失败, key={}", key, e);
        }
    }

    /**
     * 获取String类型值
     */
    public Object get(String key) {
        return key == null ? null : REDIS_TEMPLATE.opsForValue().get(key);
    }

    /**
     * 获取 List 类型
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String key) {
        Object value = get(key);
        return value instanceof List ? (List<T>) value : null;
    }

    /**
     * 删除指定key
     */
    public void del(String key) {
        try {
            REDIS_TEMPLATE.delete(key);
        } catch (Exception e) {
            log.error("Redis异步删除失败, key={}", key, e);
        }
    }

    /**
     * 异步删除 Key
     */
    public boolean delAsync(String key) {
        try {
            return REDIS_TEMPLATE.unlink(key);
        } catch (Exception e) {
            log.error("Redis异步删除失败, key={}", key, e);
            return false;
        }
    }

    /**
     * 批量删除key
     */
    public long del(List<String> keys) {
        try {
            return REDIS_TEMPLATE.delete(keys);
        } catch (Exception e) {
            log.error("Redis异步删除失败, ",  e);
            return 0;
        }
    }

    /**
     * 异步批量删除 Key
     * @param keys 要删除的键列表
     * @return 是否执行成功
     */
    public boolean delAsync(List<String> keys) {
        if (keys == null || keys.isEmpty()) {
            return true;
        }
        try {
            // 即使原生返回 Long，我们也可以轻松转为 boolean
            Long count = REDIS_TEMPLATE.unlink(keys);
            return count > 0;
        } catch (Exception e) {
            log.error("Redis异步批量删除失败, keys={}", keys, e);
            return false;
        }
    }


    /**
     * 设置key的过期时间
     */
    public void expire(String key, long time) {
        try {
            if (time > 0) {
                REDIS_TEMPLATE.expire(key, time, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.error("Redis异步删除失败, key={}", key, e);
        }
    }

    /**
     * 获取key的过期时间
     */
    public long getExpire(String key) {
        return REDIS_TEMPLATE.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 检查key是否存在
     */
    public boolean hasKey(String key) {
        try {
            return Boolean.TRUE.equals(REDIS_TEMPLATE.hasKey(key));
        } catch (Exception e) {
            log.error("Redis异步删除失败, key={}", key, e);
            return false;
        }
    }

    /**
     * Hash类型设置字段值
     */
    public boolean hset(String key, String field, Object value) {
        try {
            REDIS_TEMPLATE.opsForHash().put(key, field, value);
            return true;
        } catch (Exception e) {
            log.error("Redis异步删除失败, key={}", key, e);
            return false;
        }
    }

    /**
     * Hash类型获取字段值
     */
    public Object hget(String key, String field) {
        return REDIS_TEMPLATE.opsForHash().get(key, field);
    }

    /**
     * Hash类型获取所有字段和值
     */
    public Map<Object, Object> hmget(String key) {
        return REDIS_TEMPLATE.opsForHash().entries(key);
    }

    /**
     * Hash类型设置多个字段值
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            REDIS_TEMPLATE.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("Redis异步删除失败, key={}", key, e);
            return false;
        }
    }

    /**
     * Hash类型删除字段
     */
    public void hdel(String key, Object... fields) {
        REDIS_TEMPLATE.opsForHash().delete(key, fields);
    }

    /**
     * Hash类型判断字段是否存在
     */
    public boolean hHasKey(String key, String field) {
        return REDIS_TEMPLATE.opsForHash().hasKey(key, field);
    }

    /**
     * Hash类型递增
     */
    public double hincr(String key, String field, double by) {
        return REDIS_TEMPLATE.opsForHash().increment(key, field, by);
    }

    /**
     * Hash类型递减
     */
    public double hdecr(String key, String field, double by) {
        return REDIS_TEMPLATE.opsForHash().increment(key, field, -by);
    }

    /**
     * Set类型添加元素
     */
    public long sSet(String key, Object... values) {
        // 1. 参数校验（可选，但推荐）
        if (key == null || key.isEmpty() || values == null || values.length == 0) {
            return 0;
        }

        try {
            // 2. 安全处理返回值
            Long result = REDIS_TEMPLATE.opsForSet().add(key, values);

            // 3. 防止 null 导致拆箱异常 (NPE)
            return result == null ? 0 : result;

        } catch (Exception e) {
            // 4. 修正日志：使用变量 key 而不是字符串 "key"
            log.error("Redis Set添加失败, key={}", key, e);
            return 0;
        }
    }


    /**
     * Set类型获取所有元素
     */
    public Set<Object> sGet(String key) {
        try {
            return REDIS_TEMPLATE.opsForSet().members(key);
        } catch (Exception e) {
            log.error("Redis异步删除失败, key={}", key, e);
            return null;
        }
    }

    /**
     * Set类型判断元素是否存在
     */
    public boolean sHasKey(String key, Object value) {
        // 参数校验
        if (key == null || key.isEmpty() || value == null) {
            return false;
        }

        try {
            // 安全处理返回值
            Boolean result = REDIS_TEMPLATE.opsForSet().isMember(key, value);

            // 防止 null 导致拆箱异常 (NPE)
            // 如果 result 为 null，视为不存在（false）
            return Boolean.TRUE.equals(result);

        } catch (Exception e) {
            log.error("Redis判断成员存在性失败, key={}, value={}", key, value, e);
            return false;
        }
    }


    /**
     * Set类型删除元素
     */
    public long setRemove(String key, Object... values) {
        // 参数校验
        if (key == null || key.isEmpty() || values == null || values.length == 0) {
            return 0;
        }

        try {
            // 安全处理返回值
            // opsForSet().remove 返回被移除元素的数量，类型为 Long
            Long result = REDIS_TEMPLATE.opsForSet().remove(key, values);

            // 3. 防止 null 导致拆箱异常 (NPE)
            return result == null ? 0 : result;

        } catch (Exception e) {
            log.error("Redis Set移除元素失败, key={}, values={}", key, java.util.Arrays.toString(values), e);
            return 0;
        }
    }


    /**
     * List类型左侧插入元素
     */
    public long lPush(String key, Object value) {
        // 1. 参数校验（可选，但推荐）
        if (key == null || key.isEmpty() || value == null) {
            return 0;
        }

        try {
            // 2. 安全处理返回值
            // opsForList().leftPush 返回操作后列表的长度，类型为 Long
            Long result = REDIS_TEMPLATE.opsForList().leftPush(key, value);

            // 3. 防止 null 导致拆箱异常 (NPE)
            return result == null ? 0 : result;

        } catch (Exception e) {
            log.error("Redis List左推失败, key={}, value={}", key, value, e);
            return 0;
        }
    }


    /**
     * List类型右侧插入元素
     */
    public long rPush(String key, Object value) {
        try {
            // 1. 使用包装类 Long 接收返回值
            Long result = REDIS_TEMPLATE.opsForList().rightPush(key, value);

            // 2. 判空处理：如果为 null，返回 0 或抛出业务异常
            if (result == null) {
                log.warn("Redis rightPush returned null for key: {}", key);
                return 0;
            }

            // 3. 安全拆箱
            return result;
        } catch (Exception e) {
            log.error("Redis设置失败, key={}", key, e);
            return 0;
        }
    }

    /**
     * List类型获取指定范围元素
     */
    public List<Object> lRange(String key, long start, long end) {
        try {
            return REDIS_TEMPLATE.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error("Redis异步删除失败, key={}", key, e);
            return null;
        }
    }

    /**
     * List类型获取指定索引元素
     */
    public Object lIndex(String key, long index) {
        try {
            return REDIS_TEMPLATE.opsForList().index(key, index);
        } catch (Exception e) {
            log.error("Redis异步删除失败, key={}", key, e);
            return null;
        }
    }

    /**
     * List类型设置指定索引元素
     */
    public boolean lSet(String key, long index, Object value) {
        try {
            REDIS_TEMPLATE.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error("Redis异步删除失败, key={}", key, e);
            return false;
        }
    }

    /**
     * List类型删除元素
     */
    public long lRemove(String key, long count, Object value) {
        // 1. 参数校验（可选，但推荐）
        if (key == null || key.isEmpty() || value == null) {
            return 0;
        }

        try {
            // 2. 安全处理返回值
            // opsForList().remove 返回被移除元素的数量，类型为 Long
            Long result = REDIS_TEMPLATE.opsForList().remove(key, count, value);

            // 3. 防止 null 导致拆箱异常 (NPE)
            return result == null ? 0 : result;

        } catch (Exception e) {
            log.error("Redis List移除元素失败, key={}, count={}, value={}", key, count, value, e);
            return 0;
        }
    }


    /**
     * 递增（计数器）
     */
    public long increment(String key, long delta) {
        // 1. 参数校验（可选，但推荐）
        if (key == null || key.isEmpty()) {
            return 0;
        }

        try {
            // 2. 安全处理返回值
            // 注意：opsForValue().increment 返回类型取决于 RedisTemplate 的泛型定义
            Long result = REDIS_TEMPLATE.opsForValue().increment(key, delta);

            // 3. 防止 null 导致拆箱异常 (NPE)
            return result == null ? 0 : result;

        } catch (Exception e) {
            log.error("Redis自增失败, key={}, delta={}", key, delta, e);
            return 0;
        }
    }


    /**
     * 递增并设置过期时间（首次调用时）
     */
    public long incrementWithExpire(String key, long delta, long time, TimeUnit timeUnit) {
        try {
            Long count = REDIS_TEMPLATE.opsForValue().increment(key, delta);
            if (count != null && count == delta) {
                REDIS_TEMPLATE.expire(key, time, timeUnit);
            }
            return count != null ? count : 0;
        } catch (Exception e) {
            log.error("Redis异步删除失败, key={}", key, e);
            return 0;
        }
    }

}
