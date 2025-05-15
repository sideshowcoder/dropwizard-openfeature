package io.github.sideshowcoder.dropwizard_openfeature;

import dev.openfeature.sdk.OpenFeatureAPI;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.github.sideshowcoder.dropwizard_openfeature.helpers.App;
import io.github.sideshowcoder.dropwizard_openfeature.helpers.Config;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(DropwizardExtensionsSupport.class)
public class OpenFeatureBundleFlagdProviderTest {

    private static final DropwizardAppExtension<Config> APP = new DropwizardAppExtension<>(
            App.class,
            ResourceHelpers.resourceFilePath("flagd-provider-config.yml"));

    @Test
    public void initializesHealthCheck() throws Exception {
        var healthcheckResult = APP.getEnvironment().healthChecks().runHealthCheck("openfeature-health-check");
        assertTrue(healthcheckResult.isHealthy());
    }

    @Test
    public void providesFeatureFlagsViaInMemoryProvider() throws Exception {
        // See flagd-test-flags.json for flag definitions used!
        var client = OpenFeatureAPI.getInstance().getClient("flagd-client");
        assertEquals("red", client.getStringValue("static-string-flag", "not-expected-value"));
    }

}
