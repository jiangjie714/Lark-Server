package com.github.hollykunge.security.constants;

import org.tio.utils.time.Time;

public interface Constants {
    /**
     * 用于群聊的group id
     */
    String GROUP_ID = "showcase-websocket";
    /**
     * tio用ip数据监控统计，时间段
     */
    Long DURATION_1 = Time.MINUTE_1 * 5;
    Long[] IPSTAT_DURATIONS = new Long[]{DURATION_1};

    String ROLE_ID = "1";
    /**
     * fansq 19/12/11
     * 常用链接 portalOrgUserStatus 值
     */
    String PORTALORGUSERSTATUSZERO = "0";
    /**
     * fansq 19/12/11
     * 常用链接 portalOrgUserStatus 值
     */
    String PORTALORGUSERSTATUSONE = "1";

    /**
     * fansq
     * 19-12-24
     * 卡片 默认值
     */
    String CARDORGUSERSTATUSZERO ="0";
    String CARDLORGUSERSTATUSONE = "1";
}
