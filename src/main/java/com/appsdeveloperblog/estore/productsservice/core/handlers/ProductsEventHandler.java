package com.appsdeveloperblog.estore.productsservice.core.handlers;

import com.appsdeveloperblog.estore.productsservice.core.data.ProductEntity;
import com.appsdeveloperblog.estore.productsservice.core.data.interfaces.ProductsRepository;
import com.appsdeveloperblog.estore.productsservice.core.events.ProductCreatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;


//Handles ProductEvents and initiates an action
// depending upon the event
@Component
@ProcessingGroup("product-group")
public class ProductsEventHandler {

    private final ProductsRepository productsRepository;

    public ProductsEventHandler(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @ExceptionHandler(resultType=Exception.class)
    public void handle(Exception exception) throws Exception {
        // Log error message

        throw exception;
    }

    @ExceptionHandler(resultType=IllegalArgumentException.class)
    public void handle(IllegalArgumentException exception) throws Exception {
        // Log error message

    }

    @EventHandler
    public void on(ProductCreatedEvent event) throws Exception {
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(event, productEntity);

        try {
            productsRepository.save(productEntity);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if(true) throw new Exception("Forcing exception in event handler class");

    }
}
