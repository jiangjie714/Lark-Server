package com.github.hollykunge.security.search.bizmd.service.impl;

import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.search.bizmd.document.GateLogDocument;
import com.github.hollykunge.security.search.bizmd.reposiory.GateLogRepository;
import com.github.hollykunge.security.search.bizmd.service.GateLogService;
import com.github.hollykunge.security.search.dto.GateLogDto;
import com.github.hollykunge.security.search.service.BaseService;
import com.github.hollykunge.security.search.utils.ListUtis;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author: zhhongyu
 * @description: 网关日志业务实现
 * @since: Create in 15:57 2020/3/16
 */
@Service
public class GateLogServiceImpl extends BaseService implements GateLogService {
    @Autowired
    private GateLogRepository gateLogRepository;
    @Override
    public List<GateLogDto> page(Query query)throws Exception{
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        setPageLikeCondition(query,builder);
        int pageNo = query.getPageNo() == 0 ? 0:query.getPageNo()-1;
        int pageSize = query.getPageSize() == 0 ? 10:query.getPageSize();
        Pageable pageable = new QPageRequest(pageNo, pageSize);
        Page<GateLogDocument> page = gateLogRepository.search(builder, pageable);
        List<GateLogDocument> content = page.getContent();
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        List<GateLogDto> mapAsList = mapperFactory.getMapperFacade().mapAsList(content, GateLogDto.class);
        return mapAsList;
    }
    @Override
    public List<GateLogDto> all()throws Exception{
        Iterable<GateLogDocument> all = gateLogRepository.findAll();
        List<GateLogDocument> list = ListUtis.transferList(all);
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        List<GateLogDto> mapAsList = mapperFactory.getMapperFacade().mapAsList(list, GateLogDto.class);
        return mapAsList;
    }

}
