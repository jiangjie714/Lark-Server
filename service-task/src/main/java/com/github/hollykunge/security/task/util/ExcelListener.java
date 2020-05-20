//package com.github.hollykunge.security.task.util;
//
//import com.alibaba.excel.context.AnalysisContext;
//import com.alibaba.excel.event.AnalysisEventListener;
//import com.alibaba.excel.exception.ExcelDataConvertException;
//import com.alibaba.fastjson.JSON;
//import com.github.hollykunge.security.admin.biz.UserBiz;
//import com.github.hollykunge.security.admin.constant.AdminCommonConstant;
//import com.github.hollykunge.security.admin.entity.Org;
//import com.github.hollykunge.security.admin.entity.PositionUserMap;
//import com.github.hollykunge.security.admin.entity.RoleUserMap;
//import com.github.hollykunge.security.admin.entity.User;
//import com.github.hollykunge.security.admin.mapper.OrgMapper;
//import com.github.hollykunge.security.admin.mapper.PositionUserMapMapper;
//import com.github.hollykunge.security.admin.mapper.RoleUserMapMapper;
//import com.github.hollykunge.security.admin.mapper.UserMapper;
//import com.github.hollykunge.security.common.entity.BaseEntity;
//import com.github.hollykunge.security.common.exception.BaseException;
//import com.github.hollykunge.security.common.util.EntityUtils;
//import com.github.hollykunge.security.common.util.SpecialStrUtils;
//import com.github.hollykunge.security.common.util.UUIDUtils;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.math.NumberUtils;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
///**
// * @author fansq
// * 导入数据监听类
// */
//@Slf4j
//@Transactional(rollbackFor = Exception.class)
//public class ExcelListener<L> extends AnalysisEventListener<T> {
//
//
//	public ExcelListener() {
//	}
//
//	private static final int BATCH_COUNT = 500;
//
//	@SneakyThrows
//	@Override
//	public void invoke(T t, AnalysisContext context) {
//		int rowIndex = context.readRowHolder().getRowIndex()+1;
//		importTaskExcel(t,rowIndex);
//		}
//	}
//
//	@Override
//	public void doAfterAllAnalysed(AnalysisContext context) {
//		saveData();
//		log.info("所有数据解析完成！");
//	}
//
//	/**
//	 * 加上存储数据库
//	 */
//	private void saveData() {
//		log.info("{}条数据，开始存储数据库！", list.size());
//		if (!CollectionUtils.isEmpty(list)) {
//			userBiz.insertUserExcel(list);
//			roleUserMapMapper.insertExcelUserRole(roleUserMaps);
//			positionUserMapMapper.insertExcelUserRole(positionUserMaps);
//		}
//		if(!CollectionUtils.isEmpty(orgList)){
//			orgMapper.insertExcelOrg(orgList);
//		}
//		log.info("存储数据库成功！");
//	}
//
//	@Override
//	public void onException(Exception exception, AnalysisContext context) throws Exception {
//		log.error("此处可以记录解析异常日志");
//		// 如果是某一个单元格的转换异常 能获取到具体行号
//		// 如果要获取头的信息 配合invokeHeadMap使用
//		if (exception instanceof ExcelDataConvertException) {
//			Integer columnIndex = ((ExcelDataConvertException) exception).getColumnIndex() + 1;
//			Integer rowIndex = ((ExcelDataConvertException) exception).getRowIndex() + 1;
//			String message = "第" + rowIndex + "行，第" + columnIndex + "列" + "，数据格式有误，请按照模板规定格式填写！";
//			throw new RuntimeException(message);
//		} else if (exception instanceof RuntimeException) {
//			throw exception;
//		} else {
//			super.onException(exception, context);
//		}
//	}
//
//
//	/**
//	 * fansq
//	 * 20-2-23
//	 * 导入用户数据
//	 * @param data
//	 */
//	public void importTaskExcel(User data,int rowIndex){
//		String userId = UUIDUtils.generateShortUuid();
//		String pId = data.getPId().toLowerCase();
//		String name = data.getName();
//		if(StringUtils.isEmpty(name)){
//			throw new BaseException("第"+rowIndex+"行，姓名不可为空！");
//		}
//		if (name.length()>10) {
//			throw new BaseException("第"+rowIndex+"行，姓名不可超过10个字符!");
//		}
//		if (SpecialStrUtils.check(name)) {
//			throw new BaseException("第"+rowIndex+"行，姓名中不能包含特殊字符!");
//		}
//		if(StringUtils.isEmpty(pId)){
//			throw  new BaseException("第"+rowIndex+"行，身份证号不可为空！");
//		}
//		String pattern = "\\d{17}[\\d|x]|\\d{15}";
//		Pattern r = Pattern.compile(pattern);
//		Matcher m = r.matcher(pId);
//		if(!m.matches()){
//			throw new BaseException("第"+rowIndex+"行，身份证号格式错误!");
//		}
//		//校验身份证是否在数据库中存在
//		User user = new User();
//		user.setPId(pId);
//		if (userMapper.selectCount(user) > 0) {
//			throw new BaseException("第"+rowIndex+"行，身份证号已存在!");
//		}
//
//		if(!userPidRowIndex.containsKey(pId)){
//			userPidRowIndex.put(pId,rowIndex);
//		}else{
//			int pidIndex = userPidRowIndex.get(pId);
//			throw new BaseException("第"+rowIndex+"行和第"+pidIndex+"行的身份证号重复，请修改！");
//		}
//
//		if(StringUtils.isEmpty(data.getOrgCode())){
//			throw  new BaseException("第"+rowIndex+"行，所属组织机构编码，不可为空！");
//		}
//		if(StringUtils.isEmpty(data.getOrgName())){
//			throw new BaseException("第"+rowIndex+"行，所属机构名称，不可为空！");
//		}
//		Org org = new Org();
//		org.setId(data.getOrgCode());
//		Org orgName = orgMapper.selectOne(org);
//		if(orgName == null) {
//			throw new BaseException("第"+rowIndex+"行，组织机构不存在!");
//		}
//		if(!StringUtils.equals(orgName.getOrgName(),data.getOrgName())){
//			throw new BaseException("第"+rowIndex+"行，组织机构编码和组织机构名称不匹配!");
//		}
//
//		String secretLevel = data.getSecretLevel();
//		if(!NumberUtils.isNumber(secretLevel)){
//			throw new BaseException("第"+rowIndex+"行，密级应为数字!");
//		}
//		if(Arrays.binarySearch(secretArr,secretLevel)<0){
//			throw new BaseException("第"+rowIndex+"行，密级只能为30,40,50,60,70,80,90！");
//		}
//		String gender = data.getGender();
//		if(StringUtils.isEmpty(gender)){
//			throw new BaseException("第"+rowIndex+"行，性别不可为空！");
//		}
//		if(!StringUtils.equals(gender,"男")&&!StringUtils.equals(gender,"女")){
//			throw new BaseException("第"+rowIndex+"行，性别填写错误，只能为男或女！");
//		}
//		if(data.getOrderId()==null){
//			throw new BaseException("第"+rowIndex+"行，排序字段不可为空！");
//		}
//		data.setPassword(AdminCommonConstant.ENCRYPTION_PASSWORD);
//		EntityUtils.setCreatAndUpdatInfo(data);
//		data.setId(userId);
//		data.setPId(pId.toLowerCase());
//		data.setDeleted(AdminCommonConstant.USER_DELETED_DEFAULT);
//		data.setEmpCode(UUIDUtils.generateShortUuid());
//		data.setAvatar(AdminCommonConstant.USER_AVATAR);
//		list.add(data);
//
//		RoleUserMap roleUserMap = new RoleUserMap();
//		//给导入的用户默认一个角色信息  普通人员
//		roleUserMap.setRoleId(AdminCommonConstant.USER_ROLE_DEFAULT);
//		roleUserMap.setUserId(userId);
//		roleUserMap.setId(UUIDUtils.generateShortUuid());
//		roleUserMaps.add(roleUserMap);
//		//给导入的用户默认一个权限信息  建研究室内群 0
//		PositionUserMap positionUserMapRoomInner = new PositionUserMap();
//
//        positionUserMapRoomInner.setUserId(userId);
//        positionUserMapRoomInner.setPositionId(AdminCommonConstant.USER_POSITION_DEFAULT);
//        positionUserMapRoomInner.setId(UUIDUtils.generateShortUuid());
//        positionUserMaps.add(positionUserMapRoomInner);
//        //建跨研究室群
//        PositionUserMap positionUserMapRoomOutter = new PositionUserMap();
//        positionUserMapRoomOutter.setUserId(userId);
//        positionUserMapRoomOutter.setPositionId(AdminCommonConstant.USER_POSTTION_ROOM_OUTTER);
//        positionUserMapRoomOutter.setId(UUIDUtils.generateShortUuid());
//        positionUserMaps.add(positionUserMapRoomOutter);
//        //建跨厂所群
//        PositionUserMap positionUserMapInstitutesOutter = new PositionUserMap();
//        positionUserMapInstitutesOutter.setUserId(userId);
//        positionUserMapInstitutesOutter.setPositionId(AdminCommonConstant.USER_POSITION_INSTITUTES_OUTTER);
//        positionUserMapInstitutesOutter.setId(UUIDUtils.generateShortUuid());
//
//		positionUserMaps.add(positionUserMapInstitutesOutter);
//		log.info("解析到一条数据:{}", JSON.toJSONString(data));
//		if (list.size() >= BATCH_COUNT) {
//			saveData();
//			list.clear();
//			roleUserMaps.clear();
//			positionUserMaps.clear();
//		}
//	}
//}