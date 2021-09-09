package com.dycn.shairportauth.util;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Liq
 * @date 2021-9-9
 */
@Component
public class RedisUtil extends AbstractRedisUtil {


    /**
     * 普通缓存获取
     */
    public <T> T get(String key, Class<T> clazz) {
        return execute(connection -> deserializeValue(connection.get(rawKey(key)), clazz));
    }

    public String getString(String key) {
        return execute(connection -> deserializeStringValue(connection.get(rawKey(key))));
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public Long del(String... key) {
        return execute(connection -> connection.del(rawKey(key)));
    }

    /**
     * 普通缓存放入
     */
    public Boolean set(String key, Object value) {
        return execute(connection -> connection.set(rawKey(key), rawValue(value)));
    }

    /**
     * 普通缓存放入 - 设置过期时间
     */
    public Boolean setExpire(String key, Object value,long seconds) {
        return execute(connection->{
            connection.set(rawKey(key), rawValue(value));
            return connection.expire(rawKey(key), seconds);
        });
    }

    /**
     * 递增
     */
    public Long increment(String key) {
        return execute(connection -> connection.incr(rawKey(key)));
    }

    //================set=================

    /**
     * 根据key获取Set中的所有值
     */
    public <T> Set<T> sGet(String key, Class<T> clazz) {
        return execute(connection -> deserializeSetValues(connection.sMembers(rawKey(key)), clazz));
    }

    /**
     * 根据value从一个set中查询,是否存在
     */
    public Boolean sContain(String key, Object value) {
        return execute(connection -> connection.sIsMember(rawKey(key), rawValue(value)));
    }

    /**
     * 将数据放入set缓存
     */
    public Long sSet(String key, Object... values) {
        return execute(connection -> connection.sAdd(rawKey(key), rawValue(values)));
    }

    /**
     * 移除值为value的
     */
    public Long sRemove(String key, Object... values) {
        return execute(connection -> connection.sRem(rawKey(key), rawValue(values)));
    }

    /**
     * 批量set
     */
    public void pipSet(Map<String, Object> commands) {
        if (commands != null && !commands.isEmpty()) {
            execute(connection -> {
                connection.openPipeline();
                commands.forEach((key, value) -> connection.set(rawKey(key), rawValue(value)));
                connection.closePipeline();
                return null;
            });
        }
    }

    //====================================list=============================

    /**
     * 获取list缓存的内容
     */
    public <T> List<T> lGet(String key, long start, long end, Class<T> clazz) {
        return execute(
                connection -> deserializeListValues(connection.lRange(rawKey(key), start, end), clazz));
    }

    /**
     * 将list放入缓存
     */
    public Long lSet(String key, Object value) {
        return execute(connection -> connection.rPushX(rawKey(key), rawValue(value)));
    }

    /**
     * 通过索引 获取list中的值
     */
    public <T> T lGetIndex(String key, long index, Class<T> clazz) {
        return execute(connection -> deserializeValue(connection.lIndex(rawKey(key), index), clazz));
    }

    //=====================================hash==============================

    /**
     * 通过key获取hash内容
     */
    public <T> T hGet(String key, Object hashKey, Class<T> clazz) {
        return execute(
                connection -> deserializeValue(connection.hGet(rawKey(key), rawValue(hashKey)), clazz));
    }

    /**
     * 通过key设置hash内容
     */
    public Boolean hSet(String key, Object hashKey, Object hashValue) {
//        return execute(
//                connection -> connection.hSet(rawKey(key), rawValue(hashKey), rawValue(hashValue)));
        return execute(redisConnection -> redisConnection.hSet(rawKey(key), rawValue(hashKey), rawValue(hashValue)));
    }

    // hGetAll hExists hKeys hValues hDel
    public <K, V> Map<K, V> hGetAll(String key, Class<K> kClass, Class<V> vClass) {
        return execute(
                connection -> deserializeMapValues(connection.hGetAll(rawKey(key)), kClass, vClass));
    }

    public Boolean hExists(String key, Object hashKey) {
        return execute(connection -> connection.hExists(rawKey(key), rawValue(hashKey)));
    }

    public <T> Set<T> hKeys(String key, Class<T> clazz) {
        return execute(connection -> deserializeSetValues(connection.hKeys(rawKey(key)), clazz));
    }

    public <T> List<T> hVals(String key, Class<T> clazz) {
        return execute(connection -> deserializeListValues(connection.hVals(rawKey(key)), clazz));
    }

    public Long hDel(String key, Object... hashKey) {
        return execute(connection -> connection.hDel(rawKey(key), rawValue(hashKey)));
    }

    public Long hIncr(String key, Object hashKey) {
        return execute(connection -> connection.hIncrBy(rawKey(key), rawValue(hashKey), 1L));
    }

    /**
     *@Description: 获取，如果没有则加载
     *@param key
     *@param clazz
     *@param function
     *@return: T
     *@Author: cjh
     *@date: 2020/2/25
     */
    public <T> T get(String key, Class<T> clazz, Function<String, T> function){
        T obj = get(key, clazz);
        if(obj == null) {
            obj = function.apply(key);
        }else if(obj instanceof Collection){
            Collection collection = (Collection) obj;
            if(collection.isEmpty()) {
                obj = function.apply(key);
            }
        }
        if(obj != null) {
            if(obj instanceof Collection) {
                Collection collection = (Collection) obj;
                if(!collection.isEmpty()) {
                    set(key, obj);
                }
            }else {
                set(key, obj);
            }
        }
        return obj;
    }
}
