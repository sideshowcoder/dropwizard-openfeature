package io.github.sideshowcoder.dropwizard_openfeature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.codahale.metrics.health.HealthCheck;

import dev.openfeature.sdk.Client;
import dev.openfeature.sdk.EvaluationContext;
import dev.openfeature.sdk.ImmutableContext;
import dev.openfeature.sdk.OpenFeatureAPI;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.github.sideshowcoder.dropwizard_openfeature.helpers.App;
import io.github.sideshowcoder.dropwizard_openfeature.helpers.Config;
import io.github.sideshowcoder.dropwizard_openfeature.helpers.GoFeatureFlagContainer;

@Testcontainers
@ExtendWith(DropwizardExtensionsSupport.class)
public class OpenFeatureBundleOfrepProviderTest {

    @Container
    private static GoFeatureFlagContainer container = GoFeatureFlagContainer.create();

    private static final DropwizardAppExtension<Config> APP = new DropwizardAppExtension<>(
        App.class,
        ResourceHelpers.resourceFilePath("ofrep-provider-config.yml")
    );

    @Test
    public void initializesHealthCheck() throws Exception {
        HealthCheck.Result healthcheckResult = APP.getEnvironment().healthChecks().runHealthCheck("openfeature-health-check");
        assertTrue(healthcheckResult.isHealthy());
    }

    @Test
    public void providesFeatureFlagsOfrepProvider() throws Exception {
        Client client = OpenFeatureAPI.getInstance().getClient("ofrep-client");
        // GoFeatureFlags requires a target key for all queries even using ofrep protocol!
        EvaluationContext ctx = new ImmutableContext("target");
        assertEquals("red", client.getStringValue("staticstringflag", "not-expected-value", ctx));
    }
}
