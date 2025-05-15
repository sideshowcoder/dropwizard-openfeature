package io.github.sideshowcoder.dropwizard_openfeature;

import dev.openfeature.contrib.providers.flagd.FlagdProvider;
import dev.openfeature.sdk.FeatureProvider;
import dev.openfeature.sdk.OpenFeatureAPI;
import dev.openfeature.sdk.providers.memory.InMemoryProvider;
import io.dropwizard.core.ConfiguredBundle;
import io.dropwizard.core.setup.Environment;
import io.github.sideshowcoder.dropwizard_openfeature.health.OpenFeatureHealthCheck;

import java.util.Map;

/**
 * TODO tests for OpenFeatureBundle integration into a Dropwizard Application
 * TODO tests for configured flagd provider with flags serverd from offlineflagspath
 * TODO tests for configured flagd provider with flags read from file via running flagd in testcontainer
 * TODO tests for InMemoryProvider serving flags
 * TODO github workflow to publish to github maven
 * TODO publish to maven-central
 * TODO get listed in dropwizard modules
 * TODO get listed in openfeature.dev
 */
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
