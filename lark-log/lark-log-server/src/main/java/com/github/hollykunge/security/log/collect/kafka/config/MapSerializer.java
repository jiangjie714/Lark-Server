package com.github.hollykunge.security.log.collect.kafka.config;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * 自定义序列化消息
 * @author zh
 */
public class MapSerializer implements Serializer<Map<String,Object>> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, Map<String,Object> data) {
        if (data == null) {
            return null;
        }
        Schema schema = RuntimeSchema.getSchema(data.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        byte[] bytes = null;
        try {
            bytes  = ProtostuffIOUtil.toByteArray(data, schema, buffer);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            buffer.clear();
        }
        return bytes;
    }

    @Override
    public void close() {

    }
}
