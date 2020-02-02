package com.workhub.larktools.dao;

import com.workhub.larktools.entity.ToolTree;
import com.workhub.larktools.vo.ToolTreeVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface ToolOrgDao extends Mapper<ToolTree> {
    /**
    * @MethodName: add
     * @Description: 添加树组织结构
     * @Param: [param]组织名称 ：name ，父节点pid如果为空认为是根节点；userId登录用户；order 排序
     * @Return: int
     * @Author: zhuqz
     * @Date: 2019/8/13
    **/
    int add(@Param("params") Map param);
    /**
    * @MethodName: addTreeOrg
     * @Description: 添加组织和工具树的映射关系
     * @Param: [param] userId、orgCode、treeId树的根节点id、id主键
     * @Return: int
     * @Author: zhuqz
     * @Date: 2019/8/13
    **/
    int addTreeOrg(@Param("params") Map param);
    /**
     * @MethodName: update
     * @Description: 更新树节点信息
     * @Param: [param] userId，id主键，name树节点名称、order排序
     * @Return: int
     * @Author: zhuqz
     * @Date: 2019/8/13
     **/
    int update(@Param("params") Map param);
    /**
     * @MethodName: updateTreeNodeParent
     * @Description: 更新树节点属于哪个父节点下
     * @Param: [param] id主键，pid父节点id，userId,order 新的排序
     * @Return: int
     * @Author: zhuqz
     * @Date: 2019/8/13
     **/
    int updateTreeNodeParent(@Param("params") Map param);
    /**
     * @MethodName: delete
     * @Description: 删除节点（子节点一并删除，逻辑删除）
     * @Param: [param] id主键，userId
     * @Return: int
     * @Author: zhuqz
     * @Date: 2019/8/13
     **/
    int deleteNode(@Param("params") Map param);
    /**
     * @MethodName: deleteTreeOrg
     * @Description: 删除树和组织结构的对应关系，逻辑删除
     * @Param: [param] treeId树节点id，userId
     * @Return: int
     * @Author: zhuqz
     * @Date: 2019/8/13
     **/
    int deleteTreeOrg(@Param("params") Map param);
    /**
     * @MethodName: query
     * @Description: 查询树节点信息
     * @Param: [param] id树节点id
     * @Return: int
     * @Author: zhuqz
     * @Date: 2019/8/13
     **/
    List<ToolTreeVo> query(@Param("params") Map param);
    /**
     * @MethodName: querySingleNode
     * @Description: 查询单个节点
     * @Param: [param] id树节点id
     * @Return: int
     * @Author: zhuqz
     * @Date: 2019/8/13
     **/
    ToolTree querySingleNode(@Param("params") Map param);
    /**
     * @MethodName: queryCurrentUserTree
     * @Description: 根据用户的组织id查询树
     * @Param: [param] orgId
     * @Return: 树结构
     * @Author: zhuqz
     * @Date: 2019/8/13
     **/
    List<ToolTreeVo> queryCurrentUserTree(@Param("orgId") String orgId);
    //该类型的树是否已经存在
    String queryExistsTypeTree(@Param("treeType") String treeType,@Param("orgCode") String orgCode);

}
