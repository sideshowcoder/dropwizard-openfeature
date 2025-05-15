package io.github.sideshowcoder.dropwizard_openfeature.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;
import io.github.sideshowcoder.dropwizard_openfeature.OpenFeatureBundleConfiguration;
import io.github.sideshowcoder.dropwizard_openfeature.OpenFeatureConfiguration;

public class Config extends Configuration implements OpenFeatureBundleConfiguration {

    private OpenFeatureConfiguration openFeatureConfiguration;

    @Override
    public OpenFeatureConfiguration getOpenFeatureConfiguration() {
        return openFeatureConfiguration;
    }

    @JsonProperty("openfeature")
    public void setOpenFeatureConfiguration(OpenFeatureConfiguration openFeatureConfiguration) {
        this.openFeatureConfiguration = openFeatureConfiguration;
    }
}
