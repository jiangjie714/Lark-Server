package com.workhub.z.servicechat.VO;

import lombok.Data;

/**
 * @author:zhuqz
 * description: 联系人信息
 * date:2019/12/5 15:23
 **/
@Data
public class NewContactVo {
    /**联系人id*/
    private String id;
    /**联系人名称*/
    private String name;
    /**联系人头像*/
    private String avartar;
    /**联系人密级*/
    private String levels;
    /**联系人类型 USER GROUP MEET*/
    //private String type;
    /**联系人身份证号*/
    private String pid;
    /**联系人个数*/
    private String memberNum;
    /**群主，如果不是群为空*/
    private String groupOwner;
}
