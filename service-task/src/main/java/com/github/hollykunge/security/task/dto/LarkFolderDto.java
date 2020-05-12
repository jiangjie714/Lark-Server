package com.github.hollykunge.security.task.dto;

import com.github.hollykunge.security.task.entity.LarkFile;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author fansq
 * @since 20-5-11
 * @deprecation 用于给前端嵌套返回文件夹和文件夹下文件
 */
@Data
public class LarkFolderDto implements Serializable {

    private String code;

    private String name;

    private String organizationCode;

    private String taskCode;

    private String projectCode;

    private String deleted;

    private String id;

    private Date crtTime;

    private String crtUser;

    private String crtName;

    private String crtHost;

    private Date updTime;

    private String updUser;

    private String updName;

    private String updHost;

    private String attr1;

    private String attr2;

    private String attr3;

    private String attr4;

    private List<LarkFile> larkFiles;
}
