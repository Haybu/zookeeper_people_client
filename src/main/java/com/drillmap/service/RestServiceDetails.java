package com.drillmap.service;

import org.codehaus.jackson.map.annotate.JsonRootName;

/**
 * Created by hmohamed on 8/11/14.
 */
@JsonRootName( "serviceDetails" )
public class RestServiceDetails {

    private String version;

    public RestServiceDetails() {}

    public RestServiceDetails(final String v) {
        version = v;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
