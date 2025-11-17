package io.github.sideshowcoder.dropwizard_openfeature;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.annotation.JsonProperty;

import dev.openfeature.contrib.providers.ofrep.OfrepProviderOptions;
import jakarta.validation.constraints.NotNull;

/**
 * Maps the ofrep confiuration in yaml to the actual {@link OfrepProviderOptions}.
 *
 * Supported options
 *
 * Name | Type | example | required | description
 * baseurl | String |	http://localhost:8016 | yes | baseurl for the ofrep protocol to use
 * timeout | long | 500 | no | request timeout in millis
 * threadcount | int | 5 | no | thread pool size for the http client
 */
public class OfrepConfiguration {

    @JsonProperty
    @NotNull
    private String baseurl;

    @JsonProperty
    private long timeout;

    @JsonProperty
    private int threadcount;

    public OfrepProviderOptions getOfrepProviderOptions() {
        OfrepProviderOptions.Builder builder = OfrepProviderOptions.builder().baseUrl(baseurl);

        if (timeout != 0) {
            Duration requestTimeout = Duration.ofMillis(timeout);
            builder.requestTimeout(requestTimeout);
        }

        if (threadcount != 0) {
            Executor executor = Executors.newFixedThreadPool(threadcount);
            builder.executor(executor);
        }

        return builder.build();
    }
}

