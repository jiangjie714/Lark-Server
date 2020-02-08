package com.github.hollykunge.security.admin.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.github.hollykunge.security.admin.biz.UserBiz;
import com.github.hollykunge.security.admin.entity.User;
import com.github.hollykunge.security.admin.mapper.PositionUserMapMapper;
import com.github.hollykunge.security.admin.mapper.RoleUserMapMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author fansq
 * @deprecation  easyExcel 工具类
 * @since 20-2-7
 */
public class EasyExcelUtil {

    /**
     * 导出Excel(07版.xlsx)到指定路径下
     *
     * @param path      路径
     * @param excelName Excel名称
     * @param sheetName sheet页名称
     * @param clazz     Excel要转换的类型
     * @param data      要导出的数据
     */
    public static void exportFile(String path, String excelName, String sheetName, Class clazz, List data) {
        String fileName = path.concat(excelName).concat(ExcelTypeEnum.XLSX.getValue());
        EasyExcel.write(fileName, clazz).sheet(sheetName).doWrite(data);
    }

    /**
     * 导出Excel(07版.xlsx)到web
     *
     * @param response  响应
     * @param excelName Excel名称
     * @param sheetName sheet页名称
     * @param clazz     Excel要转换的类型
     * @param data      要导出的数据
     * @throws Exception
     */
    public static void exportWeb(HttpServletResponse response, String excelName, String sheetName, Class clazz, List data) throws Exception {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        excelName = URLEncoder.encode(excelName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
        EasyExcel.write(response.getOutputStream(), clazz).sheet(sheetName).doWrite(data);
    }

    /**
     * fansq
     * 用户数据导入
     * @Autowired
     *        private ;
     *
     *    @Autowired
     *    private ;
     *
     *    @Autowired
     *    private ;
     */
    public static void importExcel(
            InputStream inputStream,
            UserBiz userBiz,
            RoleUserMapMapper roleUserMapMapper,
            PositionUserMapMapper positionUserMapMapper
            ) throws  Exception{
        EasyExcel.read(inputStream, User.class, new ExcelListener(userBiz,roleUserMapMapper,positionUserMapMapper)).sheet().doRead();
    }
}
