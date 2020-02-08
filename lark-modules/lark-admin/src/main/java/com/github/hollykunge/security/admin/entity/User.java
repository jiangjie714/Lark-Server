package com.github.hollykunge.security.admin.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.github.hollykunge.security.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "ADMIN_USER")
public class User extends BaseEntity implements Serializable {

    @ExcelIgnore
    private static final long serialVersionUID = -1235943548965154778L;
    /**
     * 姓名
     */
    @ExcelProperty(value = {"姓名"}, index = 0)
    @Column(name = "NAME")
    private String name;
    /**
     * 身份证号
     */
    @ExcelProperty(value = {"身份证号"}, index = 1)
    @Column(name = "P_ID")
    private String pId;
    /**
     * 组织机构编码
     */
    @ExcelProperty(value = {"组织机构编码"}, index = 2)
    @Column(name = "ORG_CODE")
    private String orgCode;
    /**
     * 组织名称
     */
    @ExcelProperty(value = {"组织名称"}, index = 3)
    @Column(name = "ORG_NAME")
    private String orgName;
    /**
     * 密级 40-fm,60-mm,80-jm
     */
    @ExcelProperty(value = {"密级"}, index = 4)
    @Column(name = "SECRET_LEVEL")
    private String secretLevel;
    /**
     * 性别 1男 2女 3未知
     */
    @ExcelProperty(value = {"性别"}, index = 5)
    @Column(name = "GENDER")
    private String gender;
    /**
     * 排序
     */
    @ExcelProperty(value = {"排序"}, index = 6)
    @Column(name = "ORDER_ID")
    private Long orderId;
    /**
     * 出入证号
     */
    @ExcelProperty(value = {"出入证号"}, index = 7)
    @Column(name = "EMP_CODE")
    private String empCode;
    /**
     * 出生年月
     */
    @ExcelProperty(value = {"出生年月"}, index = 8)
    @Column(name = "BIRTH_DATE")
    private Date birthDate;
    /**
     * 办公电话
     */
    @ExcelProperty(value = {"办公电话"}, index = 9)
    @Column(name = "O_TEL")
    private String oTel;
    /**
     * 办公邮件
     */
    @ExcelProperty(value = {"办公邮件"}, index = 10)
    @Column(name = "O_EMAIL")
    private String oEmail;
    /**
     * 行政岗位
     */
    @ExcelProperty(value = {"行政岗位"}, index = 11)
    @Column(name = "WORK_POST")
    private String workPost;
    /**
     * 技术岗位
     */
    @ExcelProperty(value = {"技术岗位"}, index = 12)
    @Column(name = "TEC_POST")
    private String tecPost;
    /**
     * 是否删除，0否，1是
     */
    @ExcelProperty(value = {"是否删除"}, index = 13)
    @Column(name = "DELETED")
    private String deleted;
    /**
     * 姓
     */
    @ExcelProperty(value = {"姓"}, index = 14)
    @Column(name = "REFA")
    private String refa;
    /**
     * 名
     */
    @ExcelProperty(value = {"名"}, index = 15)
    @Column(name = "REFB")
    private String refb;
    /**
     * 头像
     */
    @ExcelProperty(value = {"头像"}, index = 16)
    @Column(name = "AVATAR")
    private String avatar;
    /**
     * 描述
     */
    @ExcelProperty(value = {"描述"}, index = 17)
    @Column(name = "DESCRIPTION")
    private String description;

    /**
     * 密码
     */
    @ExcelProperty(value = {"密码"}, index = 18)
    @Column(name = "PASSWORD")
    private String password;


}