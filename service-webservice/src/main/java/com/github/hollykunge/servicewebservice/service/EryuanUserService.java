package com.github.hollykunge.servicewebservice.service;

import com.github.hollykunge.servicewebservice.model.EryuanUser;

public interface EryuanUserService {

    /**
     * 获取统一用户接口数据，解析XML文件，保存用户数据
     */
    public void saveEryuanUser();

    /**
     * 获取文件目录文件，解析XML文件，保存用户数据
     */
    public void saveXmlFileEryuanUser();

    /**查询用户数量
     * @param idcard_no
     */
    public int  queryEryuanUserByNoCount(String idcard_no);

    /**更新用户信息
     * @param eryuanUser
     */
    public int updateEryuanUserInfo(EryuanUser eryuanUser);

    public void saveAllPath();
}
