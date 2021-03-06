package com.appsdeveloperblog.estore.productsservice;

import com.appsdeveloperblog.estore.productsservice.command.interceptors.CreateProductCommandInterceptor;
import com.appsdeveloperblog.estore.productsservice.core.handlers.errors.ProductsServiceEventsErrorHandler;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class ProductsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductsServiceApplication.class, args);
    }

    // Register Message Dispatch Interceptor
    @Autowired
    public void registerCreateProductCommandInterceptor(ApplicationContext context, CommandBus commandBus) {
        commandBus.registerDispatchInterceptor(context.getBean(CreateProductCommandInterceptor.class));
    }

    // Register ListenerInvocationErrorHandler for rolling back transactions
    // as part of processing group product-group"
    @Autowired
    public void configure(EventProcessingConfigurer config) {
        config.registerListenerInvocationErrorHandler("product-group",
                conf -> new ProductsServiceEventsErrorHandler());

        //If you do not need to create and register a custom error class then you can provide one provided by Axon framework.
/*        config.registerListenerInvocationErrorHandler("product-group",
                conf -> PropagatingErrorHandler.instance());*/
    }

    // Snapshot trigger definition bean for event store for every 3 events
    @Bean(name = "productSnapShotTriggerDefinition")
    public SnapshotTriggerDefinition productSnapshotTriggerDefinition(Snapshotter snapshotter) {
        return new EventCountSnapshotTriggerDefinition(snapshotter, 3);
    }

}
