package com.workhub.z.servicechat.config;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
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
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                case Cell.CELL_TYPE_FORMULA: {
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        cellvalue = date;
                    } else {
                        cellvalue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING:
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
}
