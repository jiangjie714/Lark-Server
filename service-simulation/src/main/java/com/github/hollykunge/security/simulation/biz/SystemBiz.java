package com.github.hollykunge.security.simulation.biz;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.util.EntityUtils;
import com.github.hollykunge.security.common.util.SpecialStrUtils;
import com.github.hollykunge.security.simulation.entity.SimuSystem;
import com.github.hollykunge.security.simulation.entity.SystemUserMap;
import com.github.hollykunge.security.simulation.mapper.SimuSystemMapper;
import com.github.hollykunge.security.simulation.mapper.SystemUserMapMapper;
import com.github.hollykunge.security.simulation.vo.UserSystemsVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
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
public class SystemBiz extends BaseBiz<SimuSystemMapper, SimuSystem> {

    @Resource
    private SystemUserMapMapper systemUserMapMapper;
    @Resource
    private SimuSystemMapper simuSystemMapper;

    @Override
    protected String getPageName() {
        return null;
    }

    public int createSystem(SimuSystem entity) {
        //检测
        if (SpecialStrUtils.check(entity.getSystemName())) {
            return NAME_REGULATION_WRONG;
        }
        //装填
        SimuSystem simuSystem = new SimuSystem();
        BeanUtils.copyProperties(entity, simuSystem);
        EntityUtils.setCreatAndUpdatInfo(simuSystem);
        //插入
        mapper.insertSelective(simuSystem);

        //创建者
        SystemUserMap systemUserMap = new SystemUserMap();
        systemUserMap.setUserId(simuSystem.getCreatorPId());
        systemUserMap.setSystemId(simuSystem.getId());
        systemUserMap.setIsCreator("1");
        EntityUtils.setCreatAndUpdatInfo(systemUserMap);
        systemUserMapMapper.insertSelective(systemUserMap);
        //参与者
        List<String> userIds = entity.getUserList();
        for (String userId : userIds) {
            SystemUserMap map = new SystemUserMap();
            map.setUserId(userId);
            map.setSystemId(simuSystem.getId());
            map.setIsCreator("0");
            EntityUtils.setCreatAndUpdatInfo(map);
            systemUserMapMapper.insertSelective(map);
        }

        return SUCCESS;
    }

    public int updateSystem(SimuSystem entity) {
        //检测
        if (SpecialStrUtils.check(entity.getSystemName())) {
            return NAME_REGULATION_WRONG;
        }
        EntityUtils.setUpdatedInfo(entity);
        mapper.updateByPrimaryKeySelective(entity);
        return SUCCESS;
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
            SimuSystem simuSystem = mapper.selectByPrimaryKey(systemUserMap.getSystemId());
            if (simuSystem == null) {
                //TODO:报个错
                continue;
            }
            //拷贝主要属性
            UserSystemsVo userSystemsVo = new UserSystemsVo();
            BeanUtils.copyProperties(simuSystem, userSystemsVo);
            //创建者
            if ("1".equals(systemUserMap.getIsCreator())) {
                userSystemsVo.setIsCreator(true);
            } else {
                userSystemsVo.setIsCreator(false);
            }
            //时间格式转换
            userSystemsVo.setUpdTime(simuSystem.getUpdTime().toString());
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
        SimuSystem simuSystem = new SimuSystem();
        simuSystem.setId(id);
        simuSystem.setState(state);
        simuSystemMapper.updateByPrimaryKeySelective(simuSystem);
    }

    public String queryState(String id) {
        SimuSystem simuSystem = new SimuSystem();
        simuSystem.setId(id);
        return simuSystemMapper.selectOne(simuSystem).getState();
    }
}
