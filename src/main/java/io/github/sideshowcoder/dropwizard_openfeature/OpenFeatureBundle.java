package io.github.sideshowcoder.dropwizard_openfeature;

import dev.openfeature.contrib.providers.flagd.FlagdProvider;
import dev.openfeature.contrib.providers.gofeatureflag.GoFeatureFlagProvider;
import dev.openfeature.contrib.providers.gofeatureflag.GoFeatureFlagProviderOptions;
import dev.openfeature.contrib.providers.gofeatureflag.exception.InvalidOptions;
import dev.openfeature.sdk.Client;
import dev.openfeature.sdk.FeatureProvider;
import dev.openfeature.sdk.OpenFeatureAPI;
import dev.openfeature.sdk.providers.memory.InMemoryProvider;
import io.dropwizard.core.ConfiguredBundle;
import io.dropwizard.core.setup.Environment;
import io.github.sideshowcoder.dropwizard_openfeature.health.OpenFeatureHealthCheck;

import java.util.HashMap;

public class OpenFeatureBundle implements ConfiguredBundle<OpenFeatureBundleConfiguration> {

    private FeatureProvider featureProvider;

    @Override
    public void run(OpenFeatureBundleConfiguration configuration, Environment environment) {
        OpenFeatureConfiguration config = configuration.getOpenFeatureConfiguration();
        initializeFeatureProvider(config);
        OpenFeatureAPIManager manager = new OpenFeatureAPIManager(getFeatureProvider());
        environment.lifecycle().manage(manager);

        if (config.getHealthcheck().isEnabled()) {
            Client client = OpenFeatureAPI.getInstance().getClient(config.getHealthcheck().getClientDomain());
            OpenFeatureHealthCheck healthcheck = new OpenFeatureHealthCheck(client);
            environment.healthChecks().register(config.getHealthcheck().getName(), healthcheck);
        }
    }

    public FeatureProvider getFeatureProvider() {
        if (featureProvider == null) {
            throw new IllegalStateException("FeatureProvider has not been initialized");
        }
        return featureProvider;
    }

    private synchronized void initializeFeatureProvider(OpenFeatureConfiguration config) {
        switch (config.getProviderType()) {
            case INMEMORY:
                featureProvider = new InMemoryProvider(new HashMap<>());
                break;
            case FLAGD:
                featureProvider = new FlagdProvider(config.getFlagd().getFlagdOptions());
                break;
            case GOFEATUREFLAG:
                try {
                    featureProvider = new GoFeatureFlagProvider(config.getGoFeatureFlag().getGoFeatureFlagProviderOptions());
                } catch(InvalidOptions e) {
                    new RuntimeException(e);
                }
                break;
        }
    }
}
