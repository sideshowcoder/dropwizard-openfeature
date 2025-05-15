package io.github.sideshowcoder.dropwizard_openfeature;

import dev.openfeature.sdk.FeatureProvider;
import dev.openfeature.sdk.OpenFeatureAPI;
import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenFeatureAPIManager implements Managed {
    private static final Logger log = LoggerFactory.getLogger(OpenFeatureAPIManager.class);
    private final FeatureProvider featureProvider;

    public OpenFeatureAPIManager(FeatureProvider featureProvider) {
        this.featureProvider = featureProvider;
    }

    @Override
    public void start() throws Exception {
        OpenFeatureAPI.getInstance().setProviderAndWait(featureProvider);
        log.info("Started {}", this.getClass().getSimpleName());
    }

    @Override
    public void stop() throws Exception {
        OpenFeatureAPI.getInstance().shutdown();
        log.info("Stopped {}", this.getClass().getSimpleName());
    }
}
