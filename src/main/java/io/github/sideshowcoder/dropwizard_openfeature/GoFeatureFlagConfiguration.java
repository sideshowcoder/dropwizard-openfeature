package io.github.sideshowcoder.dropwizard_openfeature;

import com.fasterxml.jackson.annotation.JsonProperty;

import dev.openfeature.contrib.providers.gofeatureflag.GoFeatureFlagProviderOptions;
import dev.openfeature.contrib.providers.gofeatureflag.GoFeatureFlagProviderOptions.GoFeatureFlagProviderOptionsBuilder;
import dev.openfeature.contrib.providers.gofeatureflag.exception.InvalidOptions;
import jakarta.validation.constraints.NotNull;

public class GoFeatureFlagConfiguration {

    @JsonProperty
    @NotNull
    private String endpoint;

    public GoFeatureFlagProviderOptions getGoFeatureFlagProviderOptions() throws InvalidOptions {
        GoFeatureFlagProviderOptionsBuilder builder = GoFeatureFlagProviderOptions.builder();

        builder.endpoint(endpoint);
        
        GoFeatureFlagProviderOptions options = builder.build();
        options.validate();
        return options;
    }

}
