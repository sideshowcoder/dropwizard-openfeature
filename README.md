# Dropwizard Openfeature

This plugin integrates [openfeature](https://openfeature.dev/) with dropwizard and allows you to use openfeature feature
flags, provided by supported openfeature providers via a managed `OpenFeatureAPI` instance.

Currently only [flagd](https://flagd.dev/) and the SDKs 
[InMemoryProvider](https://github.com/open-feature/java-sdk/blob/main/src/main/java/dev/openfeature/sdk/providers/memory/InMemoryProvider.java) 
providers are supported

## Installing the bundle from source code

```
git clone https://github.com/sideshowcoder/dropwizard-openfeature
cd dropwizard-openfeature
./mvn install
```

After installing the plugin locally you can include it in your pom.xml

```xml
<dependency>
  <groupId>io.github.sideshowcoder</groupId>
  <artifactId>dropwizard-openfeature</artifactId>
  <version>$VERSION</version>
</dependency>
```

## Included in the bundle

### Supported providers

The bundle currently supports both the SDK included `InMemoryProvider` as well as `flagd`, the provider can be selected
via the configuration. For details on the configuration options see `FlagdConfiguration` as well the 
[flagd documentation](https://flagd.dev/providers/java/).

### OpenFeatureAPI management

The initialized `OpenFeatureAPI` is managed via the dropwizard lifecycle.

### Healthcheck

By default the bundle registers a healthcheck on the state of the provider configured, this healthcheck can be further 
configured via the `OpenFeatureHealthCheckConfiguration`.

## Activating the bundle: Configuration

Your Dropwizard application configuration class must implement `OpenFeatureBundleConfiguration`:

## Configuring dropwizard-openfeature in the dropwizard config file

For a full overview see `OpenFeatureBundleFactory`, `OpenFeatureHealthCheckConfiguration`, and `FlagdConfiguration` a 
minimal configuration for flagd runnining locally on the port 8013 would look as follows.

```yaml
openfeature:
  provider: flagd
  flagd:
    host: localhost
    port: 8013
```

For the bundle to have access to the configuration, your application configuration needs to implement 
`OpenFeatureBundleConfiguration`.

```java
public class ApplicationConfiguration implements OpenFeatureBundleConfiguration {

    @Valid
    @NotNull
    @JsonProperty
    private OpenFeatureBundleFactory openfeature;

    @Override
    public OpenFeatureBundleFactory getOpenFeatureBundleFactory() {
        return openfeature;
    }
}
```

## Activating the bundle: Initialization

In your application's `initialize` method, call `bootstrap.addBundle(new OpenFeatureBundle())`:

```java
import io.github.sideshowcoder.dropwizard_openfeature.OpenFeatureBundle;

@Override
public void initialize(Bootstrap<MyConfiguration> bootstrap) {
    bootstrap.addBundle(new OpenFeatureBundle());
}
```

# Contributors
* [Philipp Fehre](https://github.com/sideshowcoder)
