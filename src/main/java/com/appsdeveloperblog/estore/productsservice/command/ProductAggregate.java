package com.appsdeveloperblog.estore.productsservice.command;

import com.appsdeveloperblog.estore.productsservice.core.event.ProductCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Aggregate
public class ProductAggregate {

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

        // publish event, to event handlers
        AggregateLifecycle.apply(productCreatedEvent);
    }
}
