package com.github.hollykunge.security.admin.biz;

import com.github.hollykunge.security.admin.constant.AdminCommonConstant;
import com.github.hollykunge.security.admin.entity.Menu;
import com.github.hollykunge.security.admin.mapper.MenuMapper;
import com.github.hollykunge.security.common.biz.BaseBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 协同设计小组
 * @create 2017-06-12 8:48
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class MenuBiz extends BaseBiz<MenuMapper, Menu> {

    @Override
    public List<Menu> selectListAll() {
        return super.selectListAll();
    }

    @Override
    public void insertSelective(Menu entity) {
        super.insertSelective(entity);
    }

    @Override
    public void updateById(Menu entity) {
        if (AdminCommonConstant.ROOT.equals(entity.getParentId())) {
            entity.setPath("/" + entity.getCode());
        } else {
            Menu parent = this.selectById(entity.getParentId());
            entity.setPath(parent.getPath() + "/" + entity.getCode());
        }
        super.updateById(entity);
    }

    @Override
    public void deleteById(String id) {
        super.deleteById(id);
    }

    @Override
    public void updateSelectiveById(Menu entity) {
        super.updateSelectiveById(entity);
    }

    @Override
    protected String getPageName() {
        return null;
    }

    /**
     * 获取用户可以访问的菜单
     *
     * @param id 用户id
     * @return
     */
    public List<Menu> getUserAuthorityMenuByUserId(String id) {
        return null;
        //TODO: return mapper.selectAuthorityMenuByUserId(id);
    }

    /**
     * 根据用户获取可以访问的系统
     *
     * @param id
     * @return
     */
    public List<Menu> getUserAuthoritySystemByUserId(String id) {
        return null;
        //TODO: return mapper.selectAuthoritySystemByUserId(id);
    }
}
