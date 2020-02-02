package com.github.hollykunge.serviceunitproject.dao;

import com.github.hollykunge.serviceunitproject.model.ProjectUnit;

import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author: guxq
 * @Date: 创建于 2019/9/25 10:17
 */
public interface ProjectUnitDao extends Mapper<ProjectUnit> {
    /**
     * 项目单元
     *
     * @param projectUnit :导入项目单元
     * @return
     */
    public void saveProjectUnit(ProjectUnit projectUnit);

    /**
     * 查询项目单元
     *
     * @param id :查询项目单元
     * @return
     */
    public int queryProjectUnitByProjidCount(String id);


    /**
     * 更新项目单元
     *
     * @param projectUnit ：更新项目单元
     * @return
     */

    public int updateProjectUnitInfo(ProjectUnit projectUnit);
}

    /**
     * 通过项目单元ID查询用户列表
     *
     * @param unitID 项目单元ID
     * @return 用户列表
     */
  //   public   ProjectUnit  selectUsersByUnitId( String unitID);


    /*   *//**
     * 更新isSuccess字段
     *
     * @param isSuccess
     * @param casicOrgCode
     * @return
     *//*
    int updateOrgIsSuccess(@Param("isSuccess") String isSuccess, @Param("casicOrgCode") String casicOrgCode);
}
*/
