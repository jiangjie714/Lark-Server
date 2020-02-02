package com.github.hollykunge.security.admin.vo;

import lombok.Data;

/**
 * @author fansq
 * @since 10-11-26
 * 用于组织信息表所有信息
 */
@Data
public class OrgTreeAll extends OrgTree implements Comparable<OrgTree>{

    private Integer level;
    private String pathCode;
    private String pathName;

    @Override
    public int compareTo(OrgTree o) {
        return super.compareTo(o);
    }
}
