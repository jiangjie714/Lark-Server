package com.github.hollykunge.security.common.handler;

import com.github.hollykunge.security.common.feign.*;
import java.util.concurrent.*;

/**
 * @author: zhhongyu
 * @description: 系统错误处理类
 * @since: Create in 16:55 2020/5/19
 */
public class ErrorMessageHandler {
    private static ErrorLogFeign errorLog;
    private static AdminErrorFeign adminFeign;

    /**
     * 线程池
     */
    private ExecutorService executorService = Executors.newFixedThreadPool(100);

    /**
     * 待发送的队列
     */
    private static BlockingQueue<ErrorMessageEntity> queue = new LinkedBlockingQueue<>();

    private ErrorMessageHandler() {
        take();
    }

    /**
     * 内部静态工厂类，用于在初始化的时候生产一个处理器
     */
    private static class ErrorMessageHandlerFactory {
        private static ErrorMessageHandler sender = new ErrorMessageHandler();
    }

    public void push(ErrorMessageEntity errorMessageEntity) throws Exception {
        queue.put(errorMessageEntity);
    }

    /**
     * 获取单例对象
     * @return
     */
    public static ErrorMessageHandler getInstance(ErrorLogFeign errorLogFeign,AdminErrorFeign adminErrorFeign) {
        errorLog = errorLogFeign;
        adminFeign = adminErrorFeign;
        return ErrorMessageHandlerFactory.sender;
    }

    private void take() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    ErrorMessageEntity errorMessageEntity = queue.take();
                    //执行发送kafka任务
                    executorService.execute(new SendKafkaMessageTask(errorLog,errorMessageEntity));
                    //执行发送adminjob
                    executorService.execute(new SendAdminMessageTask(adminFeign,errorMessageEntity));
                } catch (InterruptedException e) {
                    //todo 第二个过程报错处理，线程池没有执行成功
                }
            }
        });
        thread.start();
    }
}
