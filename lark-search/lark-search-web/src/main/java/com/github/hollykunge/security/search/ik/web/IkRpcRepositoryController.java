package com.github.hollykunge.security.search.ik.web;

import com.github.hollykunge.security.search.ik.service.OrgCodeIkService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author: zhhongyu
 * @description: 提供远程分词的仓库
 * @since: Create in 13:13 2020/3/18
 */
@RestController
public class IkRpcRepositoryController {
    @Autowired
    private OrgCodeIkService orgCodeIkService;

    @GetMapping("/ikRepository/orgCode")
    public String orgCodeIk(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
        return loadDict(request,response);
    }

    private String loadDict(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String result = "";
        StringBuilder sb = new StringBuilder();
        String eTag = request.getHeader("If-None-Match");
        Long modified= request.getDateHeader("If-Modified-Since");
        List<String> dictList = orgCodeIkService.allOrgs();
        //设置头
        if(null == modified || -1 == modified) {
            //如果没有，则使用当前时间
            modified = System.currentTimeMillis();
        }
        // 设置头信息。
        String oldEtag = dictList.size()+ "";
        response.setDateHeader("Last-Modified", Long.valueOf(modified));
        response.setHeader("ETags", dictList.size() + "");
        if(!oldEtag.equals(eTag)) {
            //拼装结果
            for(String dict : dictList) {
                //分词之间以换行符连接
                if(!StringUtils.isEmpty(sb.toString())) {
                    sb.append("\n");
                }
                sb.append(dict);
            }
            result = new String(sb.toString().getBytes(), "UTF-8");
//            result = sb.toString();
            //更新时间
            response.setDateHeader("Last-Modified", System.currentTimeMillis());
        }
        return result;
    }
}
