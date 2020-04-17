package com.github.hollykunge.security.admin.biz;

import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.github.hollykunge.security.admin.entity.Org;
import com.github.hollykunge.security.admin.entity.Position;
import com.github.hollykunge.security.admin.entity.PositionUserMap;
import com.github.hollykunge.security.admin.entity.User;
import com.github.hollykunge.security.admin.mapper.OrgMapper;
import com.github.hollykunge.security.admin.mapper.PositionMapper;
import com.github.hollykunge.security.admin.mapper.PositionUserMapMapper;
import com.github.hollykunge.security.admin.mapper.UserMapper;
import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.exception.auth.FrontInputException;
import com.github.hollykunge.security.common.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class PositionBiz extends BaseBiz<PositionMapper, Position> {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PositionUserMapMapper positionUserMapMapper;
    @Autowired
    private OrgMapper orgMapper;

    public List<AdminUser> getPositionUsers(String positionId) {
        return userMapper.selectUsersByPositionIdAndOrgCode(positionId,null);
    }

    /**
     * fansq 19-11-28
     * 根据用户id获取position
     * @param userId
     * @return position 列表
     */
    public List<Position> getPositionsByUserId(String userId){
        PositionUserMap positionUserMapParams = new PositionUserMap();
        positionUserMapParams.setUserId(userId);
        List<PositionUserMap> positionList = positionUserMapMapper.select(positionUserMapParams);
        List<Position> allPosition = mapper.selectAll();
        List<Position> resultPosition = new ArrayList<Position>();
        for (PositionUserMap positionUserMap :
                positionList) {
            resultPosition.addAll(allPosition.stream().filter((Position position) -> positionUserMap.getPositionId().equals(position.getId())).collect(Collectors.toList()));
        }
        return resultPosition;
    }
    public List<AdminUser> getPositionUsersBySecret(String positionId,String secretLevel) {
        List<AdminUser> usersByPositionId = setUsersByPositionId(positionId,null);
        filterBySecret(usersByPositionId,secretLevel);
        return usersByPositionId;
    }

    public List<AdminUser> getPositionUsers(String positionId,String secretLevel,String orgCode){
        if(StringUtils.isEmpty(positionId) || StringUtils.isEmpty(orgCode)){
            throw new FrontInputException("岗位id或者组织不能为空。");
        }
        List<AdminUser> usersByPositionId = setUsersByPositionId(positionId,orgCode);
        filterBySecret(usersByPositionId,secretLevel);
        return usersByPositionId;
    }

    private List<AdminUser> setUsersByPositionId(String positionId,String orgCode){
        String posIdTemp = null;
        if (!positionId.isEmpty() && "0".equals(positionId)) {
            posIdTemp = "3";
        }
        if(!positionId.isEmpty() && "1".equals(positionId)){
            posIdTemp = "4";
        }
        if(!positionId.isEmpty() && "2".equals(positionId)){
            posIdTemp = "5";
        }
        if(StringUtils.isEmpty(posIdTemp)){
            return new ArrayList<>();
        }
        return userMapper.selectUsersByPositionIdAndOrgCode(posIdTemp,orgCode);
    }
    private void filterBySecret(List<AdminUser> usersByPositionId,String secretLevel){
        usersByPositionId.stream().filter(user -> user.getSecretLevel() != null && Integer.parseInt(user.getSecretLevel()) >= Integer.parseInt(secretLevel)).forEach(user -> {
            Org orgTemp = null;
            if(!StringUtils.isEmpty(user.getOrgCode())){
                orgTemp = orgMapper.selectByPrimaryKey(user.getOrgCode());
            }
            if(orgTemp != null){
                user.setOrgName(orgTemp.getPathName());
            }
        });
    }

    public void modifyPositionUsers(String positionId, String users) {
        PositionUserMap positionUser = new PositionUserMap();
        positionUser.setPositionId(positionId);
        positionUserMapMapper.delete(positionUser);
        if (!StringUtils.isEmpty(users)) {
            String[] mem = users.split(",");
            for (String m : mem) {
                PositionUserMap positionUserMapDo = new PositionUserMap();
                positionUserMapDo.setPositionId(positionId);
                positionUserMapDo.setUserId(m);
                //给基类赋值
                EntityUtils.setCreatAndUpdatInfo(positionUserMapDo);
                positionUserMapMapper.insertSelective(positionUserMapDo);
            }
        }
    }

    /**
     * fansq
     * 19-11-28
     * 增加和修改用户权限部门
     * @param positionsIds
     * @param userId
     */
    public void insertUserPosition(String positionsIds,String userId){
        PositionUserMap positionUser = new PositionUserMap();
        positionUser.setUserId(userId);
        positionUserMapMapper.delete(positionUser);
        PositionUserMap positionUserMapDo;
        if (!StringUtils.isEmpty(positionsIds)) {
            String[] poiArr = positionsIds.split(",");
            for (String poi : poiArr) {
                positionUserMapDo = new PositionUserMap();
                positionUserMapDo.setPositionId(poi);
                positionUserMapDo.setUserId(userId);
                //给基类赋值
                EntityUtils.setCreatAndUpdatInfo(positionUserMapDo);
                positionUserMapMapper.insertSelective(positionUserMapDo);
            }
        }
    }
    public List<String> getPositionIdByUserId(String userId){
        PositionUserMap positionUserMap = new PositionUserMap();
        positionUserMap.setUserId(userId);
        ArrayList<String> userIds = new ArrayList<>();
        List<PositionUserMap> positionUserMaps = positionUserMapMapper.select(positionUserMap);
        positionUserMaps.stream().forEach(positionUsers ->
            userIds.add(positionUsers.getPositionId())

         );

        return userIds;
    }
    @Override
    protected String getPageName() {
        return null;
    }
}
