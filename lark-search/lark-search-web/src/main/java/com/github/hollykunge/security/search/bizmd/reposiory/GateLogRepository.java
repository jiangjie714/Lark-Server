package com.github.hollykunge.security.search.bizmd.reposiory;

import com.github.hollykunge.security.search.bizmd.document.GateLogDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author: zhhongyu
 * @description: 网关日志es接口层
 * @since: Create in 15:14 2020/2/27
 */
@Component
public interface GateLogRepository extends ElasticsearchRepository<GateLogDocument, String> {
    List<GateLogDocument> findAllByPathCodeLikeAndCrtTimeBetween(String pathCode, long startTime, long endTime);
}
