package com.github.hollykunge.security.admin.mapper;

import com.github.hollykunge.security.admin.api.dto.AdminUser;
import com.github.hollykunge.security.admin.entity.User;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author LARK
 */
public interface UserMapper extends Mapper<User> {
    /**
     * 通过角色ID查询用户列表，关联表中查询
     *
     * @param roleId 角色ID
     * @return 用户列表
     */
    List<User> selectUsersByRoleId(@Param("roleId") String roleId);


    /**
     *
     * @param nameLike
     * @return
     */
    List<User> selectUserByNameLike(@Param("nameLike") String nameLike);

    /**
     * 通过岗位ID,组织模糊查询用户列表
     * @param positionId
     * @param orgCode
     * @return
     */
    List<AdminUser> selectUsersByPositionIdAndOrgCode(@Param("positionId") String positionId, @Param("orgCode")String orgCode);

    List<AdminUser> findBySecretsAndNotPidLikeOrgCode(@Param("secretLevels") List<String> secretLevels,
                                                      @Param("pid") String pid,
                                                      @Param("orgCode")String orgCode);

    /**
     * 通过userid获取对应的用户详细信息接口
     * @param userId
     * @return 如果userid为空则为所有的用户信息
     */
    List<AdminUser> findByUserIdOrPid(@Param("userId") String userId,@Param("pid") String pid);

    List<User> findUserByOrgCode(@Param("os") List<String> os);

    void insertUserExcel(@Param("users") List<User> users);
}
