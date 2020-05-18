package com.github.hollykunge.security.gate.wrapper;

import com.github.hollykunge.security.gate.utils.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author LARK
 */
public class EncryptionRequestWrapper extends HttpServletRequestWrapper {

    private final Map<String, String> customHeaders;

    private byte[] requestBody = new byte[0];

    private int contentLength;

    private long contentLengthLong;

    private Map<String, String> paramMap = new HashMap<>();

    public EncryptionRequestWrapper(HttpServletRequest request) {
        super(request);
        try {
            this.customHeaders = new HashMap<String, String>();
            this.contentLength = request.getContentLength();
            this.contentLengthLong = request.getContentLength();
            requestBody = StreamUtils.copyToByteArray(request.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public String getRequestData() {
        return new String(requestBody);
    }

    public void setRequestData(String requestData) {
        this.requestBody = requestData.getBytes();
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public void putHeader(String name, String value) {
        this.customHeaders.put(name, value);
    }

    public void setContentLength(int value){
        this.contentLength = value;
    }
    public void setContentLengthLong(long value){
        this.contentLengthLong = value;
    }
    @Override
    public String getHeader(String name) {
        String headerValue = customHeaders.get(name);
        if (headerValue != null) {
            return headerValue;
        }
        return ((HttpServletRequest) getRequest()).getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {

        Set<String> set = new HashSet<String>(customHeaders.keySet());

        @SuppressWarnings("unchecked")
        Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaderNames();
        while (e.hasMoreElements()) {
            String n = e.nextElement();
            set.add(n);
        }

        return Collections.enumeration(set);
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
