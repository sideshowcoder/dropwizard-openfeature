package io.github.sideshowcoder.dropwizard_openfeature;

import dev.openfeature.sdk.FeatureProvider;
import dev.openfeature.sdk.OpenFeatureAPI;
import io.dropwizard.lifecycle.Managed;

public class OpenFeatureAPIManager implements Managed {
    private final FeatureProvider featureProvider;

    public OpenFeatureAPIManager(FeatureProvider featureProvider) {
        this.featureProvider = featureProvider;
    }

    @Override
    public void start() throws Exception {
        OpenFeatureAPI.getInstance().setProviderAndWait(featureProvider);
    }

    @Override
    public void stop() throws Exception {
        OpenFeatureAPI.getInstance().shutdown();
    }
}
