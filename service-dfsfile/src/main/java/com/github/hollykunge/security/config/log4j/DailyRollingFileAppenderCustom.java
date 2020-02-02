package com.github.hollykunge.security.config.log4j;

import org.apache.log4j.DailyRollingFileAppender;

import java.io.File;
import java.io.IOException;

/**
 * log4j 日志动态路径
 * @author  fansq
 * @since  19-9-4
 */
public class DailyRollingFileAppenderCustom extends DailyRollingFileAppender {

    @Override
    public synchronized void setFile(String fileName, boolean append, boolean bufferedIO, int bufferSize) throws IOException {
        super.setFile(getLog4jRoute(), append, bufferedIO, bufferSize);

    }

    public  String getLog4jRoute(){
        String logBackRoute = Thread.currentThread().
                getContextClassLoader().getResource("").getPath();
        return  logBackRoute+"/service-dfsfile/logs/all.log";
    }
}
