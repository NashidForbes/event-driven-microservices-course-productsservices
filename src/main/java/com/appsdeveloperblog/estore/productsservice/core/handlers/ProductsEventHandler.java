package com.appsdeveloperblog.estore.productsservice.core.handlers;

import com.appsdeveloperblog.estore.productsservice.core.data.domains.ProductEntity;
import com.appsdeveloperblog.estore.productsservice.core.data.interfaces.ProductsRepository;
import com.appsdeveloperblog.estore.productsservice.core.events.ProductCreatedEvent;
import com.appsdeveloperblog.estore.sagacoreapi.events.ProductReservationCancelledEvent;
import com.appsdeveloperblog.estore.sagacoreapi.events.ProductReservedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;


//Handles ProductEvents and initiates an action
// depending upon the event
@Slf4j
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

    }

    @EventHandler
    public void on(ProductReservedEvent productReservedEvent){
        // do a read to get the latest updated state on the product entity field "Quantity"
        ProductEntity productEntity = productsRepository.findByProductId(productReservedEvent.getProductId());
        log.debug("ProductReserved: Current product quantity " + productEntity.getQuantity());

        productEntity.setQuantity(productReservedEvent.getQuantity());
        productsRepository.save(productEntity);

        log.debug("ProductReserved: New product quantity " + productEntity.getQuantity());
        log.info("ProductReservedEvent handled for productId: " + productReservedEvent.getProductId() +
                " and orderId: " + productReservedEvent.getOrderId());
    }

    @EventHandler
    public void on(ProductReservationCancelledEvent productReservationCancelledEvent){
        ProductEntity currentlyStoredProduct = productsRepository.findByProductId(productReservationCancelledEvent.getProductId());

        log.debug("ProductReserved: Current product quantity " + currentlyStoredProduct.getQuantity());

        int newQuantity = productReservationCancelledEvent.getQuantity() + productReservationCancelledEvent.getQuantity();
        // adding the currently reserved product back into the database to restore stock quantity.
        currentlyStoredProduct.setQuantity(newQuantity);

        productsRepository.save(currentlyStoredProduct);
        log.debug("ProductReserved: New product quantity " + currentlyStoredProduct.getQuantity());
    }

    // Clear or initialize the products database table when doing replays from event store
    @ResetHandler
    public void reset(){
        productsRepository.deleteAll();
    }
}
