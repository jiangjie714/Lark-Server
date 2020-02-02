package com.github.hollykunge.security.rpc;

import com.alibaba.fastjson.JSON;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.common.util.UUIDUtils;
import com.github.hollykunge.security.constants.Constants;
import com.github.hollykunge.security.dto.CardDto;
import com.github.hollykunge.security.entity.CardInfo;
import com.github.hollykunge.security.entity.CommonTools;
import com.github.hollykunge.security.entity.User;
import com.github.hollykunge.security.entity.UserCard;
import com.github.hollykunge.security.service.CardService;
import com.github.hollykunge.security.service.UserCardService;
import com.github.hollykunge.security.vo.UserCardVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fansq
 * @since 19-12-23
 * @deprecation card服务提供类
 */
@Controller
@RequestMapping("/api/card")
public class CardRest {

    @Resource
    private CardService cardService;
    @Resource
    private UserCardService userCardService;

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<CardDto> add(@RequestBody HashMap<String,Object> hashMap){
        CardInfo cardDto = JSON.parseObject(JSON.toJSONString(hashMap.get("cards")), CardInfo.class);
        String uuid = UUIDUtils.generateShortUuid();
        cardDto.setId(uuid);
        if(cardService.selectCount(cardDto)>0){
            return new ObjectRestResponse<>().rel(false).msg("error,this commonTool is exist");
        };
        cardService.insertSelective(cardDto);
        if(Constants.CARDLORGUSERSTATUSONE.equals(cardDto.getCardOrgUserStatus())){
            List<User> userList = JSON.parseArray(JSON.toJSONString(hashMap.get("user")), User.class);
            List<UserCard> userCardList = new ArrayList<>();
            userCardService.insertUserCards(userCardList,userList,uuid);
        }
        return new ObjectRestResponse<>().rel(true);
    }

    @RequestMapping(value = "/page",method = RequestMethod.POST)
    @ResponseBody
    public TableResultResponse<CardInfo> page(@RequestBody Map<String, Object> params){
        Query query = new Query(params);
        return cardService.selectByQuery(query);
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<CardDto> update(@RequestBody HashMap<String,Object> hashMap){
        CardInfo cardDto = JSON.parseObject(JSON.toJSONString(hashMap.get("cards")), CardInfo.class);
        CardInfo cardInfo = cardService.selectById(cardDto.getId());
        if(cardInfo.getOrgCode().equals(cardDto.getOrgCode())){
            if(!cardInfo.getCardOrgUserStatus().equals(cardDto.getCardOrgUserStatus())&&
                    Constants.PORTALORGUSERSTATUSONE.equals(cardDto.getCardOrgUserStatus())){
                List<User> userList = JSON.parseArray(JSON.toJSONString(hashMap.get("user")), User.class);
                List<UserCard> userCardList = new ArrayList<>();
                userCardService.updateUserCards(userList,userCardList,cardDto);
            }
        }else{
            if(Constants.PORTALORGUSERSTATUSONE.equals(cardDto.getCardOrgUserStatus())){
                List<User> userList = JSON.parseArray(JSON.toJSONString(hashMap.get("user")), User.class);
                List<UserCard> userCardList = new ArrayList<>();
                userCardService.updateUserCards(userList,userCardList,cardDto);
            }
        }
        cardService.updateSelectiveById(cardDto);
        return new ObjectRestResponse<CommonTools>().rel(true);
    }

    @RequestMapping(value = "/remove",method = RequestMethod.GET)
    @ResponseBody
    public  ObjectRestResponse<CardDto> remove(@RequestParam("id") String id){
        Object s = cardService.selectById(id);
        if(s==null){
            return new ObjectRestResponse<>().data("查询不到此卡片，无法删除！").rel(false);
        }
        cardService.deleteById(id);
        UserCard userCard = new UserCard();
        userCard.setCardId(id);
        userCardService.delete(userCard);
        return new ObjectRestResponse<>().data(s).rel(true);
    }
}
