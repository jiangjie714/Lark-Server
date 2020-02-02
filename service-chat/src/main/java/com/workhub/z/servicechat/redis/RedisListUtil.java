package com.workhub.z.servicechat.redis;

import java.util.List;

/**
 * @author:zhuqz
 * description: redis 列表操作
 * date:2019/10/25 9:18
 **/
public class RedisListUtil{

    /**
     * 获取缓存List
     * @param key 缓存键
     * @return
     */
   public static List<String> getList(String key){
       return RedisUtil.getRedisTemplate().opsForList().range(key,0,-1);
   }
    /**
     * 添加全部list
     * @return
     */
    public static void putList(String key,List<String> list){
        RedisUtil.getRedisTemplate().opsForList().leftPushAll(key,list);
    }

    /**
     * 添加单个
     * @param key
     * @param value
     */

    public static void putSingle(String key,String value){
        RedisUtil.getRedisTemplate().opsForList().leftPush(key,value);
    }
    /**
     * 删除单个
     * @param key
     * @param value
     */

    public static void removeSingle(String key,String value){
        //删除全部等于value的值
        RedisUtil.getRedisTemplate().opsForList().remove(key,0,value);
    }
    /**
     * 添加单个 如果已经存在 不添加
     * @param key
     * @param value
     */

    public static void putSingleWithoutDup(String key,String value){
        List<String> datas = getList(key);
        if(datas!=null){
            if(!datas.contains(value)){
                putSingle(key,value);
            }
        }
    }
}
