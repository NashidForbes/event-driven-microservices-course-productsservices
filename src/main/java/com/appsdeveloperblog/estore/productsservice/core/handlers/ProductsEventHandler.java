package com.appsdeveloperblog.estore.productsservice.core.handlers;

import com.appsdeveloperblog.estore.productsservice.core.data.ProductEntity;
import com.appsdeveloperblog.estore.productsservice.core.data.interfaces.ProductsRepository;
import com.appsdeveloperblog.estore.productsservice.core.events.ProductCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

//Handles ProductEvents and initiates an action
// depending upon the event
@Component
public class ProductsEventHandler {

    private final ProductsRepository productsRepository;

    public ProductsEventHandler(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @ExceptionHandler(resultType=IllegalArgumentException.class)
    public void handle(IllegalArgumentException exception) {
        // Log error message

    }

    @ExceptionHandler(resultType=Exception.class)
    public void handle(Exception exception) {
        // Log error message

    }

    @EventHandler
    public void on(ProductCreatedEvent event) {
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(event, productEntity);

        try {
            productsRepository.save(productEntity);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
