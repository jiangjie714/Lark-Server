package com.github.hollykunge.serviceunitproject.serviceimpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.serviceunitproject.dao.ProjectUnitDao;
import com.github.hollykunge.serviceunitproject.model.ProjectUnit;
import com.github.hollykunge.serviceunitproject.service.ProjectUnitService;
import com.github.hollykunge.serviceunitproject.serviceimpl.servicebiz.Synchrodata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;



import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;


@Slf4j
@Service
public class ProjectUnitServiceImpl  implements ProjectUnitService {

    @Autowired
    private ProjectUnitDao projectUnitDao;
    @Value("${spring.address}")
    private String address;

    /**
     * 获取协同设计主数据项目单元
     *
     * @author guxq
     */
    @Override
    public void saveErbuProject() {

        ProjectUnit projectUnit = new ProjectUnit();
        //开发使用
    /*    String templatefilePath = "D:\\jsonunitproject.json";
        String s = readJsonFile(templatefilePath);
        JSONObject json = JSONObject.parseObject(s);*/
        //正式使用
         String ss= getServiceJsonString();
         log.info("说明接口访问成功");
        JSONObject json = JSONObject.parseObject(ss);
        JSONArray jsonArr = json.getJSONArray("teamList");
         if(jsonArr==null){
             log.error("接口访问不到数据，请检查端口");
             return;
         }
        for (int i = 0; i < jsonArr.size(); i++) {
            JSONObject jo = jsonArr.getJSONObject(i);
            String id = (String) jo.get("id");
            String name = (String) jo.get("name");
            String code = (String) jo.get("code");
            String description = (String) jo.get("description");
            String pid = (String) jo.get("pid");
            String cseq = (String) jo.get("cseq");
            String enabled = (String)jo.get("enabled").toString();
            String createtime =(String)jo.get("createTime");
            String creator =(String) jo.get("creator");
            String updateTime =(String) jo.get("updateTime");
            String updator = (String)jo.get("updator");
            String org_id = (String) jo.get("orgId");
            String d_pid = (String) jo.get("dPid");
            String type = String.valueOf( jo.get("type"));
            String army_id = (String) jo.get("armId");
            String model = (String) jo.get("modelName");
            String modelSimpleName = (String) jo.get("modelSimpleName");
            String manage_org = (String) jo.get("manageOrg");
            String org_leader = (String) jo.get("orgLeader");
            String tech_manager = (String) jo.get("techManager");
            String project_manager = (String) jo.get("project_manager");
            String hall_leader = (String) jo.get("hallLeader");
            String partask_org_id = (String) jo.get("partaskOrgId");
            String unitLevelId = (String) jo.get("unitLevelId");
            String unitClassifyId = (String) jo.get("unitClassifyId");
            String product_classify_id = (String) jo.get("productClassifyId");
            String prev_name_id = (String) jo.get("prevNameId");
            String alias_name = (String) jo.get("aliasName");
            String model_short = (String) jo.get("modelShort");
            String part_org_id = (String) jo.get("partOrgId");
            String orgUserId = (String) jo.get("orgUserId");
            String partaskOrgUserId = (String) jo.get("partaskOrgUserId");
            String tempId = (String) jo.get("tempId");
            String domain_id = (String) jo.get("domainId");
            String product_classify = (String) jo.get("productClassify");
            String major_classify = (String) jo.get("majorClassify");
            String general_unit = (String) jo.get("generalUnit");
            String ref_id = (String) jo.get("refId");
            String dd_org_id = (String) jo.get("ddOrgId");
            String dd_org_user_id = (String) jo.get("ddOrgUserId");
            String dd_partask_user_id = (String) jo.get("ddPartaskOrgUserId");
            String belong_model = (String) jo.get("belongModel");
            String secretLevel = (String) jo.get("secretLevel");
            String masterRefId = (String) jo.get("masterRefId");
            String superior =(String )jo.get("superior");
            String subordinate =(String )jo.get("subordinate");
            String relatedUserId =(String )jo.get("relatedUserId");
            String teamType =(String )jo.get("teamType");

            projectUnit.setId(id);
            projectUnit.setName(name);
            projectUnit.setCode(code);
            projectUnit.setDescription(description);
            projectUnit.setParentId(pid);
            projectUnit.setCseq(cseq);
            projectUnit.setEnabled(enabled);
            projectUnit.setCreatetime(createtime);
            projectUnit.setCreator(creator);
            projectUnit.setUpdator(updator);
            projectUnit.setOrgId(org_id);
            projectUnit.setDPid(d_pid);
            projectUnit.setType(type);
            projectUnit.setArmyId(army_id);
            projectUnit.setModel(model);
            projectUnit.setModelSimpleName(modelSimpleName);
            projectUnit.setManageOrg(manage_org);
            projectUnit.setOrgLeader(org_leader);
            projectUnit.setTechManager(tech_manager);
            projectUnit.setProjectManager(project_manager);
            projectUnit.setHallLeader(hall_leader);
            projectUnit.setPartaskOrgId(partask_org_id);
            projectUnit.setMasterRefId(masterRefId);
            projectUnit.setOrgUserId(orgUserId);
            projectUnit.setPartaskOrgUserId(partaskOrgUserId);
            projectUnit.setSecretLevel(secretLevel);
            projectUnit.setTempId(tempId);
            projectUnit.setDomainId(domain_id);
            projectUnit.setProductClassify(product_classify);
            projectUnit.setMajorClassify(major_classify);
            projectUnit.setGeneralUnit(general_unit);
            projectUnit.setRefId(ref_id);
            projectUnit.setDdOrgId(dd_org_id);
            projectUnit.setDdOrgUserId(dd_org_user_id);
            projectUnit.setDdPartaskUserId(dd_partask_user_id);
            projectUnit.setBelongModel(belong_model);
            projectUnit.setUnitClassifyId(unitClassifyId);
            projectUnit.setProductClassifyId(product_classify_id);
            projectUnit.setPrevNameId(prev_name_id);
            projectUnit.setAliasName(alias_name);
            projectUnit.setModelShort(model_short);
            projectUnit.setPartOrgId(part_org_id);
            projectUnit.setUnitLevelId(unitLevelId);
            projectUnit.setUpdatetime(updateTime);
            projectUnit.setSuperior(superior);
            projectUnit.setSubordinate(subordinate);
            projectUnit.setRelatedUserId(relatedUserId);
            projectUnit.setTeamType(teamType);

            if (!"".equals(id) && id != null) {
                saveProjectUnitData(id, projectUnit);
            }

        }
    }

    public void saveProjectUnitData(String id,ProjectUnit projectUnit){


            if(projectUnitDao.queryProjectUnitByProjidCount(id)>0){
              int updatenum =  projectUnitDao.updateProjectUnitInfo(projectUnit);
                System.out.println("项目单元更新成功----->" + id );
                log.info("项目单元更新成功----->" + id );
            }
            else {
                projectUnitDao.saveProjectUnit(projectUnit);
                System.out.println("项目新增成功----->" + id );
                log.info("项目单元新增成功----->" + id );
            }

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

    public String getServiceJsonString(){
        URL url = null;
        try {

            url = new URL(address);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            log.error("访问协同设计主数据webservice出错",e);
        }

        // 调用型号项目数据接口
        Synchrodata synchrodata = new Synchrodata(url);
        String rtnStr = synchrodata.getSynchrodataHttpSoap12Endpoint().getAllTeamData();
        return  rtnStr;
    }


}
