package com.github.hollykunge.larkconfig;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class ConfigRequestWrapper extends HttpServletRequestWrapper {
    public ConfigRequestWrapper(HttpServletRequest request) {

        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final byte[] bytes = new byte[0];
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return byteArrayInputStream.read() == -1 ? true:false;

            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }
}
