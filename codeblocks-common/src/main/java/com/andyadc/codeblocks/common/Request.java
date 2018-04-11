package com.andyadc.codeblocks.common;

import java.io.Serializable;

/**
 * @author andy.an
 * @since 2018/4/11
 */
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private String version;
    private String appId;
    private String traceId;
    private String accessToken;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
