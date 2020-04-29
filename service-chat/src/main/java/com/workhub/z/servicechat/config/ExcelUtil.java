package com.workhub.z.servicechat.config;

import cn.hutool.poi.excel.ExcelWriter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtil {
    private static Logger log = LoggerFactory.getLogger(ExcelUtil.class);
    //获得excel工作区
    public static Workbook getWb(MultipartFile mf){
        String filepath = mf.getOriginalFilename();
        String ext = filepath.substring(filepath.lastIndexOf("."));
        InputStream is = null;
        Workbook wb = null;
        try {
            is = mf.getInputStream();
            if(".xls".equals(ext)){
                wb = new HSSFWorkbook(is);
            }else if(".xlsx".equals(ext)){
                wb = new XSSFWorkbook(is);
            }else{
                wb=null;
            }
        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException", e);
        } catch (IOException e) {
            log.error("IOException", e);
        }finally {
            if(is!=null){

                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("inputStream close error", e);
                }
            }
        }
        return wb;
    }
    //读取excel固定一行的的内容，放入集合里，rowIndex表示行号
    public static Map<Integer,Object> readExcelSingleRow(Workbook wb,int rowIndex) throws Exception{
        Map<Integer, Object> content = new HashMap<Integer, Object>();
        Sheet sheet = wb.getSheetAt(0);
        // 得到总行数
        Row row = sheet.getRow(rowIndex);
        int colNum = row.getPhysicalNumberOfCells();

        if (row!=null) {
            int j = 0;
            Object obj=null;
            while (j < colNum) {
                obj = getCellFormatValue(row.getCell(j));
                content.put(j, obj);
                j++;
            }
        }
        return content;
    }
    //读取excel内容，放入集合里，rowIndex表示从第几列开始读取,比如第一行是标题头，那么直接从第二行度，rowIndex=1即可,数据下标从0开始
    public static Map<Integer, Map<Integer,Object>> readExcelContentz(Workbook wb,int rowIndex) throws Exception{
        Map<Integer, Map<Integer, Object>> content = new HashMap<Integer, Map<Integer, Object>>();
        Sheet sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        Row row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();

        for (int i = rowIndex; i <= rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            Map<Integer, Object> cellValue = new HashMap<Integer, Object>();
            while (j < colNum) {
                Object obj = getCellFormatValue(row.getCell(j));
                cellValue.put(j, obj);
                j++;
            }
            content.put(i-rowIndex, cellValue);

        }
        return content;
    }
    //根据Cell类型设置数据
    private static Object getCellFormatValue(Cell cell) {
        Object cellvalue = "";
        if (cell != null) {
            switch (cell.getCellTypeEnum()) {
                case NUMERIC:
                case FORMULA: {
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        cellvalue = date;
                    } else {
                        cellvalue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case STRING:
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                default:
                    cellvalue = "";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }

    /**
     * 导出
     * @param fileName 文件名称 不带后缀
     * @param isHighExcelVersion 是否高版本 true xlsx,false xls
     * @param firstRowtitle  第一行标题，没有空或者“”
     * @param firstRowTitleCols 第一行标题占用列数
     * @param dataList 数据列表
     * @param colTitle 数据列表名称转义
     * @param response
     */
    public static<T extends List>  void exportExcel(String fileName, boolean isHighExcelVersion, String firstRowtitle,int firstRowTitleCols, T dataList, List<String[]> colTitle, HttpServletResponse response){
        ExcelWriter writer = cn.hutool.poi.excel.ExcelUtil.getWriter();//xls
        String fileExt = "xls";
        if(isHighExcelVersion){
            fileExt = "xlsx";
            cn.hutool.poi.excel.ExcelUtil.getWriter(true);
        }
        if(firstRowtitle!=null && !"".equals(firstRowtitle)){
            writer.merge(firstRowTitleCols-1, firstRowtitle);
        }
        if(colTitle!=null){
            for(String[] temp:colTitle){
                writer.addHeaderAlias(temp[0], temp[1]);
            }
        }

        writer.write(dataList,true);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        String finalFileName = null;//其他浏览器
        try {
            finalFileName = URLEncoder.encode(fileName+"."+fileExt,"UTF8");
        } catch (UnsupportedEncodingException e) {
            log.error("研讨excel导出错误：");
            log.error(Common.getExceptionMessage(e));
        }
        response.setHeader("Content-disposition", "attachment;filename="+finalFileName);
        ServletOutputStream out= null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            log.error("研讨excel导出错误：");
            log.error(Common.getExceptionMessage(e));
        }
        writer.flush(out);
        writer.close();
    }
}
