package io.github.sideshowcoder.dropwizard_openfeature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public GenericContainer goFeatureFlagRelay = new GenericContainer<>(DockerImageName.parse("gofeatureflag/go-feature-flag:latest"))
        .withExposedPorts(1031)
        .withCopyFileToContainer(MountableFile.forClasspathResource("goff-proxy.yaml"), "/goff/goff-proxy.yaml")
        .withCopyFileToContainer(MountableFile.forClasspathResource("go-feature-flags-flags.yaml"), "/goff/flags.yaml")
        .waitingFor(Wait.forHttp("/health"));

    // TODO need to start container before dropwizard app! Need to bundle it in some kind of @BeforeAll or something to start the dropwizard app.
    // TODO start the gofeatureflag/go-feature-flag:latest docker image which is the relay proxy to interact with the provider https://gofeatureflag.org/docs/relay-proxy/getting_started
    // TODO set endpoint to the endpoint provider via the docker container
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
        Client client = OpenFeatureAPI.getInstance().getClient("go-feature-flag-client");
        assertEquals("red", client.getStringValue("staticstringflag", "not-expected-value"));
    }
}
