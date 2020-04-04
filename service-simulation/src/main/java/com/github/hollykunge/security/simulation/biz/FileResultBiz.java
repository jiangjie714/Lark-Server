package com.github.hollykunge.security.simulation.biz;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.util.EntityUtils;
import com.github.hollykunge.security.common.util.SpecialStrUtils;
import com.github.hollykunge.security.simulation.entity.SystemInfo;
import com.github.hollykunge.security.simulation.entity.SystemUserMap;
import com.github.hollykunge.security.simulation.mapper.SystemInfoMapper;
import com.github.hollykunge.security.simulation.mapper.SystemUserMapMapper;
import com.github.hollykunge.security.simulation.vo.BothConfigVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.github.hollykunge.security.simulation.config.Constant.NAME_REGULATION_WRONG;
import static com.github.hollykunge.security.simulation.config.Constant.SUCCESS;

/**
 * @author jihang
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class FileResultBiz extends BaseBiz<SystemInfoMapper, SystemInfo> {

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

        return SUCCESS;
    }

    public int update(SystemInfo entity) {
        //检测
        if (SpecialStrUtils.check(entity.getSystemName())) {
            return NAME_REGULATION_WRONG;
        }
        EntityUtils.setUpdatedInfo(entity);
        mapper.updateByPrimaryKeySelective(entity);
        return SUCCESS;
    }

    public void getConfig(BothConfigVo entity) {
        System.out.println("从文件服务器请求" + entity.getFileName());
        // TODO
    }

}
