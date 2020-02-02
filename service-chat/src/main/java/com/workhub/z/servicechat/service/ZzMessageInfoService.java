package com.workhub.z.servicechat.service;

import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.entity.ZzMessageInfo;

import java.util.List;
import java.util.Map;

/**
 * 消息存储(ZzMessageInfo)表服务接口
 *
 * @author makejava
 * @since 2019-06-23 13:50:41
 */
public interface ZzMessageInfoService {

    /**
     * 通过ID查询单条数据
     *
     * @param msgId 主键
     * @return 实例对象
     */
    ZzMessageInfo queryById(String msgId);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ZzMessageInfo> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param zzMessageInfo 实例对象
     * @return 实例对象
     */
    ZzMessageInfo insert(ZzMessageInfo zzMessageInfo);

    /**
     * 修改数据
     *
     * @param zzMessageInfo 实例对象
     * @return 实例对象
     */
    ZzMessageInfo update(ZzMessageInfo zzMessageInfo);

    /**
     * 通过主键删除数据
     *
     * @param msgId 主键
     * @return 是否成功
     */
    boolean deleteById(String msgId);

    /**
    *@Description: 获取最近联系人历史消息
    *@Param: 当前登陆人id
    *@return: content
    *@Author: 忠
    *@date: 2019/6/23
    */
    String queryContactsMessage(String userId);
    //当前登录人查询具体某个人或者群的聊天记录,contactId表示个人或者群id
    TableResultResponse queryHistoryMessageForSingle(String userId, String contactId, String isGroup, String query,String page, String size);
    TableResultResponse queryAllMessagePrivate(Map params) throws Exception;
    TableResultResponse queryAllMessageGroup(Map params) throws Exception;
    /**
     *
     * 查询会议或者群消息列表
     * @param type 类型 GROUP群 USER 私聊 MEET 会议，
     * @param receiver 接收人
     * @return: String
     * @Author: zhuqz
     * @Date: 2019/10/18
     **/
    String queryMessageList(String type,String receiver,String userId);
}