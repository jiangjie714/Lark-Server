package com.github.hollykunge.security.admin.rest;


import com.cxytiandi.encrypt.springboot.annotation.Decrypt;
import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.github.hollykunge.security.admin.biz.PositionBiz;
import com.github.hollykunge.security.admin.dto.biz.AdminUser;
import com.github.hollykunge.security.admin.entity.Position;
import com.github.hollykunge.security.common.msg.ListRestResponse;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.rest.BaseController;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @author fansq
 * @since 19-8-26
 * 岗位操作
 */
@RestController
@RequestMapping("position")
public class PositionController extends BaseController<PositionBiz, Position> {
    /**
     * 根据岗位获取用户
     *todo:使用
     * @param id 岗位id
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public ListRestResponse<List<AdminUser>> getUsers(@RequestParam("id") String id) {
        List<AdminUser> positionUsers = baseBiz.getPositionUsers(id);
        return new ListRestResponse("",positionUsers.size(),positionUsers);
    }


    /**
     * todo:使用
     * 批量修改岗位用户
     */
    @Decrypt
    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse modifyUsers(@RequestBody Map<String,Object> map) {
        String id =  (String)map.get("id");
        String users =  (String)map.get("users");
        baseBiz.modifyPositionUsers(id, users);
        return new ObjectRestResponse().rel(true).msg("修改成功");
    }

    /**
     * fansq
     * 19-11-27
     * 新建用户指定多个岗位
     */
    @Decrypt
    @RequestMapping(value = "/userPosition" ,method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse insertUserPosition(@RequestBody Map<String,Object> map){
        String positionIds = (String)map.get("positionIds");
        String userId = (String)map.get("userId");
        baseBiz.insertUserPosition(positionIds,userId);
        return new ObjectRestResponse().rel(true).msg("添加成功");
    }
}
