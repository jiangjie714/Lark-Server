package com.github.hollykunge.security.common.msg;


/**
 * @author: zhhongyu
 * @description: feign返回list类型数据
 * @since: Create in 10:44 2020/4/28
 */
public class FeignListResponse<T> extends FeignResponse {
    String msg;
    FeignListData<T> result;
    public FeignListData<T> getResult() {
        return result;
    }
    public FeignListResponse(){

    }

    public FeignListResponse(String msg, int count, T data){
        this.setMessage(msg);
        this.msg = msg;
        this.result = new FeignListData<>(data,count);
    }
    public class FeignListData<T>{
        private T data;
        private int count;
        public FeignListData(T data,int count){
            this.data = data;
            this.count = count;
        }
        public FeignListData(){}
        public T getData() {
            return data;
        }
        public int getCount() {
            return count;
        }
    }
}
