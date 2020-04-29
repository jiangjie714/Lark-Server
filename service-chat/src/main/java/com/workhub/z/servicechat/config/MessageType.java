package com.workhub.z.servicechat.config;

/**
*@Description: 消息码
*@Param:
*@return:
*@Author: 忠
*@date: 2019/5/29
*/
public class MessageType {

    /**
     * 用于系统消息，每个登录用户都要加入系统消息组
     * 系统通知
     */
    public static final String GROUP_SYS = "Iwork-Message";

    /**个人消息*/
    public static final int PRIVATE_MSG = 200100;
    //群消息
    public static final int GROUP_MSG = 200200;
    //系统消息
    public static final int SYS_MSG = 2;
    //会议消息
    public static final int MEET_MSG = 200300;

    //群创建
    public static final int GROUP_CREATE = 200201;
    //退群
    public static final int GROUP_QUIT = 200202;
    //群组操作
    public static final int GROUP_EDIT = 200203;
    //加入群组
    public static final int GROUP_JOIN_MSG = 5;
    //邀请加入群组
    public static final int GROUP_INVITE_MSG = 6;
    //退出群组
    public static final int GROUP_EXIT_MSG = 7;
    //关闭群组
    public static final int GROUP_CLOSE_MSG = 8;

    //返回码 boolean
    public static final int MSG_EDIT_READ = 9;
    //群组创建成功应答
    public static final int CREATE_GROUP_ANS = 200204;
    //队列
    //人员、组织机构变更

    //工作台

    //消息应答
    //消息应答码
    public static final int MSG_SEND_ANSWER = 300101;
    //成功
    public static final int SUCCESS_ANSWER = 200;
    //失败
    public static final int FAIL_ANSWER = 201;
    //提示
    public static final int TIPS_ANSWER = 202;
    //不在线
    public static final int OFFLINE_ANSWER = 204;

    //在线状态 7
    public static final int LINESTATUS = 700100;
    //在线
    public static final int ONLINE = 700;
    //离线
    public static final int OFFLINE = 701;

    /*群或者会议以及其他资源流水操作类型码*/
    //新建
    public static final String FLOW_NEW = "300";
    //新增成员
    public static final String FLOW_ADD_MEMBER = "301";
    //删除成员
    public static final String FLOW_DELETE_MEMBER = "302";
    //群解散
    public static final String FLOW_DISSOLUTION= "303";
    //上传群文件
    public static final String FLOW_UPLOADFILE= "304";
    //下载群文件
    public static final String FLOW_DOWNLOADFILE= "305";
    //群审批
    public static final String FLOW_APPROVE= "306";

    /**会议参数*/
    /**创建会议*/
    public static final int CREATE_MEETING = 901;
    /**结束会议*/
    public static final int CLOSE_MEETING = 902;
    /**会议信息变更*/
    public static final int MEET_CHANGE = 200302;
    /**会议权限变更*/
    public static final int ROLE_CHANGE= 904;

    /**会议审批通过*/
    public static final int MEETING_APPROVE_PASS = 906;
    /**新增会议*/
    public static final int MEETING_ADD = 200301;
    /**会议进行中*/
    public static final int MEETING_GOING_ON = 908;

    /**会议文件类型*/
    /**参评*/
    public static final int MEETING_FILE_APPROVE = 9000;
    /**参考*/
    public static final int MEETING_FILE_REFERECE = 9001;
    /**结论*/
    public static final int MEETING_FILE_RESULT = 9002;

    /**私人文件*/
    public static final int PRIVATE_FILE = 0;
    /**群文件*/
    public static final int GROUP_FILE = 1;
    /**会议文件*/
    public static final int MEETING_FILE= 905;

    //流水日志群
    public static final  String FLOW_LOG_GROUP = "0";
    //流水日志会议
    public static final  String FLOW_LOG_MEET = "1";
    /**群、会议创建、附件上传下载等等是否需要审批的通知的消息代码*/
    public static final int APPROVE_AUTHORITY_CODE = 500100;
    /**需要审批权限*/
    public static final int REQUIRE_APPROVE_AUTHORITY = 1;
    /**不需要审批权限*/
    public static final int NO_REQUIRE_APPROVE_AUTHORITY = 0;
    /**跨场所类型*/
    /**
     * 院机关编码
     */
    public static final String ORG_CODE_INSTITUTE_ORGAN = "001000";
    /**科室内*/
    public static final String CROSSTYPE_SAME_OFFICE = "0";
    /**跨科室*/
    public static final String CROSSTYPE_DIFF_OFFICE = "1";
    /**跨场所*/
    public static final String CROSSTYPE_DIFF_WORKSPACE = "2";

   /**密级类型*/
   /**非密*/
    public static final String NO_SECRECT_LEVEL = "30";
   /**秘密*/
    public static final String  NORMAL_SECRECT_LEVEL = "40";
   /**机密*/
    public static final String HIGH_SECRECT_LEVEL = "60";

    /**
     * 信息中心 编码规则六位 三层级别 前两位是信息中心大类，如20代表单人消息
     * 后四位对于业务代码，中间两位代表业务一层，最后两位代表具体业务
     * 举例：200201 前两位20代表单人消息 中间两位02代表给单人发的是通知类消息，
     * 最后两位01说明接收人点开了和他聊天的界面,看见了所有消息
     * 目前因为前期编码未考虑，没有完全遵守该规则
     **/
    /**群体消息**/
    public static final String SOCKET_TEAM = "100000";
    /**单人消息**/
    public static final String SOCKET_SINGLE = "200000";
    /**单人通知对方打开消息面包 通知发送人，接收人点开了和他聊天的界面,看见了所有消息**/
    public static final String SOCKET_SINGLE_NOTE_SEEMSG = "200101";
    /**消息撤销、删除**/
    public static final String SOCKET_MSG_DEL = "200102";
    //消息接收应答
    public static final String MSG_RECEIVE_ANSWER = "300102";
    //消息接收确认
    public static final String MSG_RECEIVB_CONFIRM = "300103";
    //离线消息
    public static final String SOCKET_SINGLE_OFFLINE_MSG = "400100";
    //握手反馈
    public static final String SOCKET_SINGLE_HAND_SHAKE = "200401";
    /**群体socket群体绑定**/
    public static final String SOCKET_TEAM_BIND = "300000";
    /**群体列表socket群体绑定用户**/
    public static final String SOCKET_TEAM_BIND_LIST = "400000";
    /**群体socket绑定解除**/
    public static final String SOCKET_TEAM_UNBIND = "500000";
    /**新增收藏**/
    public static final String SOCKET_COLLECT_ADD = "800100";
    /**删除收藏*/
    public static final String SOCKET_COLLECT_DELETE = "800101";
    /**信息中心编码，默认**/
    public static final String SOCKET_DEFAULT = "999999";
    /**信息中心详细编码，默认**/
    public static final String SOCKET_DETAIL_DEFAULT = "999999";
    /***在线客服信息id*/
    public static final String ONLINE_MSG_ID = "999999";

    /**消息撤销 类型**/
    public static final String MESSAGE_CANCEL_TYPE_PRIVATE = "0";//私聊
    public static final String MESSAGE_CANCEL_TYPE_GROUP = "1";//群聊
    public static final String MESSAGE_CANCEL_TYPE_MEET = "2";//会议

}
