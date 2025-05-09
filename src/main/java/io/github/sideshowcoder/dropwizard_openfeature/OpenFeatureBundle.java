package io.github.sideshowcoder.dropwizard_openfeature;

import io.dropwizard.core.ConfiguredBundle;
import io.dropwizard.core.setup.Environment;

public class OpenFeatureBundle implements ConfiguredBundle<OpenFeatureBundleConfiguration> {
    @Override
    public void run(OpenFeatureBundleConfiguration configuration, Environment environment) {
        configuration.getOpenFeatureBundleFactory().startOpenFeatureAPIManager(environment);
        configuration.getOpenFeatureBundleFactory().registerOpenFeatureHealthCheck(environment);
    }
}
