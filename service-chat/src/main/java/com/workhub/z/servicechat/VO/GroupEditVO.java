/**
  * Copyright 2019 bejson.com 
  */
package com.workhub.z.servicechat.VO;

import com.workhub.z.servicechat.model.GroupTaskDto;

/**
 * Auto-generated: 2019-06-04 17:23:52
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class GroupEditVO {

    private int code;
    private GroupTaskDto data;
    public void setCode(int code) {
         this.code = code;
     }
     public int getCode() {
         return code;
     }

    public void setData(GroupTaskDto data) {
         this.data = data;
     }
     public GroupTaskDto getData() {
         return data;
     }

}