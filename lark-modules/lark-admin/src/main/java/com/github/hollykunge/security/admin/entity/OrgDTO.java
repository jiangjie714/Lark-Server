package com.github.hollykunge.security.admin.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author fansq
 * 递归查询组织信息
 */
@Data
public class OrgDTO extends Org implements Serializable {
    private List<OrgDTO> orgDTOList;
}
