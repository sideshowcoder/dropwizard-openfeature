package io.github.sideshowcoder.dropwizard_openfeature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.codahale.metrics.health.HealthCheck;

import dev.openfeature.sdk.OpenFeatureAPI;
import dev.openfeature.sdk.Client;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.github.sideshowcoder.dropwizard_openfeature.helpers.App;
import io.github.sideshowcoder.dropwizard_openfeature.helpers.Config;

@ExtendWith(DropwizardExtensionsSupport.class)
public class OpenFeatureBundleGoFeatureFlagProviderTest {

    // TODO start the gofeatureflag/go-feature-flag:latest docker image which is the relay proxy to interact with the provider https://gofeatureflag.org/docs/relay-proxy/getting_started
    // TODO create class to parse the configuration options see https://gofeatureflag.org/docs/relay-proxy/getting_started for available options
    // TODO how can I make this work on github? Can I start docker containers there?

    private static final DropwizardAppExtension<Config> APP = new DropwizardAppExtension<>(
        App.class,
        ResourceHelpers.resourceFilePath("go-feature-flag-provider-config.yml")
    );

    @Test
    public void initializesHealthCheck() throws Exception {
        HealthCheck.Result healthcheckResult = APP.getEnvironment().healthChecks().runHealthCheck("openfeature-health-check");
        assertTrue(healthcheckResult.isHealthy());
    }

    @Test
    public void providesFeatureFlagsViaInMemoryProvider() throws Exception {
        // See flagd-test-flags.json for flag definitions used!
        Client client = OpenFeatureAPI.getInstance().getClient("go-feature-flag-client");
        assertEquals("red", client.getStringValue("static-string-flag", "not-expected-value"));
    }
}
