package com.github.hollykunge.security.gate.wrapper;

import com.github.hollykunge.security.gate.utils.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author LARK
 */
public class EncryptionRequestWrapper extends HttpServletRequestWrapper {

    private byte[] requestBody = new byte[0];

    private int contentLength;

    private long contentLengthLong;

    private Map<String, String> paramMap = new HashMap<>();

    public EncryptionRequestWrapper(HttpServletRequest request) {
        super(request);
        try {
            requestBody = StreamUtils.copyToByteArray(request.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getRequestData() {
        return new String(requestBody);
    }

    public void setRequestData(String requestData) {
        this.requestBody = requestData.getBytes();
        this.contentLength = requestData.getBytes().length;
        this.contentLengthLong = requestData.getBytes().length;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    @Override
    public int getContentLength() {
        return this.contentLength;
    }

    @Override
    public long getContentLengthLong() {
        return this.contentLengthLong;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(requestBody);
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }
        };
    }

    @Override
    public String getParameter(String name) {
        return this.paramMap.get(name);
    }

    @Override
    public String[] getParameterValues(String name) {
        if (paramMap.containsKey(name)) {
            return new String[]{getParameter(name)};
        }
        return super.getParameterValues(name);
    }

}
