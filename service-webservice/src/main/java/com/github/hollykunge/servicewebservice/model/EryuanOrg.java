package com.github.hollykunge.servicewebservice.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "ERYUANORG")
public class EryuanOrg {
    @Id
    private String  id;
    @Column(name = "H_ID")
    private String  hId;
    private String  sysId;
    private String  orgLevel;
    private String  orgName;
    private String  casicOrgSecret;
    private String  casicOrgCode;
    private String  casicPorgCode;
    private String externalName;
    private String  remark;
    private String  orderId;
    private Date  dispTime;
    private String  syncType;
    private String  status;
    private String  refa;
    private  String refb;
    private String  orgCode;
    private String  orgCodes;
    private String  orgNames;
    private String  miliCode;
    private String  orgnationCode;
    private  String creditCode;
    private String  country;
    private String  province;
    private  String city;
    private String  areaCounty;
    private String  currency;
    private  String inforLevel;
    private String  orgNature;
    private String  immeupOrg;
    private String  orgManLevel;
    private String  isLiCompany;
    private  String fillTwoOrg;
    private String  fillThrOrg;
    private String  erpComCode;
    private  String orderNumber;
    private String  induclass;
    @Column(name = "IS_SUCCESS")
    private String isSuccess;
    @Column(name = "PATH_CODE")
    private String pathCode;
    @Column(name = "PATH_NAME")
    private String pathName;
    @Column(name = "ONEDOC_IS_SUCCESS")
    private String onedocIsSuccess;

    public String getOnedocIsSuccess() {
        return onedocIsSuccess;
    }

    public void setOnedocIsSuccess(String onedocIsSuccess) {
        this.onedocIsSuccess = onedocIsSuccess;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getPathCode() {
        return pathCode;
    }

    public void setPathCode(String pathCode) {
        this.pathCode = pathCode;
    }

    public String getID() {
        return id;
    }

    public void setID(String ID) {
        this.id = ID;
    }

    public String getH_ID() {
        return hId;
    }

    public void setH_ID(String h_ID) {
        hId = h_ID;
    }

    public String getSYS_ID() {
        return sysId;
    }

    public void setSYS_ID(String SYS_ID) {
        this.sysId = SYS_ID;
    }

    public String getORG_LEVEL() {
        return orgLevel;
    }

    public void setORG_LEVEL(String ORG_LEVEL) {
        this.orgLevel = ORG_LEVEL;
    }

    public String getORG_NAME() {
        return orgName;
    }

    public void setORG_NAME(String ORG_NAME) {
        this.orgName = ORG_NAME;
    }

    public String getCASIC_ORG_SECRET() {
        return casicOrgSecret;
    }

    public void setCASIC_ORG_SECRET(String CASIC_ORG_SECRET) {
        this.casicOrgSecret = CASIC_ORG_SECRET;
    }

    public String getCASIC_ORG_CODE() {
        return casicOrgCode;
    }

    public void setCASIC_ORG_CODE(String CASIC_ORG_CODE) {
        this.casicOrgCode = CASIC_ORG_CODE;
    }

    public String getCASIC_PORG_CODE() {
        return casicPorgCode;
    }

    public void setCASIC_PORG_CODE(String CASIC_PORG_CODE) {
        this.casicPorgCode = CASIC_PORG_CODE;
    }

    public String getEXTERNAL_NAME() {
        return externalName;
    }

    public void setEXTERNAL_NAME(String EXTERNAL_NAME) {
        this.externalName = EXTERNAL_NAME;
    }

    public String getREMARK() {
        return remark;
    }

    public void setREMARK(String REMARK) {
        this.remark = REMARK;
    }

    public String getORDER_ID() {
        return orderId;
    }

    public void setORDER_ID(String ORDER_ID) {
        this.orderId = ORDER_ID;
    }

    public Date getDISP_TIME() {
        return dispTime;
    }

    public void setDISP_TIME(Date DISP_TIME) {
        this.dispTime = DISP_TIME;
    }

    public String getSYNC_TYPE() {
        return syncType;
    }

    public void setSYNC_TYPE(String SYNC_TYPE) {
        this.syncType = SYNC_TYPE;
    }

    public String getSTATUS() {
        return status;
    }

    public void setSTATUS(String STATUS) {
        this.status = STATUS;
    }

    public String getREFA() {
        return refa;
    }

    public void setREFA(String REFA) {
        this.refa = REFA;
    }

    public String getREFB() {
        return refb;
    }

    public void setREFB(String REFB) {
        this.refb = REFB;
    }

    public String getORG_CODE() {
        return orgCode;
    }

    public void setORG_CODE(String ORG_CODE) {
        this.orgCode = ORG_CODE;
    }

    public String getORG_CODES() {
        return orgCodes;
    }

    public void setORG_CODES(String ORG_CODES) {
        this.orgCodes = ORG_CODES;
    }

    public String getORG_NAMES() {
        return orgNames;
    }

    public void setORG_NAMES(String ORG_NAMES) {
        this.orgNames = ORG_NAMES;
    }

    public String getMILI_CODE() {
        return miliCode;
    }

    public void setMILI_CODE(String MILI_CODE) {
        this.miliCode = MILI_CODE;
    }

    public String getORGNATION_CODE() {
        return orgnationCode;
    }

    public void setORGNATION_CODE(String ORGNATION_CODE) {
        this.orgnationCode = ORGNATION_CODE;
    }

    public String getCREDIT_CODE() {
        return creditCode;
    }

    public void setCREDIT_CODE(String CREDIT_CODE) {
        this.creditCode = CREDIT_CODE;
    }

    public String getCOUNTRY() {
        return country;
    }

    public void setCOUNTRY(String COUNTRY) {
        this.country = COUNTRY;
    }

    public String getPROVINCE() {
        return province;
    }

    public void setPROVINCE(String PROVINCE) {
        this.province = PROVINCE;
    }

    public String getCITY() {
        return city;
    }

    public void setCITY(String CITY) {
        this.city = CITY;
    }

    public String getAREA_COUNTY() {
        return areaCounty;
    }

    public void setAREA_COUNTY(String AREA_COUNTY) {
        this.areaCounty = AREA_COUNTY;
    }

    public String getCURRENCY() {
        return currency;
    }

    public void setCURRENCY(String CURRENCY) {
        this.currency = CURRENCY;
    }

    public String getINFOR_LEVEL() {
        return inforLevel;
    }

    public void setINFOR_LEVEL(String INFOR_LEVEL) {
        this.inforLevel = INFOR_LEVEL;
    }

    public String getORG_NATURE() {
        return orgNature;
    }

    public void setORG_NATURE(String ORG_NATURE) {
        this.orgNature = ORG_NATURE;
    }

    public String getIMMEUP_ORG() {
        return immeupOrg;
    }

    public void setIMMEUP_ORG(String IMMEUP_ORG) {
        this.immeupOrg = IMMEUP_ORG;
    }

    public String getORG_MAN_LEVEL() {
        return orgManLevel;
    }

    public void setORG_MAN_LEVEL(String ORG_MAN_LEVEL) {
        this.orgManLevel = ORG_MAN_LEVEL;
    }

    public String getIS_LI_COMPANY() {
        return isLiCompany;
    }

    public void setIS_LI_COMPANY(String IS_LI_COMPANY) {
        this.isLiCompany = IS_LI_COMPANY;
    }

    public String getFILL_TWO_ORG() {
        return fillTwoOrg;
    }

    public void setFILL_TWO_ORG(String FILL_TWO_ORG) {
        this.fillTwoOrg = FILL_TWO_ORG;
    }

    public String getFILL_THR_ORG() {
        return fillThrOrg;
    }

    public void setFILL_THR_ORG(String FILL_THR_ORG) {
        this.fillThrOrg = FILL_THR_ORG;
    }

    public String getERP_COM_CODE() {
        return erpComCode;
    }

    public void setERP_COM_CODE(String ERP_COM_CODE) {
        this.erpComCode = ERP_COM_CODE;
    }

    public String getORDER_NUMBER() {
        return orderNumber;
    }

    public void setORDER_NUMBER(String ORDER_NUMBER) {
        this.orderNumber = ORDER_NUMBER;
    }

    public String getINDUCLASS() {
        return induclass;
    }

    public void setINDUCLASS(String INDUCLASS) {
        this.induclass = INDUCLASS;
    }

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }
  }
