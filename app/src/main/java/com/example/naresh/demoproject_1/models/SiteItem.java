package com.example.naresh.demoproject_1.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by naresh on 3/4/17.
 */

public class SiteItem implements Serializable {


    @SerializedName("icon_url")
    private String iconUrl;
    private String audience;
    @SerializedName("site_url")
    private String siteUrl;
    @SerializedName("api_site_parameter")
    private String apiSiteParameter;
    private String name;


    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getApiSiteParameter() {
        return apiSiteParameter;
    }

    public void setApiSiteParameter(String apiSiteParameter) {
        this.apiSiteParameter = apiSiteParameter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
