package com.dycn.shairportauth.util;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author liq
 * @date 2020/2/3
 */
public abstract class AbstractRedisUtil {

    /**
     * Date格式化字符串
     */
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    /**
     * DateTime格式化字符串
     */
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * Time格式化字符串
     */
    private static final String TIME_FORMAT = "HH:mm:ss";

    @Autowired
    protected StringRedisTemplate stringRedisTemplate;

    private RedisSerializer<String> getStringSerializer() {
        return stringRedisTemplate.getStringSerializer();
    }

//    private <T> RedisSerializer<T> getJsonSerializer(Class<T> clazz) {
//        return new FastJsonRedisSerializer<>(clazz);
//    }
//
//    private RedisSerializer<Object> getJsonSerializer() {
//        return getJsonSerializer(Object.class);
//    }

    protected <T> RedisSerializer<T> getJsonSerializer(Class<T> clazz) {
        Jackson2JsonRedisSerializer<T> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(
                clazz);
        jackson2JsonRedisSerializer.setObjectMapper(getObjectMapper());
        return jackson2JsonRedisSerializer;
    }

    protected RedisSerializer<Object> getJsonSerializer() {
        return getJsonSerializer(Object.class);
    }


    private ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        //注册时间模块
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATETIME_FORMAT)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_FORMAT)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATETIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DATE_FORMAT)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(TIME_FORMAT)));
        objectMapper.registerModule(javaTimeModule);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        return objectMapper;
    }

    byte[] rawKey(String key) {
        if (key == null) {
            return null;
        }
        return getStringSerializer().serialize(key);
    }

    byte[][] rawKey(String... keys) {
        if (keys == null) {
            return null;
        }
        byte[][] bytes = new byte[keys.length][];
        for (int i = 0, len = keys.length; i < len; i++) {
            bytes[i] = rawKey(keys[i]);
        }
        return bytes;
    }

    byte[] rawValue(Object value) {
        if (value == null) {
            return null;
        }
        Class<?> aClass = value.getClass();
        if (String.class.equals(value.getClass())) {
            String v = (String)value;
            return getStringSerializer().serialize(v);
        }
        return getJsonSerializer().serialize(value);
    }

    byte[][] rawValue(Object... values) {
        if (values == null) {
            return null;
        }
        byte[][] bytes = new byte[values.length][];
        for (int i = 0; i < values.length; i++) {
            bytes[i] = rawValue(values[i]);
        }
        return bytes;
    }

    protected String deserializeKey(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return getStringSerializer().deserialize(bytes);
    }

    String deserializeStringValue(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        String deserialize = getStringSerializer().deserialize(bytes);
        return deserialize != null ? deserialize : new String(bytes);
    }

    <T> T deserializeValue(byte[] bytes, Class<T> clazz) {
        if (bytes == null) {
            return null;
        }
        if (String.class.equals(clazz)) {
            return (T) deserializeStringValue(bytes);
        }
        RedisSerializer<T> jsonSerializer = getJsonSerializer(clazz);
        return jsonSerializer.deserialize(bytes);
    }

    <T> Set<T> deserializeSetValues(Set<byte[]> values, Class<T> clazz) {
        if (values == null) {
            return null;
        }
        RedisSerializer<T> redisSerializer = getJsonSerializer(clazz);
        return values.stream().map(redisSerializer::deserialize)
                .collect(Collectors.toSet());
    }

    <K, V> Map<K, V> deserializeMapValues(Map<byte[], byte[]> values, Class<K> kClass,
                                          Class<V> vClass) {
        if (values == null) {
            return null;
        }
        RedisSerializer<K> kRedisSerializer = getJsonSerializer(kClass);
        RedisSerializer<V> vRedisSerializer = getJsonSerializer(vClass);
        HashMap<K, V> kvHashMap = new HashMap<>();
        values.forEach((kBytes, vBytes) -> kvHashMap
                .put(kRedisSerializer.deserialize(kBytes), vRedisSerializer.deserialize(vBytes)));
        return kvHashMap;
    }

    <T> List<T> deserializeListValues(List<byte[]> values, Class<T> clazz) {
        if (values == null) {
            return null;
        }
        RedisSerializer<T> redisSerializer = getJsonSerializer(clazz);
        return values.stream().map(redisSerializer::deserialize)
                .collect(Collectors.toList());
    }

    <T> T execute(RedisCallback<T> callback) {
        return execute(callback, true);
    }

    private <T> T execute(RedisCallback<T> callback, boolean b) {
        return this.stringRedisTemplate.execute(callback, b);
    }

}
