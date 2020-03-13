package com.workhub.z.servicechat.processor;

import com.workhub.z.servicechat.config.CacheConst;
import com.workhub.z.servicechat.config.MessageType;
import com.workhub.z.servicechat.config.common;
import com.workhub.z.servicechat.redis.RedisListUtil;
import com.workhub.z.servicechat.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProcessLogin {

//    @Autowired
//    private CacheAPI cacheAPI;
      /**
     *@Description: 登录系统记录到redis
     *@Author: 忠
     *@date: 2019/8/30
     */
     public static int isOnline(String userId){

         String i = (String) RedisUtil.getValue(CacheConst.userOnlineCahce+userId);
         if(i==null){
             synchronized (ProcessLogin.class){
                 i = (String) RedisUtil.getValue(CacheConst.userOnlineCahce+userId);
                 if(i==null){
                     if("1".equals(common.isUserOnSocket(userId))){
                        i =   String.valueOf(MessageType.ONLINE);
                     } else {
                         i =   String.valueOf(MessageType.OFFLINE);
                     }
                     RedisUtil.setValue(CacheConst.userOnlineCahce+userId,String.valueOf(i));
                 }
             }
         }
        return Integer.parseInt(i);
    }
   //设置缓存用户在线
    public static int setOnLineSatus(String userId,int status){
         //当前人员在线缓存维护
        RedisUtil.setValue(CacheConst.userOnlineCahce+userId,String.valueOf(status));
        //在线人员列表缓存维护
        if(status==MessageType.OFFLINE){
            RedisListUtil.removeSingle(CacheConst.USER_ONLINE_LIST,userId);
        }else{
            RedisListUtil.putSingleWithoutDup(CacheConst.USER_ONLINE_LIST,userId);
        }

        return status;
    }

    /**
    *@Description: 退出系统
    *@Author: 忠
    *@date: 2019/9/2
    */
    public String getUserIsline(String userId){
//        String A = cacheAPI.get("i_service-chat:userId:"+userId);
//        return cacheAPI.get(userId);
        return "";
     }
}
