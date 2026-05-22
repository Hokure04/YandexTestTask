package org.example.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Settings {

    @JsonProperty("appUrl")
    private String appUrl;

    @JsonProperty("apiUrl")
    private String apiUrl;

    @JsonProperty("timeout")
    private Long timeout;

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }
}
