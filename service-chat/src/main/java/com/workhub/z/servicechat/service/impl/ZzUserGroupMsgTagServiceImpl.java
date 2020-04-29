package com.workhub.z.servicechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.workhub.z.servicechat.VO.SingleMessageVo;
import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.dao.ZzUserGroupMsgTagDao;
import com.workhub.z.servicechat.entity.group.ZzUserGroupMsgTag;
import com.workhub.z.servicechat.service.ZzUserGroupMsgTagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.workhub.z.servicechat.config.Common.putEntityNullToEmptyString;

/**
 * 用户标记群消息
 *
 * @author zhuqz
 * @since 2019-06-11
 */
@Service("zzUserGroupMsgTagService")
public class ZzUserGroupMsgTagServiceImpl implements ZzUserGroupMsgTagService {
    private static Logger log = LoggerFactory.getLogger(ZzUserGroupMsgTagServiceImpl.class);
    @Resource
    private ZzUserGroupMsgTagDao zzUserGroupMsgTagDao;
   /* @Override
    protected String getPageName() {
        return null;
    }*/
    /**
     * 增加群消息标记
     * @param entity 数据库实体
     * @return  1成功；0用户不在组内或者组已经不存在；-1错误
     * @author zhuqz
     * @since 2019-06-11
     */
    @Override
    public String addUserGroupMsgTag(ZzUserGroupMsgTag entity) throws Exception{
        String res="1";
        //调用父类方法目前报错，暂时改成自己的方法，如果以后父类方法修改好，应该改成走父类方法。
        //super.insert(entity);
        //调用自己的方法新增数据
        try {
            putEntityNullToEmptyString(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int rows = this.zzUserGroupMsgTagDao.insert(entity);
        return  res;
    }
    /**
     * 按照id删除群消息标记
     * @param id 数据库id
     * @return  1成功；0用户不在组内或者组已经不存在；-1错误
     * @author zhuqz
     * @since 2019-06-11
     */
    @Override
    public String deleteById(String id) throws Exception{
        /*String res="1";
        ZzUserGroupMsgTag entity = new ZzUserGroupMsgTag();
        entity.setId(id);
        super.delete(entity);
        return  res;*/
        int i = this.zzUserGroupMsgTagDao.deleteById(id);
        return "1";
    }
    /**
     * 按照条件（用户id，群id，消息id，标记类型）删除群消息标记
     * @param entity 数据库实体
     * @return  1成功；0用户不在组内或者组已经不存在；-1错误
     * @author zhuqz
     * @since 2019-06-11
     */
    @Override
    public String deleteByConditions(ZzUserGroupMsgTag entity) throws Exception{
        String res="1";
        //如果没有用户id，设置一个不存在的值，防止数据误删
        if((entity.getUserId()==null || entity.getUserId().equals(""))){
            entity.setUserId("nodata");
        }
        int rows = this.zzUserGroupMsgTagDao.deleteByConditions(entity);
        return  res;
    }

    /**
     * 查询用户标记消息（自己实现分页，目前父类分页不支持排序、查询条件复杂化）
     * @param
     * @return  数据列表
     * @author zhuqz
     * @since 2019-06-11
     */
    @Override
    public TableResultResponse getUserGroupMsgTagList(String userId, String groupId, String tagType, int pageNum, int pageSize) throws Exception{

        //调用父类查询分页begin
        /*Map<String, Object> params = new HashMap<>();
            if(!(groupId==null || groupId.equals(""))){
                params.put("groupId",groupId);
            }
            if(!(tagType==null || tagType.equals(""))){
                params.put("tagType",tagType);
            }
            if(!(userId==null || userId.equals(""))){
                params.put("userId",userId);
            }
            params.put("pageNo",pageNum);
            params.put("pageSize",pageSize);
            Query query = new Query(params);
            return  super.selectByQuery(query);*/
        //调用父类查询分页end

        //自己写分页
        PageHelper.startPage(pageNum, pageSize);
        List<String> list = this.zzUserGroupMsgTagDao.getInfList(userId,groupId,tagType);
        PageInfo pageInfo = new PageInfo<>(list);

        List<SingleMessageVo> voList=new ArrayList<>();
        for(String temp:list){
            SingleMessageVo vo = JSON.parseObject(temp, SingleMessageVo.class);
            voList.add(vo);
        }
        Common.putVoNullStringToEmptyString(voList);

        TableResultResponse res = new TableResultResponse(
                pageInfo.getPageSize(),
                pageInfo.getPageNum(),
                pageInfo.getPages(),
                pageInfo.getTotal(),
                voList
        );
        return res;
    }
}
