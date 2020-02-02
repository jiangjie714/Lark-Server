package com.github.hollykunge.servicewebservice.serviceImpl;

import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;

import java.rmi.RemoteException;

public class userInfoBizService {

    /**
     * 获取接口XML文件
     * @return
     * @throws ServiceException
     */

    public String getXmlUserInfo(String address)throws ServiceException {
        Service service = new Service();
        Call call = (Call) service.createCall();
       // call.setTargetEndpointAddress( "http://10.11.1.29/axis/services/BizService" );
        call.setTargetEndpointAddress( address);

        call.setOperationName( "runBiz" );
        call.addParameter( "packageName",XMLType.XSD_STRING,ParameterMode.IN );
        call.addParameter( "unitId", XMLType.XSD_STRING, ParameterMode.IN );
        call.addParameter( "processName", XMLType.XSD_STRING,ParameterMode.IN );
        call.addParameter( "bizDataXML", XMLType.XSD_STRING,ParameterMode.IN );
        call.setReturnType( XMLType.XSD_STRING );
        String bizDataXML="<?xml version='1.0' encoding='GB2312'?><root><data><sysID>XTSJPT</sysID></data></root>";

        String [] param={"uum", "0", "bizWebService.bizQueryOrgEmp", bizDataXML};
        Object response= null;
        try {
            response = call.invoke( param );
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println(" CALL BIZ RESULT IS  " + (String)response);
        return (String)response;
    }
}
