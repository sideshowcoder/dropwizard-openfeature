package io.github.sideshowcoder.dropwizard_openfeature;

import dev.openfeature.sdk.providers.memory.Flag;
import io.dropwizard.core.ConfiguredBundle;
import io.dropwizard.core.setup.Environment;

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

    private final Map<String, Flag<?>> inMemoryFlags;

    public OpenFeatureBundle() {
        this(Map.of());
    }

    public OpenFeatureBundle(Map<String, Flag<?>> inMemoryFlags) {
        this.inMemoryFlags = inMemoryFlags;
    }

    @Override
    public void run(OpenFeatureBundleConfiguration configuration, Environment environment) {
        configuration.getOpenFeatureBundleFactory().setFlags(inMemoryFlags);
        configuration.getOpenFeatureBundleFactory().startOpenFeatureAPIManager(environment);
        configuration.getOpenFeatureBundleFactory().registerOpenFeatureHealthCheck(environment);
    }
}
