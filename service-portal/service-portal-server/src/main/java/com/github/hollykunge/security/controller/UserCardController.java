package com.github.hollykunge.security.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.exception.auth.UserTokenException;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import com.github.hollykunge.security.entity.CardInfo;
import com.github.hollykunge.security.entity.UserCard;
import com.github.hollykunge.security.feign.IUserService;
import com.github.hollykunge.security.service.CardService;
import com.github.hollykunge.security.service.UserCardService;
import com.github.hollykunge.security.vo.UserCardVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户卡片接口
 * @author zhhongyu
 * @since 2019-06-11
 */
@RestController
@RequestMapping("card")
public class UserCardController extends BaseController<UserCardService, UserCard> {

    @Autowired
    private CardService cardService;
    /**
     * todo:使用
     * 给用户设置卡片接口
     * @param userCard 卡片实体类
     * @return
     */
    @Override
    @RequestMapping(value = "/collection", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<UserCard> add(@RequestBody UserCard userCard) {
        String userId = request.getHeader("userId");
        if(StringUtils.isEmpty(userId)){
            throw new UserTokenException("请求中不包含用户信息。");
        }
        userCard.setUserId(userId);
        // 非幂等问题不用加多余判断
       if(baseBiz.selectCount(userCard) > 0){
           return new ObjectRestResponse<>().rel(false).msg("设置卡片失败，已经添加过了。");
       }
        return super.add(userCard).rel(true).msg("设置成功。");
    }

    /**
     * todo:使用
     * 用户删除显示卡片
     * @param cardId 卡片id
     * @return
     */
    @RequestMapping(value = "/myself", method = RequestMethod.DELETE)
    @ResponseBody
    public ObjectRestResponse<CardInfo> removeUserCard(@RequestParam String cardId) {
        String userID =  request.getHeader("userId");
        if(StringUtils.isEmpty(userID)){
            throw new BaseException("request contains no user...");
        }
        UserCard userCard = new UserCard();
        userCard.setUserId(userID);
        userCard.setCardId(cardId);
        if(baseBiz.selectCount(userCard) == 0){
            return new ObjectRestResponse<>().msg("用户不显示该卡片,不需要移除...");
        }
        CardInfo cardInfo = cardService.selectById(cardId);
        baseBiz.delete(userCard);
        return new ObjectRestResponse<>().msg("移除成功..").rel(true).data(cardInfo);
    }

    /**
     * todo:使用
     * 获取用户要展示的卡片
     * @return
     */
    @RequestMapping(value = "/myself", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<UserCardVO>> userCards(HttpServletRequest request) {
        String userID =  request.getHeader("userId");
        if(StringUtils.isEmpty(userID)){
            throw new BaseException("request contains no user...");
        }
        List<UserCardVO> userCardVOS = baseBiz.userCards(userID);
        return new ListRestResponse("",userCardVOS.size(),userCardVOS);
    }

    /**
     * todo:使用
     * 用户改变卡片的位置
     * fansq -20-1-9
     * 修改map类型 因为id变化
     * @return
     */
    @RequestMapping(value = "/myself/move", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse modifyUserCards(@RequestParam String data,HttpServletRequest request) {
        if(StringUtils.isEmpty(data)){
            throw new BaseException("参数为null...");
        }
        String[] datas = data.split(",");
        for (String param: datas) {
            param = "{"+param+"}";
            Map<Object,Integer> map = JSONObject.parseObject(param,Map.class);
            if(map == null){
                throw new BaseException("接口参数不能被转为map...");
            }
            String userID =  request.getHeader("userId");
            if(StringUtils.isEmpty(userID)){
                throw new BaseException("request contains no user...");
            }
            Set<Object> sets = map.keySet();
            for (Object temp : sets) {
                UserCard userCard = new UserCard();
                userCard.setUserId(userID);
                userCard.setCardId(temp+"");
                userCard.setI(map.get(temp)+"");
                baseBiz.modifyUserCards(userCard);
            }
        }
        return new ObjectRestResponse().data(true).rel(true);
    }
    /**
     * todo:使用
     * 获取卡片集，如果用户点击展示该卡片，
     * 该实体类defaultChecked字段为true
     * @return
     */
    @RequestMapping(value = "/collection", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<UserCardVO>> allCard() {
        String userID =  request.getHeader("userId");
        if(StringUtils.isEmpty(userID)){
            throw new BaseException("request contains no user...");
        }
        List<UserCardVO> list = baseBiz.allCard(userID);
        return new ListRestResponse("",list.size(),list);
    }
}
