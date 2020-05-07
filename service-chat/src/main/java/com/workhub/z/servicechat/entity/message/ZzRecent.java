package com.workhub.z.servicechat.entity.message;

import lombok.Data;

import java.util.Date;

/**
 * @auther: zhuqz
 * @date: 2020/4/30 15:59
 * @description: 最近联系人
 */
@Data
public class ZzRecent {
   String id         ;
   Date crtTime   ;
   String crtUser   ;
   String crtName   ;
   String crtHost   ;
   Date updTime   ;
   String updUser   ;
   String updName   ;
   String updHost   ;
   String userId    ;
   /**联系人id*/
   String contactId ;
   /**消息id*/
   String msgId     ;
   /**是否置顶*/
   String top        ;
   /**是否at*/
   String at         ;
   /**未读消息条数*/
   int unreadNum ;
}