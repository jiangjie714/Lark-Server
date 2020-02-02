package com.github.hollykunge.servicewebservice.dao;

import com.github.hollykunge.servicewebservice.model.EryuanOrg;
import com.github.hollykunge.servicewebservice.model.EryuanUser;
import tk.mybatis.mapper.common.Mapper;

public interface EryuanUserDao extends Mapper<EryuanUser>{

    /**
     * 二院用户
     * @param eryuanUser	:导入二院用户s
     * @return
     */
    public void saveEryuanUser(EryuanUser eryuanUser);

    /**
     * 二院用户
     * @param idcard_no	:查询二院用户
     * @return
     */
    public int queryEryuanUserByNoCount(String idcard_no);

    /**
     * 更新二院用户信息
     * @param eryuanUser	：用户信息
     * @return
     */
    public int updateEryuanUserInfo(EryuanUser eryuanUser);

}
