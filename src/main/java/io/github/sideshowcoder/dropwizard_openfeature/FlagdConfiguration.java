package io.github.sideshowcoder.dropwizard_openfeature;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.sideshowcoder.dropwizard_openfeature.validation.ValidEnum;
import dev.openfeature.contrib.providers.flagd.Config;
import dev.openfeature.contrib.providers.flagd.FlagdOptions;
import dev.openfeature.contrib.providers.flagd.resolver.rpc.cache.CacheType;

public class FlagdConfiguration {

    @JsonProperty
    @ValidEnum(enumClass = Config.Resolver.class)
    private String resolver;

    @JsonProperty
    private String host;

    @JsonProperty
    private int port;

    @JsonProperty
    private String targetUri;

    @JsonProperty
    private boolean tls;

    @JsonProperty
    private String defaultAuthority;

    @JsonProperty
    private String socketPath;

    @JsonProperty
    private String certPath;

    @JsonProperty
    private int deadline;

    @JsonProperty
    private int streamDeadlineMs;

    @JsonProperty
    private long keepAlive;

    @JsonProperty
    private String selector;

    @JsonProperty
    private String providerId;

    @JsonProperty
    @ValidEnum(enumClass = CacheType.class)
    private String cacheType;

    @JsonProperty
    private int maxCacheSize;

    @JsonProperty
    private int retryBackoffMs;

    @JsonProperty
    private String offlineFlagSourcePath;

    @JsonProperty
    private int offlinePollIntervalMs;

    public FlagdOptions getFlagdOptions() {
        FlagdOptions.FlagdOptionsBuilder builder = FlagdOptions.builder();
        if (resolver != null) {
            builder.resolverType(Config.Resolver.valueOf(resolver.toUpperCase()));
        }
        if (host != null) {
            builder.host(host);
        }
        if (port != 0) {
            builder.port(port);
        }
        if (targetUri != null) {
            builder.targetUri(targetUri);
        }
        if (tls) {
            builder.tls(tls);
        }
        if (defaultAuthority != null) {
            builder.defaultAuthority(defaultAuthority);
        }
        if (socketPath != null) {
            builder.socketPath(socketPath);
        }
        if (certPath != null) {
            builder.certPath(certPath);
        }
        if (deadline != 0) {
            builder.deadline(deadline);
        }
        if (streamDeadlineMs != 0) {
            builder.streamDeadlineMs(streamDeadlineMs);
        }
        if (keepAlive != 0) {
            builder.keepAlive(keepAlive);
        }
        if (selector != null) {
            builder.selector(selector);
        }
        if (providerId != null) {
            builder.providerId(providerId);
        }
        if (cacheType != null) {
            builder.cacheType(cacheType);
        }
        if (maxCacheSize != 0) {
            builder.maxCacheSize(maxCacheSize);
        }
        if (retryBackoffMs != 0) {
            builder.retryBackoffMs(retryBackoffMs);
        }
        if (offlineFlagSourcePath != null) {
            builder.offlineFlagSourcePath(offlineFlagSourcePath);
        }
        if (offlinePollIntervalMs != 0) {
            builder.offlinePollIntervalMs(offlinePollIntervalMs);
        }
        return builder.build();
    }
}
