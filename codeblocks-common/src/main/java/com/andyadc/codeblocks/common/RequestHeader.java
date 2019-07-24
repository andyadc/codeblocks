package com.andyadc.codeblocks.common;

import java.io.Serializable;

/**
 * @author andy.an
 * @since 2018/4/11
 */
public class RequestHeader implements Serializable {
    private static final long serialVersionUID = 1L;

    private String version;
    private String appId;
    private String requestId;
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

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
