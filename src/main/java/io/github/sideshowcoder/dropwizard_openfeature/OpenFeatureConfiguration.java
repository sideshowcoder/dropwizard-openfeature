package io.github.sideshowcoder.dropwizard_openfeature;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sideshowcoder.dropwizard_openfeature.validation.ValidEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class OpenFeatureConfiguration {

    @Valid
    @JsonProperty
    private FlagdConfiguration flagd = new FlagdConfiguration();

    @Valid
    @JsonProperty
    private OpenFeatureHealthCheckConfiguration healthcheck = new OpenFeatureHealthCheckConfiguration();

    @JsonProperty
    @ValidEnum(enumClass = ProviderType.class)
    @NotNull
    private String provider;

    public FlagdConfiguration getFlagd() {
        return flagd;
    }

    public void setFlagd(FlagdConfiguration flagd) {
        this.flagd = flagd;
    }

    public OpenFeatureHealthCheckConfiguration getHealthcheck() {
        return healthcheck;
    }

    public void setHealthcheck(OpenFeatureHealthCheckConfiguration healthcheck) {
        this.healthcheck = healthcheck;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public ProviderType getProviderType() {
        return ProviderType.valueOf(provider.toUpperCase());
    }
}
