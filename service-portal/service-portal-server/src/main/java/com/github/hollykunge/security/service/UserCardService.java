package com.github.hollykunge.security.service;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.exception.auth.FrontInputException;
import com.github.hollykunge.security.common.exception.service.ClientParameterInvalid;
import com.github.hollykunge.security.common.exception.service.DatabaseDataException;
import com.github.hollykunge.security.common.util.UUIDUtils;
import com.github.hollykunge.security.entity.CardInfo;
import com.github.hollykunge.security.entity.User;
import com.github.hollykunge.security.entity.UserCard;
import com.github.hollykunge.security.mapper.CardInfoMapper;
import com.github.hollykunge.security.mapper.UserCardMapper;
import com.github.hollykunge.security.utils.ListUtil;
import com.github.hollykunge.security.vo.UserCardVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户卡片业务层
 * @author zhhongyu
 * @since 2019-06-11
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserCardService extends BaseBiz<UserCardMapper, UserCard> {
    @Autowired
    private CardService cardService;
    @Value("${portal.card.w}")
    private int cardWith;

    @Value("${portal.card.h}")
    private int cardHeight;

    @Resource
    private CardInfoMapper cardInfoMapper;

    /**
     *fansq 19-12-24 注入mapper
     */
    @Resource
    private UserCardMapper userCardMapper;

    @Override
    protected String getPageName() {
        return null;
    }

    /**
     * fansq
     * 19-12-24
     * 操作用户卡片关系表
     * @param userCardList
     */
    public void insertUserCards(List<UserCard> userCardList,List<User> userList,String uuid){
        for (User user:userList) {
            UserCard userCard= new UserCard();
            userCard.setUserId(user.getId());
            userCard.setI(mapper.selectCount(userCard)+1+"");
            userCard .setCardId(uuid);
            userCard.setId(UUIDUtils.generateShortUuid());
            userCard.setStatus("1");
            userCardList.add(userCard);
        }
        Map<Integer,List<UserCard>> map = new ListUtil<UserCard>().batchList(userCardList,500);
        for(Integer integer:map.keySet()){
            userCardMapper.insertUserCards(map.get(integer));
        }
    }

    /**
     * fansq
     * 19-12-24
     * 操作用户卡片关系表 update
     */
    public void updateUserCards(List<User> userList,List<UserCard> userCardList,CardInfo cardDto){
        for (User user:userList) {
            UserCard userCard= new UserCard();
            userCard.setUserId(user.getId());
            userCard.setI(mapper.selectCount(userCard)+1+"");
            userCard .setCardId(cardDto.getId());
            userCard.setId(UUIDUtils.generateShortUuid());
            userCard.setStatus("1");
            userCardList.add(userCard);
        }
        this.deleteUserCards(cardDto.getId(),userCardList);
        Map<Integer,List<UserCard>> map = new ListUtil<UserCard>().batchList(userCardList,500);
        for(Integer integer:map.keySet()){
            userCardMapper.insertUserCards(map.get(integer));
        }
    }

    /**
     * fansq
     * 19-12-24
     * 更新删除
     * @param cardId
     * @param userCardList
     */
    public void deleteUserCards(String cardId,List<UserCard> userCardList){
        Map<Integer,List<UserCard>> map = new ListUtil<UserCard>().batchList(userCardList,500);
        for(Integer integer:map.keySet()){
            userCardMapper.deleteUserCards(cardId,map.get(integer));
        }
    }
    @Override
    public void insertSelective(UserCard entity) {
        //用userId查询已经添加卡片的数量
        UserCard userParams = new UserCard();
        userParams.setUserId(entity.getUserId());
        entity.setI( mapper.selectCount(userParams)+1+"");
        entity.setId(UUIDUtils.generateShortUuid());
        entity.setStatus("1");
        mapper.insertSelective(entity);
    }


    /**
     * 根据用户id获取要显示
     * @param userId
     * @return
     */
    public List<UserCardVO> userCards(String userId){
        List<UserCardVO> result = new ArrayList<>();
        UserCard userCard = new UserCard();
        userCard.setUserId(userId);
        List<UserCard> userCards = mapper.select(userCard);
        userCards.forEach(userCardEntity ->{
            UserCardVO userCardVO = new UserCardVO();
            if(StringUtils.isEmpty(userCardEntity.getCardId())){
                throw new DatabaseDataException("卡片id信息异常。");
            }
            CardInfo card = cardService.selectById(userCardEntity.getCardId());
            BeanUtils.copyProperties(userCardEntity,userCardVO);
            if(card != null){
                userCardVO.setTitle(card.getTitle());
                userCardVO.setUrl(card.getUrl());
                userCardVO.setId(card.getId());
                userCardVO.setDefaultChecked(true);
                userCardVO.setType(card.getType());
            }
            result.add(userCardVO);
        });
        return result;
    }

    public List<UserCardVO> allCard(String userId){
        CardInfo temp = new CardInfo();
        temp.setStatus("1");
        temp.setDeleted("0");
        List<CardInfo> cardInfos = cardInfoMapper.select(temp);
        UserCard userCard = new UserCard();
        userCard.setUserId(userId);
        List<UserCard> userCards = mapper.select(userCard);
        List<UserCardVO> result = this.setDefaultChecked(cardInfos,userCards);
        return result;
    }

    private List<UserCardVO> setDefaultChecked(List<CardInfo> cardInfos,List<UserCard> userCards){
        List<UserCardVO> result = new ArrayList<>();
        cardInfos.forEach(cardInfo -> {
            UserCardVO userCardVO = new UserCardVO();
            BeanUtils.copyProperties(cardInfo,userCardVO);
            if(StringUtils.isEmpty(cardInfo.getId())){
                throw new DatabaseDataException("卡片信息异常。");
            }
            boolean isContains = userCards.stream().
                    anyMatch(userCard -> cardInfo.getId().equals(userCard.getCardId()));
            if(isContains){
                userCardVO.setDefaultChecked(true);
            }else{
                userCardVO.setDefaultChecked(false);
            }
            result.add(userCardVO);
        });
        return result;
    }

    public void modifyUserCards(UserCard userCard){
        if(StringUtils.isEmpty(userCard.getCardId())){
            throw new ClientParameterInvalid("要修改的卡片id为空。");
        }
        UserCard param = new UserCard();
        BeanUtils.copyProperties(userCard,param);
        param.setI(null);
        param = mapper.selectOne(param);
        if(param == null){
            throw new DatabaseDataException("没有找到对应的用户卡片，可能被删除了。");
        }
        userCard.setId(param.getId());
        mapper.updateByPrimaryKeySelective(userCard);
    }
}
