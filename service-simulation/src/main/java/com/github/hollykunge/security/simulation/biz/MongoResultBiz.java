package com.github.hollykunge.security.simulation.biz;

import com.alibaba.fastjson.JSON;
import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.util.EntityUtils;
import com.github.hollykunge.security.common.util.SpecialStrUtils;
import com.github.hollykunge.security.simulation.entity.SystemInfo;
import com.github.hollykunge.security.simulation.entity.SystemResult;
import com.github.hollykunge.security.simulation.entity.SystemUserMap;
import com.github.hollykunge.security.simulation.mapper.SystemInfoMapper;
import com.github.hollykunge.security.simulation.mapper.SystemUserMapMapper;
import com.github.hollykunge.security.simulation.pojo.*;
import com.github.hollykunge.security.simulation.vo.BothConfigVo;
import com.github.hollykunge.security.simulation.vo.MongoResultVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.github.hollykunge.security.simulation.config.Constant.NAME_REGULATION_WRONG;
import static com.github.hollykunge.security.simulation.config.Constant.SUCCESS;

/**
 * @author jihang
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class MongoResultBiz extends BaseBiz<SystemInfoMapper, SystemInfo> {

    @Autowired
    public MongoTemplate mongoTemplate;

    @Autowired
    private AssembleResultBiz assembleResultBiz;

    @Resource
    private SystemUserMapMapper systemUserMapMapper;

    @Override
    protected String getPageName() {
        return null;
    }

    public int create(SystemInfo entity) {

        //检测
        if (SpecialStrUtils.check(entity.getSystemName())) {
            return NAME_REGULATION_WRONG;
        }
        //装填
        SystemInfo systemInfo = new SystemInfo();
        BeanUtils.copyProperties(entity, systemInfo);
        EntityUtils.setCreatAndUpdatInfo(systemInfo);
        //插入
        mapper.insertSelective(systemInfo);

        //创建者
        SystemUserMap systemUserMap = new SystemUserMap();
        systemUserMap.setUserId(systemInfo.getCreatorPId());
        systemUserMap.setSystemId(systemInfo.getId());
        systemUserMap.setIsCreator("1");
        EntityUtils.setCreatAndUpdatInfo(systemUserMap);
        systemUserMapMapper.insertSelective(systemUserMap);
        //参与者
        List<String> userIds = entity.getUserList();
        for (String userId : userIds) {
            SystemUserMap map = new SystemUserMap();
            map.setUserId(userId);
            map.setSystemId(systemInfo.getId());
            map.setIsCreator("0");
            EntityUtils.setCreatAndUpdatInfo(map);
            systemUserMapMapper.insertSelective(map);
        }

        Nodes bus = new Nodes();
        bus.setShape("bus");
        bus.setType("bus");
        bus.setX(377.5);
        bus.setY(245.5);
        bus.setId("2efd998c");
        bus.setIndex(0);

        List<Nodes> nodes = new ArrayList<>();
        nodes.add(bus);

        FlowData flowData = new FlowData();
        flowData.setNodes(nodes);

        List<ActiveModels> activeModels = new ArrayList<>();
        List<ActiveDataTypes> activeDataTypes = new ArrayList<>();
        List<InterfaceList> interfaceList = new ArrayList<>();

        SystemResult systemResult = new SystemResult();
        systemResult.setSystemId(systemInfo.getId());
        systemResult.setFlowData(flowData);
        systemResult.setActiveModels(activeModels);
        systemResult.setActiveDataTypes(activeDataTypes);
        systemResult.setInterfaceList(interfaceList);
        mongoTemplate.save(systemResult);

        return SUCCESS;
    }

    public String retrieve(String systemId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("SYSTEM_ID").is(systemId));
        SystemResult systemResult = mongoTemplate.findOne(query, SystemResult.class);
        return JSON.toJSONString(systemResult);
    }

    public int update(MongoResultVo entity) {
        //检测
        if (SpecialStrUtils.check(entity.getSystemName())) {
            return NAME_REGULATION_WRONG;
        }

        SystemInfo systemInfo = new SystemInfo();
        systemInfo.setId(entity.getId());
        systemInfo.setSystemName(entity.getSystemName());
        systemInfo.setSystemDescribe(entity.getSystemDescribe());
        EntityUtils.setUpdatedInfo(systemInfo);
        mapper.updateByPrimaryKeySelective(systemInfo);

        Query query = new Query();
        query.addCriteria(Criteria.where("SYSTEM_ID").is(entity.getId()));
        Update update = new Update();
        update.set("FLOW_DATA", entity.getSystemResult().getFlowData());
        update.set("ACTIVE_MODELS", entity.getSystemResult().getActiveModels());
        update.set("ACTIVE_MODEL_TYPES", entity.getSystemResult().getActiveDataTypes());
        update.set("INTERFACE_LIST", entity.getSystemResult().getInterfaceList());
        mongoTemplate.updateFirst(query, update, SystemResult.class);

        return SUCCESS;
    }

    public org.jdom2.Document getConfig(BothConfigVo entity) {
        Query query = new Query();
        query.addCriteria(Criteria.where("SYSTEM_ID").is(entity.getId()));
        SystemResult systemResult = mongoTemplate.findOne(query, SystemResult.class);
        entity.setSystemResult(systemResult);

        org.jdom2.Document rootDocument = null;
        try {
            rootDocument = new org.jdom2.Document();
            assembleResultBiz.generateDocument(rootDocument, entity);
        } catch (Exception e) {
            // TODO Auto-generated catch block
        }
        return rootDocument;
    }

    public boolean isStruct(String systemId, String topic) {
        Query query = new Query();
        query.addCriteria(Criteria.where("SYSTEM_ID").is(systemId));
        SystemResult systemResult = mongoTemplate.findOne(query, SystemResult.class);
        assert systemResult != null;
        List<ActiveDataTypes> types = systemResult.getActiveDataTypes();
        for(ActiveDataTypes one : types) {
            if(one.getName().equals(topic)) {
                return true;
            }
        }
        return false;
    }
}
