package com.workhub.z.servicechat.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 字典词汇表(ZzDictionaryWords)实体类
 *
 * @author makejava
 * @since 2019-05-17 14:56:57
 */
public class ZzDictionaryWords implements Serializable {
    private static final long serialVersionUID = -16442401804845507L;
    //主键
    private String id;
    //词汇类型(1-涉密，2-敏感)
    private String wordType;
    //词汇编码
    private String wordCode;
    //词汇名称
    private String wordName;
    //替换词汇
    private String replaceWord;
    //创建时间
    private Date createTime;
    //创建人
    private String createUser;
    //修改时间
    private Date updateTime;
    //修改人
    private String updateUser;
    //是否启用(0-否,1-是)
    private String isUse;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWordType() {
        return wordType;
    }

    public void setWordType(String wordType) {
        this.wordType = wordType;
    }

    public String getWordCode() {
        return wordCode;
    }

    public void setWordCode(String wordCode) {
        this.wordCode = wordCode;
    }

    public String getWordName() {
        return wordName;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }

    public String getReplaceWord() {
        return replaceWord;
    }

    public void setReplaceWord(String replaceWord) {
        this.replaceWord = replaceWord;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getIsUse() {
        return isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

}