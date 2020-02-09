package com.github.hollykunge.security.admin.util;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.github.hollykunge.security.admin.biz.UserBiz;
import com.github.hollykunge.security.admin.entity.PositionUserMap;
import com.github.hollykunge.security.admin.entity.RoleUserMap;
import com.github.hollykunge.security.admin.entity.User;
import com.github.hollykunge.security.admin.mapper.PositionUserMapMapper;
import com.github.hollykunge.security.admin.mapper.RoleUserMapMapper;
import com.github.hollykunge.security.common.util.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ExcelListener extends AnalysisEventListener<User> {

	private UserBiz userBiz;

	private RoleUserMapMapper roleUserMapMapper;

	private PositionUserMapMapper positionUserMapMapper;

	public ExcelListener(UserBiz userBiz,RoleUserMapMapper roleUserMapMapper
	,PositionUserMapMapper positionUserMapMapper) {
		this.userBiz=userBiz;
		this.roleUserMapMapper=roleUserMapMapper;
		this.positionUserMapMapper=positionUserMapMapper;
	}

	private static final int BATCH_COUNT = 500;
	List<User> list = new ArrayList<User>();
	List<RoleUserMap> roleUserMaps = new ArrayList<RoleUserMap>();
	List<PositionUserMap> positionUserMaps = new ArrayList<PositionUserMap>();

	@Override
	public void invoke(User data, AnalysisContext context) {
		String userId = UUIDUtils.generateShortUuid();
		data.setId(userId);
		//导入用户默认密码
		data.setPassword("123456");
		list.add(data);

		RoleUserMap roleUserMap = new RoleUserMap();
		//给导入的用户默认一个角色信息  普通人员
		roleUserMap.setRoleId("izuhPB1");
		roleUserMap.setUserId(userId);
		roleUserMap.setId(UUIDUtils.generateShortUuid());
		roleUserMaps.add(roleUserMap);
		//给导入的用户默认一个权限信息  建研究室内群
		PositionUserMap positionUserMap = new PositionUserMap();
		positionUserMap.setUserId(userId);
		positionUserMap.setPositionId("0");
		positionUserMap.setId(UUIDUtils.generateShortUuid());
		positionUserMaps.add(positionUserMap);
		log.info("解析到一条数据:{}", JSON.toJSONString(data));
		if (list.size() >= BATCH_COUNT) {
			saveData();
			list.clear();
		}
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {
		saveData();
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
}