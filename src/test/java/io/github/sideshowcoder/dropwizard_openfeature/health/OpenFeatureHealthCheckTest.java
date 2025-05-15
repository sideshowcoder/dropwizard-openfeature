package io.github.sideshowcoder.dropwizard_openfeature.health;

import dev.openfeature.sdk.Client;
import dev.openfeature.sdk.ClientMetadata;
import dev.openfeature.sdk.ProviderState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OpenFeatureHealthCheckTest {

    @Mock
    private Client client;

    @Mock
    private ClientMetadata metadata;

    private OpenFeatureHealthCheck healthCheck;

    @BeforeEach
    public void setUp() {
        healthCheck = new OpenFeatureHealthCheck(client);
    }

    @Test
    public void testProviderHealthy() throws Exception {
        when(client.getProviderState()).thenReturn(ProviderState.READY);
        assertTrue(healthCheck.check().isHealthy());
    }

    @Test
    public void testProviderUnhealthy() throws Exception {
        when(client.getProviderState()).thenReturn(ProviderState.NOT_READY);
        when(client.getMetadata()).thenReturn(metadata);
        when(metadata.getDomain()).thenReturn("domain");

        var result = healthCheck.check();

        assertFalse(result.isHealthy());
    }
}
