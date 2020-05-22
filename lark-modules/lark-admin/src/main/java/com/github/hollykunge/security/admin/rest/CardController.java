package com.github.hollykunge.security.admin.rest;

import com.github.hollykunge.security.admin.constant.AdminCommonConstant;

import com.github.hollykunge.security.admin.entity.OrgDTO;
import com.github.hollykunge.security.admin.entity.User;
import com.github.hollykunge.security.admin.feign.CardServerFeign;
import com.github.hollykunge.security.admin.mapper.OrgMapper;
import com.github.hollykunge.security.admin.mapper.UserMapper;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.dto.CardDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fansq
 * @since 19-12-23
 * @description 卡片操作接口
 */
@Controller
@RequestMapping("/card")
public class CardController{

    @Autowired
    private CardServerFeign cardServerFeign;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrgMapper orgMapper;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<CardDto> add(@RequestBody CardDto entity) {
        return cardServerFeign.add(returnParam(entity));
    }

    @RequestMapping(value = "page", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<CardDto> page(@RequestParam Map<String, Object> params)throws Exception{
        return cardServerFeign.page(params);
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse<CardDto> update(@RequestBody CardDto entity) throws Exception {
        return cardServerFeign.update(returnParam(entity));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ObjectRestResponse<CardDto> remove(@PathVariable String id) throws Exception {
        return cardServerFeign.remove(id);
    }

    /**
     * fansq
     * 封装fegin 请求参数
     * @param entity
     * @return
     */
    public HashMap<String,Object> returnParam(CardDto entity){
        HashMap<String,Object> hashMap = new HashMap<>();
        if(AdminCommonConstant.PORTALORGUSERSTATUS.equals(entity.getCardOrgUserStatus())) {
            String orgCode = entity.getOrgCode();
            if ("0010".equals(orgCode)) {
                List<User> userList = userMapper.selectAll();
                hashMap.put("user", userList);
            } else {
                List<OrgDTO> orgDTOS = orgMapper.findOrg(orgCode);
                if(orgDTOS!=null && !orgDTOS.isEmpty()){
                    System.out.println(orgDTOS);
                    List<String> os = new ArrayList<String>();
                    this.findOrdId(orgDTOS,os);
                    List<User> userList = userMapper.findUserByOrgCode(os);
                    if(userList.size()>0){
                        hashMap.put("user", userList);
                    }
                }else{
                    User user = new User();
                    user.setOrgCode(orgCode);
                    List<User> userList = userMapper.select(user);
                    if(userList.size()>0){
                        hashMap.put("user", userList);
                    }
                }

            }
        }
        hashMap.put("cards",entity);
        return hashMap;
    }

    /**
     * fansq 获取所有的部门id
     * @param orgDTOS
     * @param os
     * @return
     */
    public List<String> findOrdId(List<OrgDTO> orgDTOS, List<String> os) {
        for (OrgDTO o : orgDTOS) {
            os.add(o.getId());
            List<OrgDTO> orgDTOList = o.getOrgDTOList();
            if (orgDTOList != null && !orgDTOList.isEmpty()) {
                findOrdId(o.getOrgDTOList(), os);
            }
        }
        return os;
    }

}
