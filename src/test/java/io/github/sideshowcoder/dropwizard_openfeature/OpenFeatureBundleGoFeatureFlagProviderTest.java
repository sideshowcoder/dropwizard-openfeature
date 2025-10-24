package io.github.sideshowcoder.dropwizard_openfeature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

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

@Testcontainers
@ExtendWith(DropwizardExtensionsSupport.class)
public class OpenFeatureBundleGoFeatureFlagProviderTest {

    @Container
    private static GenericContainer<?> goFeatureFlagRelay = createGoFeatureFlagContainer();

    private static GenericContainer<?> createGoFeatureFlagContainer() {
        GenericContainer<?> container = new GenericContainer<>(DockerImageName.parse("gofeatureflag/go-feature-flag:latest"))
            .withCopyFileToContainer(MountableFile.forClasspathResource("goff-proxy.yaml"), "/goff/goff-proxy.yaml")
            .withCopyFileToContainer(MountableFile.forClasspathResource("go-feature-flags-flags.yaml"), "/goff/flags.yaml");

        container.setPortBindings(List.of("1031:1031"));
        container.waitingFor(Wait.forHttp("/health"));

        return container;
    }

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
        Client client = OpenFeatureAPI.getInstance().getClient("go-feature-flag-client");
        // GoFeatureFlags requires a target key for all queries!
        EvaluationContext ctx = new ImmutableContext("target");
        assertEquals("red", client.getStringValue("staticstringflag", "not-expected-value", ctx));
    }
}
