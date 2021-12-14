package com.appsdeveloperblog.estore.productsservice.query;

import com.appsdeveloperblog.estore.productsservice.core.events.ProductCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

//Handles ProductEvents and initiates an action
// depending upon the event
@Component
public class ProductsEventHandler {

    @EventHandler
    public void on(ProductCreatedEvent event){

    }
}
