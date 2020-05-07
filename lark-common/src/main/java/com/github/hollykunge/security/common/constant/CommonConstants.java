package com.github.hollykunge.security.common.constant;

import org.tio.utils.time.Time;

/**
 *
 * @author 协同设计小组
 * @date 2017/8/29
 */
public class CommonConstants {
    /**
     * 资源类型
     */
    public final static String RESOURCE_TYPE_MENU = "menu";
    public final static String RESOURCE_TYPE_BTN = "button";

    /**
     * 用户身份认证失败,token过期
     */
    public static final int EX_USER_TOKEN_EXPIRED = 40001;
    /**
     * token签名错误
     */
    public static final int EX_USER_TOKEN_SIGNATURE = 40002;
    /**
     * token为空或空串
     */
    public static final int EX_USER_TOKEN_INVALID = 40003;
    /**
     * 不符合密级约束条件 todo 密级异常未做处理
     */
    public static final int EX_SECRET_LEVELS = 40003;


    /**
     * 客户端token过期
     */
    public static final int EX_CLIENT_TOKEN_EXPIRED = 50031;
    /**
     * 客户端token签名错误
     */
    public static final int EX_CLIENT_TOKEN_SIGNATURE = 50032;
    /**
     * 客户端token为空或空串
     */
    public static final int EX_CLIENT_TOKEN_INVALID = 50033;
    /**
     * 拒绝执行当前的客户端请求
     * 没有配置服务调用表导致的
     */
    public static final int EX_CLIENT_FORBIDDEN_CODE = 50010;
    /**
     * 服务端处理异常
     */
    public static final int EX_SERVICE_INVALID_CODE = 50020;
    /**
     * mq消费者运行异常
     */
    public static final int EX_MQ_CONSUMER_BIZ = 50021;
    /**
     * feign接口调用异常
     */
    public static final int EX_FEIGN_EXECUT = 50030;
    /**
     * 其它未知异常
     */
    public static final int EX_OTHER_CODE = 50099;

    /**
     * 成功
     */
    public static final int HTTP_SUCCESS = 200;

    /**
     * 业务异常中的数据查询结果运行时导致异常
     */
    public static final int EX_BIZ_SERVER_DATABASE = 40011;
    public static final String EX_BIZ_SERVER_OHTER = "业务拓展异常";
    /**
     * 业务异常中的客户端输入参数导致异常
     */
    public static final int EX_BIZ_CLIENT_PARAMETER = 40022;
    /**
     * 业务异常中的客户端输入表单参数导致异常
     */
    public static final int EX_BIZ_FORM_PARAMETER = 40021;
    /**
     * 用户名或密码异常
     */
    public final static int EX_USER_PASS_INVALID_CODE = 40023;
    /**
     * 没有请求该资源的权限
     */
    public static final int URL_NOT_PERMISSION = 40031;
    /**
     * 没有请求该资源操作方法的权限
     */
    public static final int URL_METHOD_NOT_PERMISSION = 40032;



    public static final String CONTEXT_KEY_USER_ID = "currentUserId";
    public static final String CONTEXT_KEY_USERNAME = "currentUserName";
    public static final String CONTEXT_KEY_USER_NAME = "currentUser";
    public static final String CONTEXT_KEY_USER_TOKEN = "currentUserToken";
    public static final String JWT_KEY_USER_ID = "userId";
    public static final String JWT_KEY_NAME = "name";
    /**
     * 超级管理员adminUser默认密码
     */
    public static final String ADMIN_PASSWORD = "$2a$12$0pS6RoQtr2ASZyhpdUwmvO/VPBdYu.S.BFASQiJVo0ZblGrlqmABm";


    /** tio用ip数据监控统计，时间段*/
    public static final Long DURATION_1 = Time.MINUTE_1 * 5;
    public static final Long[] IPSTAT_DURATIONS = new Long[]{DURATION_1};

    public static final String WEB_USERHOST = "userHost";
    public static final String WEB_USERNAME = "userName";
    public static final String WEB_USERID = "userId";
    /**
     * 院网关身份姓名
     */
    public static final String PERSON_ID_ARG = "dnname";
    public static final String CLIENT_IP_ARG = "clientIp";
    /**
     * 院网关解码
     */
    public static final String PERSON_CHAR_SET = "ISO8859-1";


    public static final String GATE_LOG_REQUEST_BODY_PARAMS  = "requestBodyParam";
    /**
     * 获取请求体中的参数名，按顺序获取如果没有第一个，则第二个，以此类推
     */
    public static final String GET_GATE_LOG_REQUEST_BODY_RULE = "name,title";

    public static final String GATE_REQUEST_URI = "requestUri";

    public static final String GET_GATE_LOG_REQUEST_LIST = "all";

    public static final String GET_GATE_LOG_REQUEST_PAGE = "page";

    public static final String GET_GATE_LOG_REQUEST_EXPORT = "export";
    public static final String GET_GATE_LOG_REQUEST_FEIGN = "/front/info";
    public static final String AUTH_JWT_TOKEN = "/auth/jwt/token";

    /**
     * fansq 统计功能 时间
     */
    public static final String JIN_RI="jinri";
    public static final String BEN_ZHOU="benzhou";
    public static final String BEN_YUE="benyue";
    public static final String QUAN_BU="quanbu";

    public static final String JIN_RI_TYPE="5";
    public static final String BEN_ZHOU_TYPE="6";
    public static final String BEN_YUE_TYPE="7";
    public static final String QUAN_BU_TYPE="8";

    public static final String YESTERDAY="1";
    public static final String THIS_YEAR="2";
    public static final String LAST_YEAR="3";
    public static final String BEFOR_YESTERDAY ="4";

}
