package io.github.sideshowcoder.dropwizard_openfeature;

import dev.openfeature.contrib.providers.flagd.FlagdProvider;
import dev.openfeature.sdk.FeatureProvider;
import dev.openfeature.sdk.OpenFeatureAPI;
import dev.openfeature.sdk.providers.memory.InMemoryProvider;
import io.dropwizard.core.ConfiguredBundle;
import io.dropwizard.core.setup.Environment;
import io.github.sideshowcoder.dropwizard_openfeature.health.OpenFeatureHealthCheck;

import java.util.Map;

public class OpenFeatureBundle implements ConfiguredBundle<OpenFeatureBundleConfiguration> {

    private FeatureProvider featureProvider;

    @Override
    public void run(OpenFeatureBundleConfiguration configuration, Environment environment) {
        var config = configuration.getOpenFeatureConfiguration();
        initializeFeatureProvider(config);
        var manager = new OpenFeatureAPIManager(getFeatureProvider());
        environment.lifecycle().manage(manager);

        if (config.getHealthcheck().isEnabled()) {
            var client = OpenFeatureAPI.getInstance().getClient(config.getHealthcheck().getClientDomain());
            var healthcheck = new OpenFeatureHealthCheck(client);
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
        featureProvider = switch (config.getProviderType()) {
            case FLAGD -> new FlagdProvider(config.getFlagd().getFlagdOptions());
            case INMEMORY -> new InMemoryProvider(Map.of());
        };
    }
}
