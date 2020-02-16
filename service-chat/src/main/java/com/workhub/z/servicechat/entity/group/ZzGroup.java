package com.workhub.z.servicechat.entity.group;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 群组表(ZzGroup)实体类
 *
 * @author 忠
 * @since 2019-05-10 14:29:32
 */
@Data
public class ZzGroup implements Serializable {
    private static final long serialVersionUID = -36565429819302307L;
    //用户组id
    private String groupId;
    //组名称
    private String groupName;
    //组描述
    private String groupDescribe;
    //是否已经解散
    private String isdelete;
    //创建时间
    private Date createTime;
    //创建人
    private String creator;
    //更新时间
    private Date updateTime;
    //更新人
    private String updator;
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
    //是否跨越场所0科室内1跨科室2跨场所
    private String iscross;
    //创建人姓名
    private String creatorName;
    //群主id
    private String groupOwnerId;
    //群主姓名
    private String groupOwnerName;
    @Override
    public String toString() {
        return "ZzGroup{" +
                "groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", groupDescribe='" + groupDescribe + '\'' +
                ", isdelete='" + isdelete + '\'' +
                ", createTime=" + createTime +
                ", creator='" + creator + '\'' +
                ", updateTime=" + updateTime +
                ", updator='" + updator + '\'' +
                ", pname='" + pname + '\'' +
                ", scop='" + scop + '\'' +
                ", ispublic='" + ispublic + '\'' +
                ", levels='" + levels + '\'' +
                ", isclose='" + isclose + '\'' +
                ", groupImg='" + groupImg + '\'' +
                ", iscross='" + iscross + '\'' +
                ", creatorName='" + creatorName + '\'' +
                ", groupOwnerId='" + groupOwnerId + '\'' +
                ", groupOwnerName='" + groupOwnerName + '\'' +
                '}';
    }

}