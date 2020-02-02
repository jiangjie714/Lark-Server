package com.github.hollykunge.security.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * excel导出
 *
 * @author zhhongyu
 * @since 2019-07-16
 */
@Slf4j
public class ExportExcelUtils {
    /**
     * table 导出
     *
     * @param listt        table 列值（存对象）
     * @param columName    列头名称（中文）
     * @param columCode    列头code（对象中的私有属性）
     * @param sheetName    sheet页名称
     * @param outputStream 文件流
     * @return
     */
    public static byte[] export(List listt, String columName, String columCode, String sheetName, OutputStream outputStream) throws IOException {
        try {
            if(StringUtils.isEmpty(columName)){
                throw new BaseException("列头名称不能为空...");
            }
            if(StringUtils.isEmpty(columCode)){
                throw new BaseException("列头编码不能为空...");
            }
            String[] headers = columName.trim().split(",");
            String[] cList = columCode.trim().split(",");
            List<List> list = new ArrayList<>();
            for (int i = 0; i < listt.size(); i++) {
                List<String> line = new ArrayList<String>();
                String jsonString = JSON.toJSONString(listt.get(i));
                JSONArray jarr = JSON.parseArray("[" + jsonString + "]");
                Iterator<Object> iterator = jarr.iterator();
                String value = null;
                int j = 0;
                while (iterator.hasNext()) {
                    JSONObject ob = (JSONObject) iterator.next();
                    for (int a = 0; a < ob.size(); a++) {
                        if (a >= cList.length) {
                            break;
                        }
                        value = ob.getString(cList[a]);
                        //表格内容
                        line.add(value);
                    }
                    j++;
                }
                list.add(line);
            }
            byte[] bytes = exportExcel(sheetName, headers, list, outputStream);
            return bytes;
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            outputStream.close();
        }
        return null;
    }

    /**
     * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
     *
     * @param sheetName sheet页名称
     * @param headers   表格属性列名数组
     * @param list      需要显示的数据集合
     * @param out       与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     */

    private static byte[] exportExcel(String sheetName, String[] headers, List<List> list, OutputStream out) {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(sheetName);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 20);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 18);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        // 把字体应用到当前的样式
        style2.setFont(font2);
        // 声明一个画图的顶级管理器
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        // 定义注释的大小和位置,详见文档
        HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
                0, 0, 0, (short) 4, 2, (short) 6, 5));
        // 设置注释内容
//        comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
        // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
        comment.setAuthor("leno");
        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for (short i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        // 遍历集合数据，产生数据行
        for (int i = 0; i < list.size(); i++) {
            List<String> line = list.get(i);
            row = sheet.createRow(i + 1);
            for (int j = 0; j < line.size(); j++) {
                HSSFCell cell = row.createCell(j);
                cell.setCellStyle(style2);
                String value = line.get(j);
                try {
                    // 判断值的类型后进行强制类型转换
                    String textValue = null;
                    // 其它数据类型都当作字符串简单处理
                    if (StringUtils.isEmpty(value)) {
                        value = "";
                    }
                    textValue = value.toString();
                    // 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
                    if (textValue != null) {
                        //打开下面的注解 大文件会报错
//                        HSSFRichTextString richString = new HSSFRichTextString(textValue);
//                        HSSFFont font3 = workbook.createFont();
//                        richString.applyFont(font3);
                        cell.setCellValue(textValue);
                    }
                } catch (SecurityException e) {
                    e.printStackTrace();
                } finally {
                    // 清理资源
                }
            }

        }
        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook.getBytes();
    }

}