package com.cumulocity.agent.server;

import static com.google.common.collect.FluentIterable.from;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.cumulocity.agent.server.servers.standalone.StandaloneServer;

public class StandaloneServerBuilder extends SpringServerBuilder<StandaloneServerBuilder> {

    private static final Logger log = LoggerFactory.getLogger(StandaloneServerBuilder.class);

    private final ServerBuilder builder;

    public StandaloneServerBuilder(ServerBuilder builder) {
        this.builder = builder;
    }

    public Server build() {

        final AnnotationConfigApplicationContext parentContext = builder.getContext();

        final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

        applicationContext.setParent(parentContext);

        applicationContext.register(annotatedClasses(StandaloneServer.class));
        if (!packages.isEmpty()) {
            applicationContext.scan(from(packages).toArray(String.class));
        }
        applicationContext.refresh();
        final Server server = applicationContext.getBean(Server.class);
        return new Server() {

            @Override
            public void stop() {
                applicationContext.stop();
                applicationContext.close();
                parentContext.close();
                server.stop();
            }

            @Override
            public void start() {
                try {
                    server.start();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    stop();
                }

            }

            @Override
            public void awaitTerminated() {
                server.awaitTerminated();
            }
        };
    }
}
