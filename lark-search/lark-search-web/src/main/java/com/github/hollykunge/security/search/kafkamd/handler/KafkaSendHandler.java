package com.github.hollykunge.security.search.kafkamd.handler;

import com.github.hollykunge.security.common.util.ExceptionCommonUtil;
import com.github.hollykunge.security.search.kafkamd.constants.KafkaConstants;
import com.github.hollykunge.security.search.kafkamd.task.KafkaSendWithoutResultTask;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;

/**
 * kafka发送处理器，多线程调度
 * 单例模式
 *
 * @author zhhongyu
 */
@Slf4j
public class KafkaSendHandler {

    private KafkaSendHandler() {
        takeDataToProcess();
    }

    /**
     * 内部静态工厂类，用于在初始化的时候生产一个处理器
     */
    private static class KafkaSendHandlerFactory {
        private static KafkaSendHandler kafkaSender = new KafkaSendHandler();
    }

    /**
     * 获取单例对象
     * @return
     */
    public static KafkaSendHandler getInstance() {
        return KafkaSendHandlerFactory.kafkaSender;
    }

    /**
     * 线程池
     */
    private ExecutorService executorService = Executors.newFixedThreadPool(100);
//    private ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * 待发送的队列
     */
    private static BlockingQueue<Map> queue = new LinkedBlockingQueue<>();

    /**
     * 把待发送的消息放进队列
     *
     * @param topic 主题
     * @param obj   消息体
     */
    public void pushDataToQueue(String topic, String obj) throws Exception{
        Map data = new ConcurrentHashMap();
        data.put(KafkaConstants.KAFKA_SERVER_TOPIC, topic);
        data.put(KafkaConstants.KAFKA_SERVER_MESSAGE, obj);
        queue.offer(data);
    }

    /**
     * 不停的循环从队列中拿消息进行发送
     */
    public void takeDataToProcess() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Map message = queue.take();
                    String topic = (String) message.get(KafkaConstants.KAFKA_SERVER_TOPIC);
                    String msg = (String) message.get(KafkaConstants.KAFKA_SERVER_MESSAGE);
                    log.info("从队列拿到kafka消息主题：{}，kafka消息内容：{}", topic, msg);
                    //第一种：不返回发送结果
                    executorService.execute(new KafkaSendWithoutResultTask(topic, msg));
                    //第二种：返回发送结果，并打印
                } catch (InterruptedException e) {
                    log.error(ExceptionCommonUtil.getExceptionMessage(e));
                } catch (Exception e) {
                    log.error(ExceptionCommonUtil.getExceptionMessage(e));
                }
            }
        });
        thread.start();
    }

}
