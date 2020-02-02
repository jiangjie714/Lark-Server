package com.github.hollykunge.security.gate.utils;
/**
 * @author: zhhongyu
 * @description: 接口操作字典enum
 * @since: Create in 10:13 2019/9/11
 */
public enum GateLogRestOptionEnum {
    ELEMENT_REST("按钮","element"),
    GATELOG_REST("日志列表","gateLog"),
    MENU_REST("菜单管理","menu"),
    NOTICE_REST("公告信息","notice"),
    ORG_REST("组织管理","org"),
    ROLE_REST("角色管理","role"),
    USER_REST("用户管理","user"),
    CARD_REST("卡片管理","card"),
    COMMONTOOL_REST("常用工具","commonTools"),
    FEEDBACK_REST("问题反馈","feedback"),
    HOTMAP_REST("工作热力图卡片","hotmap"),
    PORTAL_MESSAGE_REST("消息提醒卡片","message"),
    PORTAL_NOTICE_REST("公告通知卡片","potalNotice"),
    PORTAL_USER_CARD_REST("卡片设置","userCard"),
    PORTAL_USER_COMMONTOOLS_REST("常用工具设置","userCommonTools"),
    SPECIAL_REST_LOGIN_USERINFO("获取本人用户信息，本人姓名","/front/info"),
    EXPORT("导出日志","export"),
    SEND_ONE("发布一条","send"),
    CONTACT_PERSON("最近联系人","orgUsers"),
    PERMISSON("菜单权限","permission"),
    PORTAL_MYSELF("本人信息","myself"),
    ALL_LIST("全部数据","all"),
    PAGE_LIST("分页数据","page"),
    TREE("树","tree"),
    OTHER("","other"),
    MOVE("位置移动","move"),
    ;
    private final String name;
    private final String value;
    
    private GateLogRestOptionEnum(String name, String value){
        this.name = name;
        this.value = value;
    }
    public static GateLogRestOptionEnum getEnumByValue(String value){
        if(null == value){
            return OTHER;
        }
        for(GateLogRestOptionEnum temp: GateLogRestOptionEnum.values()){
            if(temp.getValue().equals(value)){
                return temp;
            }
        }
        return OTHER;
    }
    public String getName() {
        return name;
    }
    public String getValue() {
        return value;
    }
}