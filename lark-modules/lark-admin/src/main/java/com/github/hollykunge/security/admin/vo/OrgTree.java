package com.github.hollykunge.security.admin.vo;

import com.github.hollykunge.security.common.vo.TreeNode;
import lombok.Data;

import java.util.Objects;

/**
 * @author holly
 */
@Data
public class OrgTree extends TreeNode implements Comparable<OrgTree>{

    private String label;
    private String name;
    private Long order;

    @Override
    public int compareTo(OrgTree o) {
        if(Objects.equals(o.getId(),id)){
            return 0;
        }
        if(Objects.equals(o.getOrder(),order)){
            return -1;
        }
        return order.compareTo(o.getOrder());
    }
}
