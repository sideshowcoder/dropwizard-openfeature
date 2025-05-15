package io.github.sideshowcoder.dropwizard_openfeature;

import dev.openfeature.sdk.OpenFeatureAPI;
import dev.openfeature.sdk.providers.memory.Flag;
import dev.openfeature.sdk.providers.memory.InMemoryProvider;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.github.sideshowcoder.dropwizard_openfeature.helpers.App;
import io.github.sideshowcoder.dropwizard_openfeature.helpers.Config;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(DropwizardExtensionsSupport.class)
public class OpenFeatureBundleInMemoryProviderTest {

    private static final DropwizardAppExtension<Config> APP = new DropwizardAppExtension<>(
            App.class,
            ResourceHelpers.resourceFilePath("in-memory-provider-config.yml"));

    @Test
    public void initializesHealthCheck() throws Exception {
        var healthcheckResult = APP.getEnvironment().healthChecks().runHealthCheck("openfeature-health-check");
        assertTrue(healthcheckResult.isHealthy());
    }

    @Test
    public void providesFeatureFlagsViaInMemoryProvider() throws Exception {
        var expectedValue = "expected-value";
        var flagKey = "flag-key";
        var flag = Flag.builder().variant("variant-key", expectedValue).defaultVariant("variant-key").build();
        var flags = Map.<String, Flag<?>>of(flagKey, flag);

        // set the in memory flags
        var bundle = ((App) APP.getApplication()).getOpenFeatureBundle();
        var provider = (InMemoryProvider) bundle.getFeatureProvider();
        provider.updateFlags(flags);

        var client = OpenFeatureAPI.getInstance().getClient("in-memory-provider-client");
        assertEquals(expectedValue, client.getStringValue(flagKey, "not-expected-value"));
    }
}
