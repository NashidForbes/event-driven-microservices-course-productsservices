package com.appsdeveloperblog.estore.productsservice.query.rest;

import com.appsdeveloperblog.estore.productsservice.core.data.ProductsRepository;
import org.springframework.stereotype.Component;

@Component
public class ProductsQueryHandler {

    private final ProductsRepository productsRepository;

    public ProductsQueryHandler(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }
}
