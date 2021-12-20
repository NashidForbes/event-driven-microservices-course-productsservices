package com.appsdeveloperblog.estore.productsservice.command.handlers;

import com.appsdeveloperblog.estore.productsservice.core.data.ProductLookupEntity;
import com.appsdeveloperblog.estore.productsservice.core.data.interfaces.ProductLookupRepository;
import com.appsdeveloperblog.estore.productsservice.core.events.ProductCreatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
// command handler and query handler belong to this processing group
@ProcessingGroup("product-group")
public class ProductLookupEventsHandler {

    private final ProductLookupRepository productLookupRepository;

    // autowired via constructor
    public ProductLookupEventsHandler(ProductLookupRepository productLookupRepository) {
        this.productLookupRepository = productLookupRepository;
    }

    @EventHandler
    public void on(ProductCreatedEvent event){
        ProductLookupEntity productLookupEntity = new ProductLookupEntity(event.getProductId(), event.getTitle());
        // can do a check here if product entity exists already
        // but we'll do this check in message dispatch interceptor class.

        productLookupRepository.save(productLookupEntity);
    }
}
