package com.github.hollykunge.serviceunitproject.common;

import com.github.hollykunge.security.common.vo.TreeNode;
//import com.github.hollykunge.serviceunitproject.common.TreeNode;
import lombok.Data;

import java.util.Objects;

/**
 * @author guxq
 */
@Data
public class ProjectUnitTree extends TreeNode implements Comparable<ProjectUnitTree>{

    private String label;
    private String name;
    private Long order;
    private String type;
    private String cseq;
    private String enabled;
    private String orgUserId;
    private String partaskOrgUserId;
    private String ddOrgUserId;
    private String ddPartaskUserId;
    /*private String orgUserId;
    private String partaskOrgUserId;
    private String ddOrgUserId;
    private String ddPartaskUserId;*/
    @Override
    public int compareTo(ProjectUnitTree o) {
        if(Objects.equals(o.getId(),id)){
            return 0;
        }
        if(Objects.equals(o.getOrder(),order)){
            return -1;
        }
        return order.compareTo(o.getOrder());
    }
}
