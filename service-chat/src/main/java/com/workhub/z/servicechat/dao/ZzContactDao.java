package com.workhub.z.servicechat.dao;

import com.workhub.z.servicechat.entity.ZzContactInf;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author:zhuqz
 * description: 联系人信息
 * date:2019/12/3 14:57
 **/
public interface ZzContactDao extends Mapper<ZzContactInf> {
    /**
     * 查询是否存在
     * @param id
     * @return
     */
    String countsById(@Param("id") String id);

    /**
     * 新增
     * @param zzContactInf
     * @return
     */
    int add(@Param("params") ZzContactInf zzContactInf);

    /**
     * 修改
     * @param zzContactInf
     * @return
     */
    int update(@Param("params") ZzContactInf zzContactInf);

    /**
     * 删除
     * @param id
     * @return
     */
    int deleteData(@Param("id") String id);
}
