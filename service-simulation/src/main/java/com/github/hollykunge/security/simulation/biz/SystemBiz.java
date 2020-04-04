package com.github.hollykunge.security.simulation.biz;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.simulation.entity.SystemInfo;
import com.github.hollykunge.security.simulation.entity.SystemUserMap;
import com.github.hollykunge.security.simulation.mapper.SystemInfoMapper;
import com.github.hollykunge.security.simulation.mapper.SystemUserMapMapper;
import com.github.hollykunge.security.simulation.vo.UserSystemsVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jihang
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class SystemBiz extends BaseBiz<SystemInfoMapper, SystemInfo> {

    @Resource
    private SystemUserMapMapper systemUserMapMapper;

    @Override
    protected String getPageName() {
        return null;
    }

    public List<UserSystemsVo> queryByUser(String userId) {
        //查找
        SystemUserMap example1 = new SystemUserMap();
        example1.setUserId(userId);
        List<SystemUserMap> systemUserMaps = systemUserMapMapper.select(example1);

        List<UserSystemsVo> userSystemsVos = new ArrayList<>();
        //填充
        for (SystemUserMap systemUserMap : systemUserMaps) {
            //查找一个
            SystemInfo systemInfo = mapper.selectByPrimaryKey(systemUserMap.getSystemId());
            if (systemInfo == null) {
                //TODO:报个错
                continue;
            }
            //拷贝主要属性
            UserSystemsVo userSystemsVo = new UserSystemsVo();
            BeanUtils.copyProperties(systemInfo, userSystemsVo);
            //创建者
            if ("1".equals(systemUserMap.getIsCreator())) {
                userSystemsVo.setIsCreator(true);
            } else {
                userSystemsVo.setIsCreator(false);
            }
            //时间格式转换
            userSystemsVo.setUpdTime(systemInfo.getUpdTime().toString());
            //参与人
            SystemUserMap example3 = new SystemUserMap();
            example3.setSystemId(systemUserMap.getSystemId());
            String users = StringUtils.join(systemUserMapMapper.select(example3).
                    stream().map(SystemUserMap::getUserId).toArray(), ",");
            userSystemsVo.setUsers(users);

            userSystemsVos.add(userSystemsVo);
        }
        //返回
        return userSystemsVos;
    }

    public void updateState(String id, String state) {
        SystemInfo systemInfo = new SystemInfo();
        systemInfo.setId(id);
        systemInfo.setState(state);
        mapper.updateByPrimaryKeySelective(systemInfo);
    }

    public String queryState(String id) {
        SystemInfo systemInfo = new SystemInfo();
        systemInfo.setId(id);
        return mapper.selectOne(systemInfo).getState();
    }
}
