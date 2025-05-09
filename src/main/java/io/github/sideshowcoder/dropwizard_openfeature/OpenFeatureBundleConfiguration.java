package io.github.sideshowcoder.dropwizard_openfeature;

/**
 * TODO extract Bundle out of Scratchpad
 * TODO move flagd to test container
 * TODO Add tests for OpenFeatureBundle integration test -> FlagD with offlineflags, and inmemory provider
 * TODO add Documentation
 * TODO Message OpenFeature folks
 * TODO Message Dropwizard folks
 */

public interface OpenFeatureBundleConfiguration {
    OpenFeatureBundleFactory getOpenFeatureBundleFactory();
}
