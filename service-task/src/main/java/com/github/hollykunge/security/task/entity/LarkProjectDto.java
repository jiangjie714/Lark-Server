package com.github.hollykunge.security.task.entity;

import com.github.hollykunge.security.common.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author  fansq
 * @since 20-4-15
 * 用于给前端返回 项目具体信息
 * 增加项目拥有者 信息
 * @deprecation
 */
@Data
public class LarkProjectDto extends LarkProject {


    private String projectUserId;
    private String projectUserName;
    private String projectUserOrgCode;
}