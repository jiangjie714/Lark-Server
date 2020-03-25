package com.github.hollykunge.security.admin.mapper;

import com.github.hollykunge.security.admin.entity.Org;
import com.github.hollykunge.security.admin.entity.OrgDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.aspectj.weaver.ast.Or;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OrgMapper extends Mapper<Org> {

    public List<OrgDTO> findOrg(@Param("id") String id);

    /**
     *  fansq 20-2-23 添加 导入excel
     * @param orgs 导入组织信息
     */
    void insertExcelOrg(@Param("orgs") List<Org> orgs);

    List<Org> findOrgByLevelAndParentId(@Param("parentId") String parentId,@Param("orgLevel") Integer orgLevel);
}