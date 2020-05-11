package com.github.hollykunge.security.log.collect.kafka.config;

import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义反序列化消息
 * @author zh
 */
public class MapDeserialize implements Deserializer<Map<String,Object>> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }
    @Override
    public Map<String,Object> deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        Schema<Map> schema = RuntimeSchema.getSchema(Map.class);
        Map<String,Object> company = new HashMap<>();
        ProtostuffIOUtil.mergeFrom(data, company, schema);
        return company;
    }
    @Override
    public void close() {
    }
}
