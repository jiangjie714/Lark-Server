package com.github.hollykunge.security.common.vo.mq;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * admin用户mq实体
 * @Author: yzq
 * @Date: 创建于 2019/7/22 12:51
 */
@Data
@NoArgsConstructor
@ToString
public class AdminUserVO implements Serializable {
    private static final long serialVersionUID = -8074925300463639400L;

    private String id;
    /**
     * 姓名-->OPERATOR_NAME
     */
    private String name;
    /**
     * 身份证号
     */
    private String pId;
    /**
     * 组织机构编码-->CASIC_ORG_CODE
     */
    private String orgCode;
    /**
     * 组织名称
     */
    private String orgName;
    /**
     * 密级 30-fm,40&50-mm,60&70-jm,80&90-juem
     */
    private String secretLevel;
    /**
     * 性别 1男 2女 3未知
     */
    private String gender;
    /**
     * 排序
     */
    private Long orderId;
    /**
     * 出入证号
     */
    private String empCode;
    /**
     * 出生年月
     */
    private Date birthDate;
    /**
     * 办公电话
     */
    private String oTel;
    /**
     * 办公邮件
     */
    private String oEmail;
    /**
     * 行政岗位
     */
    private String workPost;
    /**
     * 技术岗位
     */
    private String tecPost;
    /**
     * 是否删除，0新增，1修改，2删除-->SYNC_TYPE
     */
    private String deleted;
    /**
     * 姓
     */
    private String refa;
    /**
     * 名
     */
    private String refb;
    /**
     * 描述
     */
    private String description;
    /**
     * 密码  $2a$12$0pS6RoQtr2ASZyhpdUwmvO/VPBdYu.S.BFASQiJVo0ZblGrlqmABm
     */
    private  String password;
}
