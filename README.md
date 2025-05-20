[![Maven Central](https://maven-badges.sml.io/sonatype-central/io.github.sideshowcoder/dropwizard-openfeature/badge.svg)](https://maven-badges.sml.io/sonatype-central/io.github.sideshowcoder/dropwizard-openfeature) ![Maven Test & Build](https://github.com/sideshowcoder/dropwizard-openfeature/actions/workflows/maven-build.yml/badge.svg)

# Dropwizard Openfeature

This plugin integrates [openfeature][1] with dropwizard and allows you to use openfeature feature
flags, provided by supported openfeature providers via a managed `OpenFeatureAPI` instance.

Currently only [flagd][2] and the SDKs [InMemoryProvider][3] providers are supported

## Installing the bundle

### Releases from maven central 

[io.github.sideshowcoder/dropwizard-openfeature][4]

```xml
<dependency>
  <groupId>io.github.sideshowcoder</groupId>
  <artifactId>dropwizard-openfeature</artifactId>
  <version>0.0.2</version>
</dependency>
```

### Snapshots form source

```
git clone https://github.com/sideshowcoder/dropwizard-openfeature
cd dropwizard-openfeature
./mvn install
```

After installing the plugin locally you can include it in your `pom.xml`

```xml
<dependency>
  <groupId>io.github.sideshowcoder</groupId>
  <artifactId>dropwizard-openfeature</artifactId>
  <version>0.0.3-SNAPSHOT</version>
</dependency>
```



## Included in the bundle

### Supported providers

The bundle currently supports both the SDK included `InMemoryProvider` as well as `flagd`, the provider can be selected
via the configuration. For details on the configuration options see `FlagdConfiguration` as well the 
[flagd documentation][5].

### OpenFeatureAPI management

The initialized `OpenFeatureAPI` is managed via the dropwizard lifecycle and will be shutdown gracefully upon 
application shutdown, see `OpenFeatureAPIManager`.

### Healthcheck

By default the bundle registers a healthcheck on the state of the provider configured, this healthcheck can be further 
configured via the `OpenFeatureHealthCheckConfiguration`.

## Activating the bundle

Your Dropwizard application configuration class must implement `OpenFeatureBundleConfiguration`

### Configuring dropwizard-openfeature in the dropwizard config file

For a full overview see `OpenFeatureConfiguration`, `OpenFeatureHealthCheckConfiguration`, and `FlagdConfiguration` a 
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
public class Config extends Configuration implements OpenFeatureBundleConfiguration {

    @Valid
    @NotNull
    @JsonProperty
    private OpenFeatureConfiguration openfeature;

    @Override
    public OpenFeatureConfiguration getOpenFeatureConfiguration() {
        return openfeature;
    }
}
```

### Initialization

In your application's `initialize` method, call `bootstrap.addBundle(new OpenFeatureBundle())`:

```java
public class App extends Application<Config> {
    
    @Override
    public void initialize(Bootstrap<MyConfiguration> bootstrap) {
        bootstrap.addBundle(new OpenFeatureBundle());
    }

    @Override
    public void run(Config config, Environment environment) throws Exception {
        /* ... */
    }
}
```

### Using the client

OpenFeature configures a global `OpenFeatureAPI` which grants access to a client, which can be injected as needed, it is 
common practise to provide a domain as an identifier, this is however not required, unless multiple clients are to be 
created.

```java
public class App extends Application<Config> {

    @Override
    public void initialize(Bootstrap<MyConfiguration> bootstrap) {
        bootstrap.addBundle(new OpenFeatureBundle());
    }

    @Override
    public void run(Config config, Environment environment) throws Exception {
        /* ... */
        var client = OpenFeatureAPI.getInstance().getClient("my-application-domain");
        
        var myResource = new MyResource(client);
        environment.jersey().register(myResource);
        
        var myOtherResource = new MyOtherResource(client);
        environment.jersey().register(myResource);
        /* ... */
    }
}
```

### Accessing the underlying feature provider

The bundle exposes access to the underlying feature provider. Useful for runtime configuration and introspection of the 
provider. For example when using the `InMemoryProvider` flags can be updated at runtime for example for testing.

```java
public class App extends Application<Config> {

    private OpenFeatureBundle bundle;
    private InMemoryProvider provider;

    @Override
    public void initialize(Bootstrap<MyConfiguration> bootstrap) {
        bundle = new OpenFeatureBundle();
        bootstrap.addBundle(bundle);
    }

    @Override
    public void run(Config config, Environment environment) throws Exception {
        // ...
        provider = (InMemoryProvider) bundle.getFeatureProvider();
        provider.updateFlags(Map.of(/* ... */));
        // ...
    }
}
```

# Contributors
* [Philipp Fehre](https://github.com/sideshowcoder)

[1]: https://openfeature.dev/
[2]: https://flagd.dev/
[3]: https://github.com/open-feature/java-sdk/blob/main/src/main/java/dev/openfeature/sdk/providers/memory/InMemoryProvider.java
[4]: https://central.sonatype.com/artifact/io.github.sideshowcoder/dropwizard-openfeature
[5]: https://flagd.dev/providers/java/
