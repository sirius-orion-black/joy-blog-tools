package com.joy.utils;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Component
@RequiredArgsConstructor
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
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置String类型值并指定过期时间
     */
    public boolean setex(String key, Object value, long time) {
        try {
            if (time > 0) {
                REDIS_TEMPLATE.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取String类型值
     */
    public Object get(String key) {
        return key == null ? null : REDIS_TEMPLATE.opsForValue().get(key);
    }

    /**
     * 删除指定key
     */
    public boolean del(String key) {
        try {
            return REDIS_TEMPLATE.delete(key);
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 设置key的过期时间
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                REDIS_TEMPLATE.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
        try {
            return REDIS_TEMPLATE.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Set类型判断元素是否存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return REDIS_TEMPLATE.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Set类型删除元素
     */
    public long setRemove(String key, Object... values) {
        try {
            return REDIS_TEMPLATE.opsForSet().remove(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * List类型左侧插入元素
     */
    public long lPush(String key, Object value) {
        try {
            return REDIS_TEMPLATE.opsForList().leftPush(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * List类型右侧插入元素
     */
    public long rPush(String key, Object value) {
        try {
            return REDIS_TEMPLATE.opsForList().rightPush(key, value);
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
            return false;
        }
    }

    /**
     * List类型删除元素
     */
    public long lRemove(String key, long count, Object value) {
        try {
            return REDIS_TEMPLATE.opsForList().remove(key, count, value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 递增（计数器）
     */
    public long increment(String key, long delta) {
        try {
            return REDIS_TEMPLATE.opsForValue().increment(key, delta);
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
            return 0;
        }
    }

}
