package io.github.sideshowcoder.dropwizard_openfeature;

import com.fasterxml.jackson.annotation.JsonProperty;

import dev.openfeature.contrib.providers.gofeatureflag.GoFeatureFlagProviderOptions;
import dev.openfeature.contrib.providers.gofeatureflag.GoFeatureFlagProviderOptions.GoFeatureFlagProviderOptionsBuilder;
import jakarta.validation.constraints.NotNull;

public class GoFeatureFlagConfiguration {

    @JsonProperty
    @NotNull
    private String endpoint;

    public GoFeatureFlagProviderOptions getGoFeatureFlagProviderOptions() {
        GoFeatureFlagProviderOptionsBuilder builder = GoFeatureFlagProviderOptions.builder();

        builder.endpoint(endpoint);
        
        return builder.build();
    }

}
