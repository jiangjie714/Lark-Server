package com.github.hollykunge.security.simulation.config;

public class Constant {

    public static final int SUCCESS = 599;

    /**
     * 名称不符合规范
     */
    public static final int NAME_REGULATION_WRONG = 600;

    /**
     * 名称重复
     */
    public static final int NAME_REPLICATE = 601;

    /**
     * 状态机
     */
    public static final String UNREADY = "system_unready";
    public static final String PREPARING = "system_preparing";
    public static final String RUNNING = "system_running";
    public static final String PAUSING = "system_pausing";


    /**
     * 动态库回复码
     */
    public static final String RT_EXCEPTION = "-2";
    public static final String RT_ERROR = "-1";
    public static final String RT_WARNING = "0";
    public static final String RT_SUCCESS = "1";

    /**
     * 根路径
     */
    public static final String BASE_PATH = "D:\\ospl\\";
    public static final String CONFIG_PATH = BASE_PATH + "lark_simu_config\\";

}
