package com.github.hollykunge.security.service;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.entity.CardInfo;
import com.github.hollykunge.security.entity.UserCard;
import com.github.hollykunge.security.mapper.CardInfoMapper;
import com.github.hollykunge.security.mapper.UserCardMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 卡片主表信息业务类
 * @author zhhongyu
 * @since 2019-06-11
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CardService extends BaseBiz<CardInfoMapper, CardInfo> {

    @Resource
    private UserCardMapper userCardMapper;

    @Resource
    private CardInfoMapper cardInfoMapper;
    @Override
    protected String getPageName() {
        return null;
    }

    @Override
    public void insertSelective(CardInfo entity) {
        cardInfoMapper.insertSelective(entity);
    }

    @Override
    public void updateSelectiveById(CardInfo entity) {
        if("1".equals(entity.getDeleted())||"0".equals(entity.getStatus())||"0".equals(entity.getCardOrgUserStatus())){
            UserCard userCard= new UserCard();
            userCard.setCardId(entity.getId());
            userCardMapper.delete(userCard);
            entity.setCardOrgUserStatus("0");
        }
        super.updateSelectiveById(entity);
    }
}
