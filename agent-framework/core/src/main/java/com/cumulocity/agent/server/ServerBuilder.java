package com.cumulocity.agent.server;

import static com.cumulocity.agent.server.config.PropertiesFactoryBean.DEFAULT_CONFIG_ROOT_DIR;
import static com.google.common.base.Throwables.propagate;
import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Iterables.concat;
import static java.nio.file.Files.exists;
import static org.springframework.boot.logging.LoggingApplicationListener.CONFIG_PROPERTY;

import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import com.cumulocity.agent.server.config.CommonConfiguration;
import com.cumulocity.agent.server.config.PropertiesFactoryBean;
import com.google.common.collect.ImmutableList;

public class ServerBuilder {

    private final Logger log = LoggerFactory.getLogger(ServerBuilder.class);

    public static interface ApplicationBuilder {
        public ServerBuilder application(String id);
    }

    private final InetSocketAddress address;

    private final String applicationId;

    private final Set<String> configurations = new LinkedHashSet<String>();

    private final Set<Class<?>> features = new LinkedHashSet<Class<?>>();

    private String loggingConfiguration;
    
    private boolean webEnvironmentEnabled = true;

    private String configRootDir;
            
    protected InetSocketAddress address() {
        return address;
    }

    public static ApplicationBuilder on(final InetSocketAddress address) {
        return new ApplicationBuilder() {
            @Override
            public ServerBuilder application(String id) {
                return new ServerBuilder(address, id);
            }
        };
    }

    public static ApplicationBuilder create() {
        return new ApplicationBuilder() {
            @Override
            public ServerBuilder application(String id) {
                return new ServerBuilder(null, id);
            }
        };
    }

    private static boolean isConfigurationExists(Path logbackConfig) {

        if (!exists(logbackConfig)) {
            return false;
        }
        return true;

    }

    public static ApplicationBuilder on(final int port) {
        return on(new InetSocketAddress(port));
    }

    private ServerBuilder(InetSocketAddress address, String id) {
        this.address = address;
        this.applicationId = id;
    }

    public ServerBuilder logging(String config) {
        this.loggingConfiguration = config;
        return this;
    }

    /**
     *  file:/etc/{applicationId}/{resource}-default.properties
     *  file:/etc/{applicationId}/{resource}.properties
     *  file:{user.home}/.{applicationId}/{resource}.properties
     *  classpath:META-INF/{applicationId}/{resource}.properties
     *  classpath:META-INF/spring/{resource}.properties
     * 
     * @param resource
     * @return
     */
    public ServerBuilder loadConfiguration(String resource) {
        configurations.add(resource);
        return this;
    }

    public ServerBuilder enable(Class<?>... features) {
        for (Class<?> feature : features) {
            this.features.add(feature);
        }
        return this;
    }
    
    public ServerBuilder useWebEnvironment(boolean webEnvironmentEnabled) {
        this.webEnvironmentEnabled = webEnvironmentEnabled;
        return this;
    }

    class FeatureOrderComparator extends AnnotationAwareOrderComparator {
        @Override
        protected int getOrder(Object obj) {
            Integer order = findOrder(obj);
            return (order != null ? order : 0);
        }
    }

    protected SpringApplicationBuilder context() {
        return configure(new SpringApplicationBuilder());
    }
    
    public SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        StandardEnvironment environment = new StandardEnvironment();
        registerBaseConfiguration(environment);
        loadConfiguration(environment);
        List<Object> features = new ArrayList<Object>(this.features);
        Collections.sort(features, new FeatureOrderComparator());
        Object[] sources = from(concat(common(), features)).toArray(Object.class);
        return builder
            .sources(sources)
            .showBanner(false)
            .registerShutdownHook(false)
            .environment(environment)
            .web(webEnvironmentEnabled);
    }

    private void registerBaseConfiguration(StandardEnvironment environment) {
        final Properties configuration = new Properties();
        confugireLogging(configuration);
        if (address() != null) {
            configuration.setProperty("server.address", address().getHostString());
            configuration.setProperty("server.port", String.valueOf(address().getPort()));
        }
        configuration.setProperty("application.id", applicationId);
        configuration.setProperty("server.contextPath", "/" + applicationId);
        environment.getPropertySources().addLast(new PropertiesPropertySource("base-configuration", configuration));
    }

    private void loadConfiguration(StandardEnvironment environment) {
        for (String resource : configurations) {
            log.debug("registering configuration {}", resource);
            environment.getPropertySources().addLast(new PropertiesPropertySource(resource, loadResource(environment, resource)));
        }
    }

    public void confugireLogging(final Properties configuration) {
        final String config = findLoggingConfiguration();
        if (config != null) {
            configuration.setProperty(CONFIG_PROPERTY, config);
            System.setProperty(CONFIG_PROPERTY, config);
        } else {
            System.err.println("Application is running without logger configuration.");
        }
    }

    private String findLoggingConfiguration() {
        for (Path path : getConfigurationPaths()) {
            if (isConfigurationExists(path)) {
                System.out.println("configuration founded on path " + path);
                return path.toString();
            } else {
                System.err.println("Not logback configuration found: " + path + ".");
            }
        }

        return null;
    }

    private Collection<Path> getConfigurationPaths() {
        String id = applicationId.toLowerCase();
        Collection<Path> logbackConfig = ImmutableList.of(
                Paths.get(System.getProperty("user.home"), "." + id, loggingConfiguration + ".xml"),
                Paths.get(getConfigRootDir(), id, loggingConfiguration + ".xml"));
        return logbackConfig;
    }

    private ImmutableList<Object> common() {
        return ImmutableList.<Object> of(CommonConfiguration.class);
    }

    public ConfigurableApplicationContext run(String... args) {
        try {
            return context().run(args);
        } catch (Exception ex) {
            log.error("build server failed", ex);
            throw propagate(ex);
        }
    }

    private Properties loadResource(Environment environment, String resource) {
        ResourceLoader loader = new DefaultResourceLoader(this.getClass().getClassLoader());
        PropertiesFactoryBean factoryBean = new PropertiesFactoryBean(applicationId.toLowerCase(), resource, environment, loader, false, getConfigRootDir());
        try {
            return factoryBean.getObject();
        } catch (Exception e) {
            throw new RuntimeException("Unable to load resource " + resource, e);
        }
    }

    public ServerBuilder configurationRootDirectory(String configRootDir) {
        this.configRootDir = configRootDir;
        return this;
    }
    
    private String getConfigRootDir() {
        return configRootDir == null ? DEFAULT_CONFIG_ROOT_DIR : configRootDir;
    }
}
