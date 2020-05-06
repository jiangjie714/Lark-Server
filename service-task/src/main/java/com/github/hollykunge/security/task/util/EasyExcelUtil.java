//package com.github.hollykunge.security.task.util;
//
//import com.alibaba.excel.EasyExcel;
//import com.alibaba.excel.support.ExcelTypeEnum;
//import com.github.hollykunge.security.admin.biz.UserBiz;
//import com.github.hollykunge.security.admin.entity.Org;
//import com.github.hollykunge.security.admin.entity.User;
//import com.github.hollykunge.security.admin.mapper.OrgMapper;
//import com.github.hollykunge.security.admin.mapper.PositionUserMapMapper;
//import com.github.hollykunge.security.admin.mapper.RoleUserMapMapper;
//import com.github.hollykunge.security.admin.mapper.UserMapper;
//import org.springframework.util.StringUtils;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.InputStream;
//import java.net.URLEncoder;
//import java.util.List;
//
///**
// * @author fansq
// * @deprecation  easyExcel 工具类
// * @since 20-5-6
// */
//public class EasyExcelUtil {
//
//    /**
//     * 导出Excel(07版.xlsx)到指定路径下
//     *
//     * @param path      路径
//     * @param excelName Excel名称
//     * @param sheetName sheet页名称
//     * @param clazz     Excel要转换的类型
//     * @param data      要导出的数据
//     */
//    public static void exportFile(String excelType,String path, String excelName, String sheetName, Class clazz, List data) {
//        String fileName = null;
//        if(StringUtils.isEmpty(excelType)|| "xlsx".equals(excelType)){
//            fileName = path.concat(excelName).concat(ExcelTypeEnum.XLSX.getValue());
//        }else{
//            fileName = path.concat(excelName).concat(ExcelTypeEnum.XLS.getValue());
//        }
//        EasyExcel.write(fileName, clazz).sheet(sheetName).doWrite(data);
//    }
//
//    /**
//     * 导出Excel(07版.xlsx)到web
//     *
//     * @param response  响应
//     * @param excelName Excel名称
//     * @param sheetName sheet页名称
//     * @param clazz     Excel要转换的类型
//     * @param data      要导出的数据
//     * @throws Exception
//     */
//    public static void exportWeb(String excelType,HttpServletResponse response, String excelName, String sheetName, Class clazz, List data) throws Exception {
//        String fileName = null;
//        if(StringUtils.isEmpty(excelType)|| "xlsx".equals(excelType)){
//            fileName = ExcelTypeEnum.XLSX.getValue();
//        }else{
//            fileName = ExcelTypeEnum.XLS.getValue();
//        }
//        response.setContentType("application/vnd.ms-excel");
//        response.setCharacterEncoding("utf-8");
//        // 这里URLEncoder.encode可以防止中文乱码
//        excelName = URLEncoder.encode(excelName, "UTF-8");
//        response.setHeader("Content-disposition", "attachment;filename=" + excelName + fileName);
//        EasyExcel.write(response.getOutputStream(), clazz).sheet(sheetName).doWrite(data);
//    }
//
//    /**
//     * fansq
//     * 用户数据导入
//     */
//    public static ExcelListener  importExcel(
//            InputStream inputStream,
//            UserBiz userBiz,
//            RoleUserMapMapper roleUserMapMapper,
//            PositionUserMapMapper positionUserMapMapper,
//            UserMapper userMapper,
//            OrgMapper orgMapper
//            ){
//        ExcelListener excelListener = new ExcelListener(userBiz,roleUserMapMapper,positionUserMapMapper,userMapper,orgMapper);
//        EasyExcel.read(inputStream, User.class, excelListener).sheet().doRead();
//        return excelListener;
//    }
//
//    /**
//     * fansq
//     * 组织数据导入
//     */
//    public static ExcelListener  importOrgExcel(
//            InputStream inputStream,
//            OrgMapper orgMapper
//    ){
//        ExcelListener excelListener = new ExcelListener(orgMapper);
//            EasyExcel.read(inputStream, Org.class, excelListener).sheet().doRead();
//        return excelListener;
//    }
//}
