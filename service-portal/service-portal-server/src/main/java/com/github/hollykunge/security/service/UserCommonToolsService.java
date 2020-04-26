package com.github.hollykunge.security.service;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.exception.service.DatabaseDataException;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.common.util.UUIDUtils;
import com.github.hollykunge.security.entity.CommonTools;
import com.github.hollykunge.security.entity.UserCommonTools;
import com.github.hollykunge.security.mapper.CommonToolsMapper;
import com.github.hollykunge.security.mapper.UserCommonToolsMapper;
import com.github.hollykunge.security.utils.ListUtil;
import com.github.hollykunge.security.vo.UserCommonToolsVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 用户设置常用工具业务
 *
 * @author zhhongyu
 * @since 2019-06-19
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserCommonToolsService extends BaseBiz<UserCommonToolsMapper, UserCommonTools> {
    @Resource
    private CommonToolsMapper commonToolsMapper;
    @Resource
    private UserCommonToolsMapper userCommonToolsMapper;

    @Override
    protected String getPageName() {
        return null;
    }

    /**
     * fansq
     * 插入用户 常用链接 关系表接口
     *
     * @param userCommonTools
     * @return
     */
    public void insertCommonTools(List<UserCommonTools> userCommonTools) {
        Map<Integer, List<UserCommonTools>> map = new ListUtil<UserCommonTools>().batchList(userCommonTools, 500);
        for (Integer integer : map.keySet()) {
            userCommonToolsMapper.insertCommonTools(map.get(integer));
        }
    }

    /**
     * fansq
     * 删除
     *
     * @param toolId
     * @param userCommonTools
     */
    public void deleteCommonTools(String toolId, List<UserCommonTools> userCommonTools) {
        Map<Integer, List<UserCommonTools>> map = new ListUtil<UserCommonTools>().batchList(userCommonTools, 500);
        for (Integer integer : map.keySet()) {
            userCommonToolsMapper.deleteUserCommonTools(toolId, userCommonTools);
        }
    }

    /**
     * fansq
     * 19-12-12
     * 根据用户id 常用链接id 查询
     */
    public List<UserCommonTools> selectUserCommonTools(String toolId, List<UserCommonTools> userCommonToolsList) {
        return userCommonToolsMapper.selectUserCommonTools(toolId, userCommonToolsList);
    }


    /**
     * 定义用户常用工具分页，由于该接口不能使用basetable，故而重新定义该方法
     *
     * @param query  查询参数封装实体类，orgCode 用户组织必须包含在query中
     * @param userId 用户id
     * @return
     */
    public TableResultResponse<UserCommonToolsVO> userCommonToolsTable(Query query, String userId) {
        return this.allCommonTools(userId, query);
    }

    /**
     * 获取用户所有的常用工具，已被勾选的将defaultChecked赋值为true
     *
     * @param userId
     * @return
     */
    public TableResultResponse<UserCommonToolsVO> allCommonTools(String userId, Query query) {
        Example example = new Example(CommonTools.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", "1");
        String orgCode = "";
        if (query.entrySet().size() > 0) {
            for (Map.Entry<String, Object> entry : query.entrySet()) {
                if ("orgCode".equals(entry.getKey())) {
                    orgCode = entry.getValue().toString();
                    continue;
                }
                criteria.andLike(entry.getKey(), "%" + entry.getValue().toString() + "%");
            }
        }
        if (StringUtils.isEmpty(orgCode) || "".equals(orgCode)) {
            throw new DatabaseDataException("当前用户没有组织编码，无法获取常用链接。");
        }
        //查询status为启用的数据
        Page<Object> result = PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<CommonTools> commonTools = commonToolsMapper.selectByExample(example);
        String userOrgCode = orgCode;
        commonTools = commonTools.parallelStream().filter(new Predicate<CommonTools>() {
            @Override
            public boolean test(CommonTools commonTools) {
                if (StringUtils.isEmpty(commonTools.getOrgCode())) {
                    return false;
                }
                if (userOrgCode.contains(commonTools.getOrgCode())) {
                    return true;
                }
                return false;
            }
        }).collect(Collectors.toList());
        UserCommonTools userCommonTools = new UserCommonTools();
        userCommonTools.setUserId(userId);
        List<UserCommonTools> userCommonToolsList = mapper.select(userCommonTools);
        List<UserCommonToolsVO> finalResult = this.setDefaultChecked(commonTools, userCommonToolsList);
        return new TableResultResponse<UserCommonToolsVO>(result.getPageSize(), result.getPageNum(), result.getPages(), result.getTotal(), finalResult);
    }

    private List<UserCommonToolsVO> setDefaultChecked(List<CommonTools> commonTools, List<UserCommonTools> userCommonToolsList) {
        List<UserCommonToolsVO> result = new ArrayList<>();
        commonTools.forEach(commonTool -> {
            UserCommonToolsVO userCommonToolsVO = new UserCommonToolsVO();
            BeanUtils.copyProperties(commonTool, userCommonToolsVO);
            if (StringUtils.isEmpty(commonTool.getId())) {
                throw new DatabaseDataException("常用链接id异常。");
            }
            boolean isContains = userCommonToolsList.parallelStream().
                    anyMatch(userCommonTool -> commonTool.getId().equals(userCommonTool.getToolId()));
            if (isContains) {
                userCommonToolsVO.setDefaultChecked(true);
            } else {
                userCommonToolsVO.setDefaultChecked(false);
            }
            result.add(userCommonToolsVO);
        });
        return result;
    }

    /**
     * 用户获取要显示的常用工具列表
     *
     * @param userId
     * @return
     */
    public List<UserCommonToolsVO> userCommonTools(String userId) {
        List<UserCommonToolsVO> result = new ArrayList<>();
        UserCommonTools userCommonTool = new UserCommonTools();
        userCommonTool.setUserId(userId);
        List<UserCommonTools> userCommonTools = mapper.select(userCommonTool);
        userCommonTools.forEach(userTool -> {
            UserCommonToolsVO userCommonToolsVO = new UserCommonToolsVO();
            if (StringUtils.isEmpty(userTool.getToolId())) {
                throw new DatabaseDataException("常用链接信息异常。");
            }
            CommonTools commonTools = commonToolsMapper.selectByPrimaryKey(userTool.getToolId());
            BeanUtils.copyProperties(commonTools, userCommonToolsVO);
            userCommonToolsVO.setTitle(commonTools.getTitle());
            userCommonToolsVO.setUri(commonTools.getUri());
            userCommonToolsVO.setId(commonTools.getId());
            userCommonToolsVO.setDefaultChecked(true);

            result.add(userCommonToolsVO);
        });
        return result;
    }

    @Override
    public void insertSelective(UserCommonTools entity) {
        entity.setId(UUIDUtils.generateShortUuid());
        mapper.insertSelective(entity);
    }
}
