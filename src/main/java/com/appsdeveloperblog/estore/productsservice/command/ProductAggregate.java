package com.appsdeveloperblog.estore.productsservice.command;

import com.appsdeveloperblog.estore.productsservice.command.rest.models.CreateProductCommand;
import com.appsdeveloperblog.estore.productsservice.core.events.ProductCreatedEvent;
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
    public ProductAggregate(CreateProductCommand createProductCommand) {
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

        // publish handler, to handler handler
        // update the ProductAggregate state with the latest values
        AggregateLifecycle.apply(productCreatedEvent);
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
}
