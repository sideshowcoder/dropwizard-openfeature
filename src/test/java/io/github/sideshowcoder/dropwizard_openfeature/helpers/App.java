package io.github.sideshowcoder.dropwizard_openfeature.helpers;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.github.sideshowcoder.dropwizard_openfeature.OpenFeatureBundle;

public class App extends Application<Config> {

    private final OpenFeatureBundle openFeatureBundle = new OpenFeatureBundle();

    public OpenFeatureBundle getOpenFeatureBundle() {
        return openFeatureBundle;
    }

    @Override
    public void initialize(final Bootstrap<Config> bootstrap) {
        bootstrap.addBundle(openFeatureBundle);
    }

    @Override
    public void run(Config config, Environment environment) throws Exception {

    }
}
