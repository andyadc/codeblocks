package com.andyadc.scaffold.showcase.common.dto;

import java.io.Serializable;

/**
 * @author andy.an
 * @since 2017/10/24
 */
public class BaseRequest implements Serializable {
    private static final long serialVersionUID = 34137137523056691L;

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
