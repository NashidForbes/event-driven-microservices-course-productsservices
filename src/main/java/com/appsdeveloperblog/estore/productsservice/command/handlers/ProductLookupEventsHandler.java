package com.appsdeveloperblog.estore.productsservice.command.handlers;

import com.appsdeveloperblog.estore.productsservice.core.events.ProductCreatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
// command handler and query handler belong to this processing group
@ProcessingGroup("product-group")
public class ProductLookupEventsHandler {

    @EventHandler
    public void on(ProductCreatedEvent event){

    }
}
