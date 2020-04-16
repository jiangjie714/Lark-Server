package com.github.hollykunge.security.service;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.constants.Constants;
import com.github.hollykunge.security.entity.CommonTools;
import com.github.hollykunge.security.entity.UserCommonTools;
import com.github.hollykunge.security.mapper.CommonToolsMapper;
import com.github.hollykunge.security.mapper.UserCommonToolsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * 常用工具业务
 * @author zhhongyu
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CommonToolsService extends BaseBiz<CommonToolsMapper, CommonTools> {
    @Resource
    private UserCommonToolsMapper userCommonToolsMapper;
    @Override
    protected String getPageName() {
        return null;
    }

    @Override
    public void insertSelective(CommonTools entity) {
        if(StringUtils.isEmpty(entity.getOrgCode())){
            throw new BaseException("组织代码不能为空...");
        }
        //entity.setId(UUIDUticls.generateShortUuid());
        //添加时状态默认为1
        entity.setStatus("1");
        mapper.insertSelective(entity);
    }

    /**
     * fansq
     * 19-12-12
     * 添加 Constants.PORTALORGUSERSTATUSONE.equal (entity.getPortalOrgUserStatus())
     */
    @Override
    public void updateSelectiveById(CommonTools entity) {
        if(StringUtils.isEmpty(entity.getId())){
            throw new BaseException("常用工具id不能为空...");
        }
        //如果做停用常用工具，需要将用户设置好的用户常用工具删除掉
        if("0".equals(entity.getStatus()) || Constants.PORTALORGUSERSTATUSZERO.equals(entity.getPortalOrgUserStatus())){
            UserCommonTools userCommonTools = new UserCommonTools();
            userCommonTools.setToolId(entity.getId());
            userCommonToolsMapper.delete(userCommonTools);
        }
        super.updateSelectiveById(entity);
    }
}
