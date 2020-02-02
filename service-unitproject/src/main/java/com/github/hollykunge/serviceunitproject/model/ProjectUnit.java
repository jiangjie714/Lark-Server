package com.github.hollykunge.serviceunitproject.model;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


import javax.persistence.Table;



@Data
@NoArgsConstructor
@ToString
@Table(name = "projectunitinfo")
public class ProjectUnit {
    private String id;
    private String name;
    private String code;
    private String description;
    private String parentId;
    private String cseq;
    private String enabled;
    private String createtime;
    private String creator;
    private String updatetime;
    private String updator;
    private String orgId;
    private String dPid;
    private String type;
    private String armyId;
    private String model;
    private String modelSimpleName;
    private String manageOrg;
    private String orgLeader;
    private String techManager;
    private String projectManager;
    private String hallLeader;
    private String partaskOrgId;
    private String unitLevelId;
    private String unitClassifyId;
    private String productClassifyId;
    private String prevNameId;
    private String aliasName;
    private String modelShort;
    private String partOrgId;
    private String orgUserId;
    private String partaskOrgUserId;
    private String tempId;
    private String domainId;
    private String productClassify;
    private String majorClassify;
    private String generalUnit;
    private String refId;
    private String ddOrgId;
    private String ddOrgUserId;
    private String ddPartaskUserId;
    private String belongModel;
    private String secretLevel;
    private String masterRefId;
    private String superior;
    private String subordinate;
    private String relatedUserId;
    private String teamType;

}
