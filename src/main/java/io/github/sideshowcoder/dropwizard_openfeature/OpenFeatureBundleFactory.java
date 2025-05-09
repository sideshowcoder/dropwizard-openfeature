package io.github.sideshowcoder.dropwizard_openfeature;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sideshowcoder.dropwizard_openfeature.health.OpenFeatureHealthCheck;
import io.github.sideshowcoder.dropwizard_openfeature.validation.ValidEnum;
import dev.openfeature.contrib.providers.flagd.FlagdProvider;
import dev.openfeature.sdk.FeatureProvider;
import dev.openfeature.sdk.OpenFeatureAPI;
import dev.openfeature.sdk.providers.memory.Flag;
import dev.openfeature.sdk.providers.memory.InMemoryProvider;
import io.dropwizard.core.setup.Environment;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public class OpenFeatureBundleFactory {

    @Valid
    @JsonProperty
    private FlagdConfiguration flagd = new FlagdConfiguration();

    @Valid
    @JsonProperty
    private OpenFeatureHealthCheckConfiguration healthcheck = new OpenFeatureHealthCheckConfiguration();

    private Map<String, Flag<?>> flags = Map.of();

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

    public Map<String, Flag<?>> getFlags() {
        return flags;
    }

    public void setFlags(Map<String, Flag<?>> flags) {
        this.flags = flags;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    private ProviderType getProviderType() {
        return ProviderType.valueOf(provider.toUpperCase());
    }

    private FeatureProvider createFeatureProvider() {
        return switch (getProviderType()) {
            case FLAGD -> new FlagdProvider(getFlagd().getFlagdOptions());
            case INMEMORY -> new InMemoryProvider(flags);
        };
    }

    public void startOpenFeatureAPIManager(Environment environment) {
        var manager = new OpenFeatureAPIManager(createFeatureProvider());
        environment.lifecycle().manage(manager);
    }

    public void registerOpenFeatureHealthCheck(Environment environment) {
        if (!healthcheck.isEnabled()) {
            return;
        }

        var client = OpenFeatureAPI.getInstance().getClient(healthcheck.getClientDomain());
        var healthcheck = new OpenFeatureHealthCheck(client);
        environment.healthChecks().register(this.healthcheck.getName(), healthcheck);
    }
}
