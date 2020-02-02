package com.workhub.z.servicechat.config;

import com.github.hollykunge.security.common.util.UUIDUtils;

import java.util.UUID;
/**
*@Description: 随机ID生成
*@Author: 忠
*@date: 2019/5/15
*/
public class RandomId {

    public static String getUUID() {
       return UUIDUtils.generateShortUuid();
    }

}
