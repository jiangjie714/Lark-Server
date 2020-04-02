package com.github.hollykunge.security.admin.api.authority;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author fansq
 * @since 20-3-23
 * @deprecation 门户统计功能前端信息展示实体
 */
@Data
public class PortalStatistics implements Serializable {
    /**
     * 总访问量（所有请求之和）
     */
    private Long totalAccess;
    /**
     * 日访问量（前一天的请求之和）
     */
    private Long dayAccess;
    /**
     * 总增长率（今年的请求之和-去年请求之和）/去年请求之和
     */
    private Double totalRate;
    /**
     * 日增长率（昨天请求之和-前天请求之和）/前天请求之和
     */
    private Double dayRate;
    /**
     * 登录量排行
     */
    private List<AccessNum> accessNums;
    /**
     * 消息量排行
     */
    private List<MessageNums> messageNums;
    /**
     * 文件量排行
     */
    private List<FileNum> fileNums;
    /**
     * 群组量排行
     */
    private List<GroupNum> groupNums;
    /**
     * 饼图（默认二院）
     */
    private List<SourceOrg> sourceOrg;
    /**
     * 散点图 点
     */
    private List<Node> nodes;
    /**
     * 散点图 线
     */
    private List<Link> links;
}
