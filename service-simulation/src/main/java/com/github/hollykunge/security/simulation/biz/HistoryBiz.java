package com.github.hollykunge.security.simulation.biz;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.simulation.entity.SystemInfo;
import com.github.hollykunge.security.simulation.mapper.SystemInfoMapper;
import com.github.hollykunge.security.simulation.vo.HistoryInfoVo;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional(rollbackFor = Exception.class)
public class HistoryBiz extends BaseBiz<SystemInfoMapper, SystemInfo> {

    @Autowired
    public MongoTemplate mongoTemplate;

    @Override
    protected String getPageName() {
        return null;
    }

    public List<String> systemHistory(String systemId) {
        Set<String> collectionSet = mongoTemplate.getCollectionNames();
        collectionSet.remove("SIMU_SYSTEM_RESULT");
        List<String> histories = new ArrayList<>();
        for (String collection : collectionSet) {
            String[] array = collection.split("@");
            if (array[0].equals(systemId)) {
                histories.add(array[1]);
            }
        }
        return histories;
    }

    public HistoryInfoVo oneHistoryInfo(String name) {
        HistoryInfoVo hi = new HistoryInfoVo();
        hi.setStartTime(0.01);
        hi.setStopTime(10.06);
        return hi;
    }
}
