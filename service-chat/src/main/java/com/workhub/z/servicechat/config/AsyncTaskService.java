package com.workhub.z.servicechat.config;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
*@Description: 多线程任务处理类
*@Param: 
*@return: 
*@Author: 忠
*@date: 2019/4/15
*/
@Service
public class AsyncTaskService {

    @Async // 通过@Async注解表明该方法是一个异步方法，如果注解在类级别，表明该类下所有方法都是异步方法，而这里的方法自动被注入使用ThreadPoolTaskExecutor 作为 TaskExecutor
    public void executeAsyncTask(Integer i){
        System.out.println("执行异步任务：" + i);
    }

    @Async
    public void executeAsyncTaskPlus(Integer i){
        System.out.println("执行异步任务+1：" + (i+1));
    }

    @Async
    public void createGroup(){

    }

    @Async
    public void joinGroup(){
        System.out.println("加入群组");
    }

    @Async
    public void exitGroup(){
        System.out.println("退出群组");
    }

    @Async
    public void saveMessage(){
        System.out.println("发送消息");
    }

    @Async
    public void closeGroup(){
        System.out.println("关闭群组");
    }

    @Async
    public void fileUpload(MultipartFile file,String url) throws Exception {
        FileManage.uploadFile(file,url);
    }

//    @Override
//    public String call() throws Exception {
//        return null;
//    }
}
