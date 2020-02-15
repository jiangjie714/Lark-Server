package com.github.hollykunge.security.admin.util;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.fastjson.JSON;
import com.github.hollykunge.security.admin.biz.UserBiz;
import com.github.hollykunge.security.admin.constant.AdminCommonConstant;
import com.github.hollykunge.security.admin.entity.Org;
import com.github.hollykunge.security.admin.entity.PositionUserMap;
import com.github.hollykunge.security.admin.entity.RoleUserMap;
import com.github.hollykunge.security.admin.entity.User;
import com.github.hollykunge.security.admin.mapper.OrgMapper;
import com.github.hollykunge.security.admin.mapper.PositionUserMapMapper;
import com.github.hollykunge.security.admin.mapper.RoleUserMapMapper;
import com.github.hollykunge.security.admin.mapper.UserMapper;
import com.github.hollykunge.security.common.constant.UserConstant;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.util.EntityUtils;
import com.github.hollykunge.security.common.util.SpecialStrUtils;
import com.github.hollykunge.security.common.util.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ExcelListener extends AnalysisEventListener<User> {

	private UserBiz userBiz;

	private RoleUserMapMapper roleUserMapMapper;

	private PositionUserMapMapper positionUserMapMapper;

	private UserMapper userMapper;

	private OrgMapper orgMapper;

	public String errMsg = null;
	public ExcelListener(UserBiz userBiz,RoleUserMapMapper roleUserMapMapper
	,PositionUserMapMapper positionUserMapMapper,UserMapper userMapper,OrgMapper orgMapper) {
		this.userBiz=userBiz;
		this.roleUserMapMapper=roleUserMapMapper;
		this.positionUserMapMapper=positionUserMapMapper;
		this.userMapper = userMapper;
		this.orgMapper = orgMapper;
	}

	private static final int BATCH_COUNT = 500;
	List<User> list = new ArrayList<User>();
	List<RoleUserMap> roleUserMaps = new ArrayList<RoleUserMap>();
	List<PositionUserMap> positionUserMaps = new ArrayList<PositionUserMap>();

	@Override
	public void invoke(User data, AnalysisContext context) {
		String userId = UUIDUtils.generateShortUuid();
		if (SpecialStrUtils.check(data.getName())) {
			errMsg= "姓名中不能包含特殊字符!";
			return;
		}
		Org org = new Org();
		org.setId(data.getOrgCode());
	    Org orgName = orgMapper.selectOne(org);
		if(!StringUtils.equals(orgName.getOrgName(),data.getOrgName())){
			errMsg="组织机构编码和组织机构名称不匹配!";
			return;
		}
		//校验身份证是否在数据库中存在
		User user = new User();
		user.setPId(data.getPId());
		if (userMapper.selectCount(user) > 0) {
			errMsg= "身份证已存在!";
			return;
		}
		if(NumberUtils.isNumber(data.getSecretLevel())){
			errMsg="密级应为数字!";
			return;
		}
		String password = new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).encode(AdminCommonConstant.USER_PASSWORD_DEFAULT);
		data.setPassword(password);
		EntityUtils.setCreatAndUpdatInfo(data);
		data.setId(userId);
		data.setDeleted(AdminCommonConstant.USER_DELETED_DEFAULT);
		data.setEmpCode(UUIDUtils.generateShortUuid());
		data.setAvatar(AdminCommonConstant.USER_AVATAR);
		list.add(data);

		RoleUserMap roleUserMap = new RoleUserMap();
		//给导入的用户默认一个角色信息  普通人员
		roleUserMap.setRoleId(AdminCommonConstant.USER_ROLE_DEFAULT);
		roleUserMap.setUserId(userId);
		roleUserMap.setId(UUIDUtils.generateShortUuid());
		roleUserMaps.add(roleUserMap);
		//给导入的用户默认一个权限信息  建研究室内群
		PositionUserMap positionUserMap = new PositionUserMap();
		positionUserMap.setUserId(userId);
		positionUserMap.setPositionId(AdminCommonConstant.USER_POSITION_DEFAULT);
		positionUserMap.setId(UUIDUtils.generateShortUuid());
		positionUserMaps.add(positionUserMap);
		log.info("解析到一条数据:{}", JSON.toJSONString(data));
		if (list.size() >= BATCH_COUNT) {
			//saveData();
			list.clear();
		}
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {
		//saveData();
		log.info("所有数据解析完成！");
	}

	/**
	 * 加上存储数据库
	 */
	private void saveData() {
		log.info("{}条数据，开始存储数据库！", list.size());
		if (!CollectionUtils.isEmpty(list)) {
			userBiz.insertUserExcel(list);
			roleUserMapMapper.insertExcelUserRole(roleUserMaps);
			positionUserMapMapper.insertExcelUserRole(positionUserMaps);
		}
		log.info("存储数据库成功！");
	}

	@Override
	public void onException(Exception exception, AnalysisContext context) throws Exception {
		log.error("此处可以记录解析异常日志");
		super.onException(exception, context);
	}
}