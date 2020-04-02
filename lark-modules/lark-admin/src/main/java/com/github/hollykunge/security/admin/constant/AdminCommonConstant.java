package com.github.hollykunge.security.admin.constant;

/**
 * ${DESCRIPTION}
 *
 * @author 协同设计小组
 * @create 2017-06-17 14:41
 */
public class AdminCommonConstant {
    public final static String ROOT = "root";
    public final static int DEFAULT_GROUP_TYPE = 0;
    /**
     * 权限关联类型
     */
    public final static String AUTHORITY_TYPE_GROUP = "group";
    /**
     * 权限资源类型
     */
    public final static String RESOURCE_TYPE_MENU = "menu";
    public final static String RESOURCE_TYPE_BTN = "button";

    public final static String RESOURCE_REQUEST_METHOD_GET = "GET";
    public final static String RESOURCE_REQUEST_METHOD_PUT = "PUT";
    public final static String RESOURCE_REQUEST_METHOD_DELETE = "DELETE";
    public final static String RESOURCE_REQUEST_METHOD_POST = "POST";

    public final static String RESOURCE_ACTION_VISIT = "访问";

    public final static String BOOLEAN_NUMBER_FALSE = "0";

    public final static String BOOLEAN_NUMBER_TRUE = "1";
    /**
     * 通知死信队列交换机名称
     */
    public final static String NOTICE_DEAD_EXCHANGENAME = "notice_dead_exchange";
    /**
     * 通知死信队列路由键
     */
    public final static String NOTICE_DEAD_ROUTING_KEY = "notice_dead_routing_key";


    /**
     * 死信队列 交换机标识符
     */
    public static final String DEAD_LETTER_QUEUE_KEY = "x-dead-letter-exchange";
    /**
     * 死信队列交换机绑定键标识符
     */
    public static final String DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";
    /**
     * 创建角色时默认给权限的三个菜单
     */
    public static final String DEFAULT_MENU_PERMISSION_CODE_LIST = "talk,dashboard,login,self";

    public static final String NO_DATA_ORG_CODE = "0010";


    /**
     *     三元角色
     */
//    安全管理员
    public static final String SECURITY_USER = "安全保密员";
//    日志审计员
    public static final String LOG_USER = "安全审计员";
//    系统管理员
    public static final String SYSTEM_USER = "系统管理员";
    /**
     * 系统超级管理员id
     */
    public static final String SUPER_SYSTEM_ID = "1";
    /**
     * 院机关编码
     */
    public static final String ORG_CODE_INSTITUTE_ORGAN = "001000";
    public static final String ORG_CODE_HANGTIANKEGONG = "99999";

    public static final String CACHE_KEY_RPC_USER = "rpc:user";

    public static final String CONTACT_FLAG_ORGNODE = "orgNode";
    public static final String CONTACT_FLAG_USERNODE = "userNode";

    /**
     *fansq 19/11/25
     * 组织管理  添加接口  是否删除默认值
     */
    public static final String ORG_DELETED_CODE="0";
    public static final String ORG_PATH_CODE=".";
    public static final String ORG_PATH_NAME="/";
    public static final String USER_AVATAR = "group1/M00/00/01/CgsYil1pbGyAcT01AAAWQzJ2f5880.jpeg";
    public static final String USER_LEVEL = "40";
    //EXCEL 导入用户默认角色
    public static final String USER_ROLE_DEFAULT = "izuhPB1h";
    //EXCEL 导入用户默认权限
    public static final String USER_POSITION_DEFAULT = "0";
    //EXCEL 导入用户默认是否删除
    public static final String USER_DELETED_DEFAULT = "0";
    //EXCEL 导入用户默认密码
    public static final String USER_PASSWORD_DEFAULT = "123456";

    //建研究室内群岗位编号
    public static final String USER_POSITION_ROOM_INNER = "0";
    //建跨研究室群岗位编号
    public static final String USER_POSTTION_ROOM_OUTTER = "1";
    //建跨厂所群群岗位编号
    public static final String USER_POSITION_INSTITUTES_OUTTER = "2";

    //普通用户角色ID（内网）
    public static final String USER_ROLE_ORDINARY = "CSyzMUCE";
    /**
     * fansq 19/11/25
     * 常用链接 portalOrgUserStatus 默认值
     * 卡片操作 car 默认值
     */
    public static final String PORTALORGUSERSTATUS = "1";

    public static final String ENCRYPTION_PASSWORD = "$2a$12$IyWcD6rXNNWAzGPQGTbO.O5azKOm7QPcInF4QcYyml4MA.C5jXkPy";
}
