package com.appsdeveloperblog.estore.productsservice.command.aggregate;

import com.appsdeveloperblog.estore.productsservice.command.models.CreateProductCommand;
import com.appsdeveloperblog.estore.productsservice.core.events.ProductCreatedEvent;
import com.appsdeveloperblog.estore.sagacoreapi.commands.CancelProductReservationCommand;
import com.appsdeveloperblog.estore.sagacoreapi.commands.ReserveProductCommand;
import com.appsdeveloperblog.estore.sagacoreapi.events.ProductReservationCancelledEvent;
import com.appsdeveloperblog.estore.sagacoreapi.events.ProductReservedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Aggregate
public class ProductAggregate {

    // associate the dispatch command e.g. CreateProductCommand class
    // with the right aggregate
    // via the id (target id -> AggregateIdentifier)
    @AggregateIdentifier 
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;
    
    public ProductAggregate() {
    }

    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand) throws Exception {
        // Validate Create Product Command
        if (createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price cannot be less than or equal to zero");
        }

        if (createProductCommand.getTitle() == null ||
                createProductCommand.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();

        // copy property values from source object to corresponding destination object properties.
        // thank you BeanUtils!
        BeanUtils.copyProperties(createProductCommand, productCreatedEvent);

        // apply "stages" event for publish, if no exceptions,
        // staged object is published update the ProductAggregate
        // state with the latest values
        AggregateLifecycle.apply(productCreatedEvent);

    }

    @CommandHandler
    public void handle(ReserveProductCommand reserveProductCommand){
        if(this.quantity < reserveProductCommand.getQuantity()){
            throw new IllegalArgumentException("Insufficient number of items in stock");
        }

        ProductReservedEvent productReservedEvent = ProductReservedEvent.builder()
                .orderId(reserveProductCommand.getOrderId())
                .productId(reserveProductCommand.getProductId())
                .quantity(reserveProductCommand.getQuantity())
                .userId(reserveProductCommand.getUserId())
                .build();

        // apply "stages" event for publish, if no exceptions,
        // staged object is published update the ProductAggregate
        // state with the latest values
        AggregateLifecycle.apply(productReservedEvent);
    }

    @CommandHandler
    public void handle(CancelProductReservationCommand cancelProductReservationCommand){
        ProductReservationCancelledEvent productReservationCancelledEvent =
                ProductReservationCancelledEvent.builder()
                        .orderId(cancelProductReservationCommand.getOrderId())
                        .productId(cancelProductReservationCommand.getProductId())
                        .quantity(cancelProductReservationCommand.getQuantity())
                        .reason(cancelProductReservationCommand.getReason())
                        .userId(cancelProductReservationCommand.getUserId())
                        .build();

        AggregateLifecycle.apply(productReservationCancelledEvent);
    }


    // use initialize the aggregate class with the latest information state
    // avoid adding any business logic, use this handler handler to update the
    // aggregate state.
    @EventSourcingHandler
    public void on(ProductCreatedEvent productCreatedEvent){
        this.productId = productCreatedEvent.getProductId();
        this.price = productCreatedEvent.getPrice();
        this.title = productCreatedEvent.getTitle();
        this.quantity = productCreatedEvent.getQuantity();
    }

    @EventSourcingHandler
    public void on(ProductReservedEvent productReservedEvent){
        // subtract the current quantity in stock
        // persist the quantity object in stock in the event store
        this.quantity -= productReservedEvent.getQuantity();
    }
}
