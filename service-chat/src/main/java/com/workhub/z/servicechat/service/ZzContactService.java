package com.workhub.z.servicechat.service;

import com.workhub.z.servicechat.entity.ZzContactInf;

/**
 * @author:zhuqz
 * description: 联系人,研讨联系人信息
 * date:2019/12/3 15:02
 **/
public interface ZzContactService {
    /**
     * 查询是否存在
     * @param id
     * @return
     */
    String countsById(String id);

    /**
     * 新增
     * @param zzContactInf
     * @return
     */
    int add(ZzContactInf zzContactInf);

    /**
     * 修改
     * @param zzContactInf
     * @return
     */
    int update(ZzContactInf zzContactInf);

    /**
     * 删除
     * @param id
     * @return
     */
    int deleteData(String id);
}
