package com.workhub.larktools.dao;

import com.workhub.larktools.entity.ToolFileAuth;
import com.workhub.larktools.vo.ToolFileAuthVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * author:zhuqz
 * description:
 * date:2019/8/30 14:29
 **/
public interface ToolFileAuthDao extends Mapper<ToolFileAuth> {
    int addData(@Param("params") ToolFileAuth toolFileAuth);
    /**
    * @MethodName: delete
     * @Description: 逻辑删除
     * @Param: [params] userId fileId orgCode
     * @Return: int
     * @Author: zhuqz
     * @Date: 2019/8/30
    **/
    int deleteData(@Param("params") Map params);
    /**
    * @MethodName: queryByFileId
     * @Description: 权限查询
     * @Param: [fileId, orgCode]
     * @Return: java.util.List<com.workhub.larktools.vo.ToolFileAuthVo>
     * @Author: zhuqz
     * @Date: 2019/8/30
    **/
    List<ToolFileAuthVo> queryByFileId(@Param("fileId") String fileId);
    /**
    * @MethodName: ifAuthExists
     * @Description: 是否具有下载权限1是0否
     * @Param: [fileId, orgCode]
     * @Return: java.lang.String
     * @Author: zhuqz
     * @Date: 2019/8/30
    **/
    String ifAuthExists(@Param("fileId") String fileId,@Param("orgCode") String orgCode);
}
