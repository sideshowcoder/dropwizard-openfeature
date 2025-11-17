package io.github.sideshowcoder.dropwizard_openfeature.helpers;

import java.util.List;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

public class GoFeatureFlagContainer extends GenericContainer<GoFeatureFlagContainer> {

    public static final DockerImageName GO_FEATURE_FLAG_DOCKER_IMAGE_NAME = DockerImageName.parse("gofeatureflag/go-feature-flag:latest");

    private static final String CONTAINER_PROXY_CONFIG_PATH = "/goff/goff-proxy.yaml";
    private static final String CONTAINER_FLAGS_CONFIG_PATH = "/goff/flags.yaml";
    private static final String DEFAULT_PROXY_RESOURCE_PATH = "goff-proxy.yaml";
    private static final String DEFAULT_FLAGS_RESOURCE_PATH = "go-feature-flags-flags.yaml";


    public static GoFeatureFlagContainer create() {
        return new GoFeatureFlagContainer(GO_FEATURE_FLAG_DOCKER_IMAGE_NAME)
            .withProxyConfiguration(DEFAULT_PROXY_RESOURCE_PATH)
            .withFlagsConfiguration(DEFAULT_FLAGS_RESOURCE_PATH);
    }

    public GoFeatureFlagContainer(DockerImageName imageName) {
        super(imageName);
        imageName.assertCompatibleWith(GO_FEATURE_FLAG_DOCKER_IMAGE_NAME);
    }

    public GoFeatureFlagContainer withProxyConfiguration(String resourceName) {
        withCopyFileToContainer(MountableFile.forClasspathResource(resourceName), CONTAINER_PROXY_CONFIG_PATH);
        return self();

    }

    public GoFeatureFlagContainer withFlagsConfiguration(String resourceName) {
        withCopyFileToContainer(MountableFile.forClasspathResource(resourceName), CONTAINER_FLAGS_CONFIG_PATH);
        return self();
    }

    @Override
    protected void configure() {
        setPortBindings(List.of("1031:1031"));
        waitingFor(Wait.forHttp("/health"));
    }
}
