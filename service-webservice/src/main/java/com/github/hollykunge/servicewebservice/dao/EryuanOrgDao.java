package com.github.hollykunge.servicewebservice.dao;

import com.github.hollykunge.servicewebservice.model.EryuanOrg;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author: yzq
 * @Date: 创建于 2019/7/25 10:17
 */
public interface EryuanOrgDao extends Mapper<EryuanOrg> {
    /**
     * 二院部门
     * @param eryuanOrg	:导入二院部门
     * @return
     */
    public void saveEryuanOrg(EryuanOrg eryuanOrg);

    /**
     * 二院部门
     * @param CASIC_ORG_CODE	:查询二院用户
     * @return
     */
    public int queryEryuanOggByOrgidCount(String CASIC_ORG_CODE);

    /**
     * 更新二院部门
     * @param eryuanOrg	：二院部门
     * @return
     */
    public int updateEryuanOrgInfo(EryuanOrg eryuanOrg);

    /**
     * 更新isSuccess字段
     * @param isSuccess
     * @param casicOrgCode
     * @return
     */
    int updateOrgIsSuccess(@Param("isSuccess")String isSuccess,@Param("casicOrgCode")String casicOrgCode);

    /**
     * 更新isSuccess字段
     * @param onedocIsSuccess
     * @param casicOrgCode
     * @return
     */
    int updateOrgOnedocIsSuccess(@Param("onedocIsSuccess")String onedocIsSuccess,@Param("casicOrgCode")String casicOrgCode);

    /**
     * 查找父级PATH
     * @param casicPOrgCode
     * @return
     */
    String queryPathByCode(@Param("casicPOrgCode") String casicPOrgCode);

    /**
     * 查找父级PATH_NAME
     * @param casicPOrgCode
     * @return
     */
    String queryPathNameByCode(@Param("casicPOrgCode") String casicPOrgCode);
}
