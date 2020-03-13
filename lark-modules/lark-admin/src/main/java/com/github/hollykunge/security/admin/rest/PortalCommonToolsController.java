package com.github.hollykunge.security.admin.rest;

import com.github.hollykunge.security.admin.constant.AdminCommonConstant;
import com.github.hollykunge.security.admin.entity.OrgDTO;
import com.github.hollykunge.security.admin.entity.User;
import com.github.hollykunge.security.admin.feign.PortalServerFeign;
import com.github.hollykunge.security.admin.mapper.OrgMapper;
import com.github.hollykunge.security.admin.mapper.UserMapper;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.dto.CommonToolsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: zhhongyu
 * @description: 调用门户服务的常用工具接口
 * @since: Create in 15:12 2019/8/16
 */
@Controller
@RequestMapping("commonTools")
public class PortalCommonToolsController {
    @Resource
    private PortalServerFeign portalServerFeign;

    @Resource
    private UserMapper userMapper;

    @Autowired
    private OrgMapper orgMapper;
    /**
     * todo:使用
     * 19-12-11
     * fansq
     * 添加常用链接增加接口
     * @param entity
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse<CommonToolsDto> add(@RequestBody CommonToolsDto entity) {
        return portalServerFeign.add(returnParam(entity));
    }

    /**
     * todo:使用
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "page", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<CommonToolsDto> page(@RequestParam Map<String, Object> params)throws Exception{
        return portalServerFeign.page(params);
    }

    /**
     * todo:使用
     * @param entity
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse<CommonToolsDto> update(@RequestBody CommonToolsDto entity) throws Exception {
        return portalServerFeign.update(returnParam(entity));
    }

    /**
     * todo:使用
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ObjectRestResponse<CommonToolsDto> remove(@PathVariable String id) throws Exception {
        return portalServerFeign.remove(id);
    }

    /**
     * fansq
     * 封装fegin 请求参数
     * @param entity
     * @return
     */
    public HashMap<String,Object>  returnParam(CommonToolsDto entity){
        HashMap<String,Object> hashMap = new HashMap<>();
        if(AdminCommonConstant.PORTALORGUSERSTATUS.equals(entity.getPortalOrgUserStatus())){
            String orgCode = entity.getOrgCode();
            if ("0010".equals(orgCode)) {
                Example example = new Example(User.class);
                Example.Criteria criteria = example.createCriteria();
                List<User> userList = userMapper.selectByExample(example);
                hashMap.put("user",userList);
            }else {
//                List<OrgDTO> orgDTOS = orgMapper.findOrg(orgCode);
//                System.out.println(orgDTOS);
//                List<String> os = new ArrayList<String>();
//                this.findOrdId(orgDTOS,os);
//                List<User> userList = userMapper.findUserByOrgCode(os);
//                if(userList.size()>0){
//                    hashMap.put("user", userList);
//                }
                Example example = new Example(User.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andLike("orgCode",orgCode);
                List<User> userList = userMapper.selectByExample(example);
                if(userList.size()>0){
                    hashMap.put("user", userList);
                }
            }
        }
        hashMap.put("tools",entity);
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
