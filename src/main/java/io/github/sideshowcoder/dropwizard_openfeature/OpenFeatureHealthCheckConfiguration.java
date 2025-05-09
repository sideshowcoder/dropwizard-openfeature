package io.github.sideshowcoder.dropwizard_openfeature;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OpenFeatureHealthCheckConfiguration {

    @JsonProperty
    private String clientDomain = "openfeature-bundle-health-check";

    @JsonProperty
    private String name = "openfeature-health-check";

    @JsonProperty
    private boolean enabled = true;

    public String getClientDomain() {
        return clientDomain;
    }

    public void setClientDomain(String clientDomain) {
        this.clientDomain = clientDomain;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
