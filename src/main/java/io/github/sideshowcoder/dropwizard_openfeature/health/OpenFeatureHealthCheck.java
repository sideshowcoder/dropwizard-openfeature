package io.github.sideshowcoder.dropwizard_openfeature.health;

import com.codahale.metrics.health.HealthCheck;
import dev.openfeature.sdk.Client;
import dev.openfeature.sdk.ProviderState;

public class OpenFeatureHealthCheck extends HealthCheck {

    private final Client client;

    public OpenFeatureHealthCheck(Client client) {
        this.client = client;
    }

    @Override
    protected Result check() throws Exception {
        if (client.getProviderState() == ProviderState.READY) {
            return Result.healthy();
        }
        return Result.unhealthy("Client[" + client.getMetadata().getDomain() + "] Provider state " +
                client.getProviderState() + " is not ready.");
    }
}
