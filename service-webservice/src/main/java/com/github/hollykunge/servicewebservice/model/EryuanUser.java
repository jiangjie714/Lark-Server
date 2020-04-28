package com.github.hollykunge.servicewebservice.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "ERYUANUSER")
public class EryuanUser {

    @Id
    private String id;
    private String hId;
    private String sysId;
    private String casicOrgCode;

    public String getID() {
        return id;
    }

    public void setID(String ID) {
        this.id = ID;
    }

    private String operatorName;
    private String empCode;
    private String PId;
    private String secretLevel;
    private Date birthDate;
    private String gender;
    private String oTel;
    private String oEmail;
    private String workPost;
    private String tecPost;
    private String orderId;
    private String syncType;
    private Date dispTime;
    private String remark;
    private String refa;
    private String refb;
    private String orgName;
    private String status;
    @Column(name = "IS_SUCCESS")
    private String isSuccess;
    @Column(name = "ONEDOC_IS_SUCCESS")
    private String onedocIsSuccess;

    public String getOnedocIsSuccess() {
        return onedocIsSuccess;
    }

    public void setOnedocIsSuccess(String onedocIsSuccess) {
        this.onedocIsSuccess = onedocIsSuccess;
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

    public String getCASIC_ORG_CODE() {
        return casicOrgCode;
    }

    public void setCASIC_ORG_CODE(String CASIC_ORG_CODE) {
        this.casicOrgCode = CASIC_ORG_CODE;
    }

    public String getOPERATOR_NAME() {
        return operatorName;
    }

    public void setOPERATOR_NAME(String OPERATOR_NAME) {
        this.operatorName = OPERATOR_NAME;
    }

    public String getEMP_CODE() {
        return empCode;
    }

    public void setEMP_CODE(String EMP_CODE) {
        this.empCode = EMP_CODE;
    }

    public String getP_ID() {
        return PId;
    }

    public void setP_ID(String p_ID) {
        PId = p_ID;
    }

    public String getSECRET_LEVEL() {
        return secretLevel;
    }

    public void setSECRET_LEVEL(String SECRET_LEVEL) {
        this.secretLevel = SECRET_LEVEL;
    }

    public Date getBIRTH_DATE() {
        return birthDate;
    }

    public void setBIRTH_DATE(Date BIRTH_DATE) {
        this.birthDate = BIRTH_DATE;
    }

    public String getGENDER() {
        return gender;
    }

    public void setGENDER(String GENDER) {
        this.gender = GENDER;
    }

    public String getO_TEL() {
        return oTel;
    }

    public void setO_TEL(String o_TEL) {
        oTel = o_TEL;
    }

    public String getO_EMAIL() {
        return oEmail;
    }

    public void setO_EMAIL(String o_EMAIL) {
        oEmail = o_EMAIL;
    }

    public String getWORK_POST() {
        return workPost;
    }

    public void setWORK_POST(String WORK_POST) {
        this.workPost = WORK_POST;
    }

    public String getTEC_POST() {
        return tecPost;
    }

    public void setTEC_POST(String TEC_POST) {
        this.tecPost = TEC_POST;
    }

    public String getORDER_ID() {
        return orderId;
    }

    public void setORDER_ID(String ORDER_ID) {
        this.orderId = ORDER_ID;
    }

    public String getSYNC_TYPE() {
        return syncType;
    }

    public void setSYNC_TYPE(String SYNC_TYPE) {
        this.syncType = SYNC_TYPE;
    }

    public Date getDISP_TIME() {
        return dispTime;
    }

    public void setDISP_TIME(Date DISP_TIME) {
        this.dispTime = DISP_TIME;
    }

    public String getSTATUS() {
        return status;
    }

    public void setSTATUS(String STATUS) {
        this.status = STATUS;
    }

    public String getREMARK() {
        return remark;
    }

    public void setREMARK(String REMARK) {
        this.remark = REMARK;
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

    public String getORG_NAME() {
        return orgName;
    }

    public void setORG_NAME(String ORG_NAME) {
        this.orgName = ORG_NAME;
    }

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }
}
