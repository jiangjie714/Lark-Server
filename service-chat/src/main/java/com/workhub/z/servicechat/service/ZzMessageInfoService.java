package com.workhub.z.servicechat.service;

import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.workhub.z.servicechat.VO.MessageHistoryVo;
import com.workhub.z.servicechat.entity.message.ZzMessageInfo;

import javax.servlet.http.HttpServletResponse;
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
    int update(ZzMessageInfo zzMessageInfo);

    /**
     * 通过主键删除数据
     *
     * @param msgId 主键
     * @return 是否成功
     */
    boolean deleteById(String msgId);
    String queryContactsMessage2(String userId);
    /**
    *@Description: 获取最近联系人历史消息
    *@Param: 当前登陆人id
    *@return: content
    *@Author: 忠
    *@date: 2019/6/23
    */
    String queryContactsMessage(String userId);
    TableResultResponse queryHistoryMessageForSingle2(String userId, String contactId, String isGroup, String query,String page, String size);
    //当前登录人查询具体某个人或者群的聊天记录,contactId表示个人或者群id
    TableResultResponse queryHistoryMessageForSingle(String userId, String contactId, String isGroup, String query,String page, String size);

    TableResultResponse queryAllMessagePrivate2(Map params) throws Exception;
    TableResultResponse queryAllMessageGroup2(Map params) throws Exception;
    TableResultResponse queryAllMessageMonitor(Map params) throws Exception;
    /**
     *
     * 查询会议或者群消息列表
     * @param type 类型 GROUP群 USER 私聊 MEET 会议，
     * @param receiver 接收人
     * @return: String
     * @Author: zhuqz
     * @Date: 2019/10/18
     **/

    String queryMessageList2(String type,String receiver,String userId);
    String queryMessageList(String type,String receiver,String userId);

    /**
     * 消息撤销
     * @param msgId
     * @return
     */
    int msgCancel(String msgId,String user);

    /**
     * 导出聊天历史记录
     * @param userId 登录人
     * @param contactId 聊天对象
     * @param beginDate 开始日期
     * @param endDate 结束日期
     * @param type 类型 user meet group
     * @return 1成功 其他失败
     */
    void exportHistoryMessageForSingle(String userId, String contactId, String beginDate, String endDate, String type, HttpServletResponse httpServletResponse);

    /**
     * 打开消息面板
     * @param sender
     * @param senderName
     * @param receiver
     * @param receiverName
     * @param ip
     */
    void openMsgBoard(String sender, String senderName,String receiver, String receiverName, String ip);
    /**
     *历史消息
     * @param user 用户id
     * @param contact 联系人id
     * @param lastMsgId 最后一条消息id
     * @param type 联系人类型 user、group、meet
     * @param size 每次几条
     * @return
     */
    List<String> listHistoryMsg(String user, String contact, String lastMsgId , String type, String size);
    /**
     *历史消息(非json)
     * @param user 用户id
     * @param contact 联系人id
     * @param lastMsgId 最后一条消息id
     * @param type 联系人类型 user、group、meet
     * @param size 每次几条
     * @return
     */
    List<MessageHistoryVo> listHistoryMsgInf(String user, String contact, String lastMsgId , String type, String size);
}