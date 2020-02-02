package com.github.hollykunge.serviceunitproject.serviceimpl;


import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.serviceunitproject.common.UserInfo;
import com.github.hollykunge.serviceunitproject.service.IUserService;
import com.github.hollykunge.serviceunitproject.service.UserPositionService;

import com.github.hollykunge.serviceunitproject.serviceimpl.servicebiz.Synchrodata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserPositionServiceImpl implements UserPositionService {


    @Autowired
    IUserService iUserService;

    @Value("${spring.address}")
    private String address;

    /**
     * 获取人员岗位信息
     *
     * @author guxq
     */
    @Override
    public UserInfo getUserPosition(String userID) {

        Map p2 = new HashMap<>();
        p2.put("userid",userID);
        UserInfo userInfo = iUserService.getUserInfo(p2);

        //开发使用
      /*  String templatefilePath = "D:\\jsonpersonpostion.json";
        String s = readJsonFile(templatefilePath);
        JSONObject json = JSONObject.parseObject(s);*/

        //正式使用
        String ss= getServiceJsonString(userID);
        log.info("说明用户岗位接口访问成功");
        JSONObject json = JSONObject.parseObject(ss);
        if(json==null){
            log.error("接口访问不到数据，两种");
            return userInfo;
        }
        String org = (String) json.get("org");
        String position = (String) json.get("position");
        String responsyslist = (String) json.get("responsyslist");
        String partsyslist = (String) json.get("partsyslist");

        userInfo.setErbuorg(org);
        userInfo.setPosition(position);
        userInfo.setResponsyslist(responsyslist);
        userInfo.setPartsyslist(partsyslist);
        return userInfo;
    }


    /**
     * 读取json文件，返回json串
     * @param fileName
     * @return
     */
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);

            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"gb2312");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private String getServiceJsonString(String userPid){
        URL url = null;
        String rtnStr="";
        try {

            url = new URL(address);
            Synchrodata synchrodata = new Synchrodata(url);
            rtnStr = synchrodata.getSynchrodataHttpSoap12Endpoint().getUserPosition(userPid);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("访问协同设计用户岗位主数据webservice出错",e);
            return  rtnStr;
        }
       return  rtnStr;
    }
}
