package com.github.hollykunge.servicewebservice.testwebservice;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import javax.xml.rpc.ParameterMode;
/**
 * @author yzq
 */
public class AxisTest
{
    /*public static void main( String[] args ) throws Exception
    {
        Service service = new Service();
        Call call = ( Call ) service.createCall();
        call.setTargetEndpointAddress( "http://10.11.1.29/axis/services/BizService" );
//设置操作名
        call.setOperationName( "runBiz" );
//设置入口参数
        call.addParameter( "packageName",XMLType.XSD_STRING,ParameterMode.IN );
        call.addParameter( "unitId",XMLType.XSD_STRING,ParameterMode.IN );
        call.addParameter( "processName",XMLType.XSD_STRING,ParameterMode.IN );
        call.addParameter( "bizDataXML",XMLType.XSD_STRING,ParameterMode.IN );
        call.setReturnType( XMLType.XSD_STRING );

        String bizDataXML="<?xml version=\"1.0\" encoding=\"GB2312\"?><root><data><sysID>业务系统代码</sysID></data></root>";
        System.out.println(bizDataXML);
        //调用服务，返回结果保存在字符串response中。
        String [] param={"uum","0","bizWebService.bizQueryOrgEmp",bizDataXML};
        Object response=call.invoke( param );
        System.out.println(" CALL BIZ RESULT IS  " + (String)response);
    }*/
}
