package com.github.hollykunge.security.common.msg;


/**
 * @author: zhhongyu
 * @description: feign返回list类型数据
 * @since: Create in 10:44 2020/4/28
 */
public class FeignListResponse<T> extends BaseResponse {
    String msg;
    ListData<T> result;
    public ListData<T> getResult() {
        return result;
    }

    public FeignListResponse(String msg, int count, T data){
        this.setMessage(msg);
        this.msg = msg;
        this.result = new ListData<>(data,count);
    }
    public class ListData<T>{
        private T data;
        private int count;
        public ListData(T data,int count){
            this.data = data;
            this.count = count;
        }
        public T getData() {
            return data;
        }
        public int getCount() {
            return count;
        }
    }
}
