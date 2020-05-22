package com.github.hollykunge.security.gate.utils;

import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.common.feign.LarkFeignFactory;
import com.github.hollykunge.security.gate.constants.GateConstants;
import com.github.hollykunge.security.gate.dto.LogInfoDto;
import com.github.hollykunge.security.gate.feign.LarkLogFeign;
import com.github.hollykunge.security.log.dto.kafka.MessageDto;
import com.github.hollykunge.security.log.dto.kafka.TopicDto;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * ${DESCRIPTION}
 *
 * @author 协同设计小组
 * @create 2017-07-01 15:28
 */
@Slf4j
public class DBLog extends Thread {
    private static DBLog dblog = null;
    private static BlockingQueue<LogInfoDto> logInfoQueue = new LinkedBlockingQueue<LogInfoDto>(1024);

    public LarkLogFeign getLogService() {
        return logFeign;
    }

    public DBLog setLogService(LarkLogFeign logFeign) {
        if(this.logFeign==null) {
            this.logFeign = LarkFeignFactory.getInstance().loadFeign(logFeign);
        }
        return this;
    }

//    private AdminLogServiceFeignClient logService;
    private LarkLogFeign logFeign;
    public static synchronized DBLog getInstance() {
        if (dblog == null) {
            dblog = new DBLog();
        }
        return dblog;
    }

    private DBLog() {
        super("CLogOracleWriterThread");
    }

    public void offerQueue(LogInfoDto logInfo) {
        try {
            logInfoQueue.offer(logInfo);
        } catch (Exception e) {
            log.error("日志写入失败", e);
        }
    }

    @Override
    public void run() {
        List<LogInfoDto> bufferedLogList = new ArrayList<LogInfoDto>(); // 缓冲队列
        while (true) {
            try {
                bufferedLogList.add(logInfoQueue.take());
                logInfoQueue.drainTo(bufferedLogList);
                if (bufferedLogList != null && bufferedLogList.size() > 0) {
                    // 写入日志
                    for(LogInfoDto log:bufferedLogList){
                        //发送kafka消息，同步到es中
                        if(log != null){
                            TopicDto topicDto = new TopicDto();
                            topicDto.setTopicName(GateConstants.GATE_LOG_TOPIC);
                            MessageDto messageDto = new MessageDto();
                            messageDto.setMessage(JSONObject.toJSONString(log));
                            topicDto.setMessage(messageDto);
                            logFeign.sendKafka(topicDto);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 防止缓冲队列填充数据出现异常时不断刷屏
                try {
                    Thread.sleep(1000);
                } catch (Exception eee) {
                }
            } finally {
                if (bufferedLogList != null && bufferedLogList.size() > 0) {
                    try {
                        bufferedLogList.clear();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
}
