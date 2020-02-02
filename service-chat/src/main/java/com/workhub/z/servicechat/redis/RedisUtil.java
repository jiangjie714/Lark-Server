package com.workhub.z.servicechat.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author:zhuqz
 * description: redis工具类
 * date:2019/10/25 9:52
 **/
@Repository
public class RedisUtil {
    //经多线程测试 RedisTemplate是线程安全的
    private static RedisTemplate redisTemplate;
    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate){
        RedisUtil.redisTemplate = redisTemplate;
    }

    /**
     * 获取RedisTemplate
     * @return
     */
    public static RedisTemplate getRedisTemplate(){
        return RedisUtil.redisTemplate;
    }
    /**
     * 判断键是否存在
     * @param key
     * @return
     */
    public static boolean isKeyExist(String key){
       return redisTemplate.hasKey(key);
    }
    /**
     * 加入值 string
     * @param key
     * @return
     */
    public static void setValue(String key,String value){
        redisTemplate.opsForValue().set(key,value);
    }
    /**
     * 加入值 object
     * @param key
     * @return
     */
    public static void setValueObject(String key,Object value){
        redisTemplate.opsForValue().set(key,value);
    }
    /**
     * 获取值
     * @param key
     * @return
     */
    public static Object getValue(String key){
        if(isKeyExist(key)){
            return  redisTemplate.opsForValue().get(key);
        }
        return  null;
    }
    /**
     * 获取值
     * @param key
     * @return
     */
    public static void remove(String key){
        if(isKeyExist(key)){
            redisTemplate.delete(key);
        }
    }
    /**
     * 批量删除
     * @param keysPrefix
     * @return
     */
    public static void removeKeys(String keysPrefix){
        Set<String> keys = redisTemplate.keys(keysPrefix + "*");
        redisTemplate.delete(keys);
    }
}
