package io.github.sideshowcoder.dropwizard_openfeature;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.openfeature.contrib.providers.flagd.Config;
import io.dropwizard.configuration.ConfigurationValidationException;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FlagdConfigurationTest {

    private final ObjectMapper mapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<FlagdConfiguration> factory =
            new YamlConfigurationFactory<>(FlagdConfiguration.class, validator, mapper, "");

    @Test
    public void providesFlagdOptions() throws Exception {
        var configuration = factory.build(new ResourceConfigurationSourceProvider(), "basic-flagd-config.yml");
        assertEquals("flagd", configuration.getFlagdOptions().getHost());
        assertEquals(8082, configuration.getFlagdOptions().getPort());
        assertEquals(Config.Resolver.RPC, configuration.getFlagdOptions().getResolverType());
        assertEquals("disabled", configuration.getFlagdOptions().getCacheType());
    }

    @Test
    public void rejectsUnknownResolver() throws Exception {
        assertThrows(ConfigurationValidationException.class,
                () -> factory.build(new ResourceConfigurationSourceProvider(), "invalid-resolver-flagd-config.yml"));
    }

    @Test
    public void rejectsUnknownCacheType() throws Exception {
        assertThrows(ConfigurationValidationException.class,
                () -> factory.build(new ResourceConfigurationSourceProvider(), "invalid-cache-flagd-config.yml"));
    }
}
