package com.workhub.z.servicechat.VO;
/**
 * @zhuqz
 * 群vo
 */

import lombok.Data;

@Data
public class GroupVo {
    //用户组id
    private String groupId;
    //组名称
    private String groupName;
    //组描述
    private String groupDescribe;
    //创建时间
    private String createTime;
    //创建人
    private String creator;
    //创建姓名
    private String creatorName;
    //项目名称
    private String pname;
    //参与范围
    private String scop;
    //是否公共
    private String ispublic;
    //讨论组等级
    private String levels;
    //是否关闭
    private String isclose;
    //群组头像
    private String groupImg;
    /**是否跨越场所0科室内1跨科室2跨场所*/
    private String iscross;
    //群组成员个数
    private String memberNums;
    //群主id
    private String groupOwnerId;
    //群主姓名
    private String groupOwnerName;
}
