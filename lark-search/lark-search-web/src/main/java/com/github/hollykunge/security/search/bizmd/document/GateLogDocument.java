package com.github.hollykunge.security.search.bizmd.document;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author: zhhongyu
 * @description: 网关日志document
 * @since: Create in 15:10 2020/2/27
 */
@Data
@Document(indexName = "larkgate", type = "gatelog")
public class GateLogDocument {
    @Id
    private String id;
    private String crtName;
    private String opt;
    private String isSuccess;
    private String mongodbCollection;
    private String menu;
    private String ksendFlag;
    private String uri;
    private String optInfo;
    private Date crtTime;
    private String pid;
    private String crtUser;
    private String crtHost;
    private String pathCode;

}
