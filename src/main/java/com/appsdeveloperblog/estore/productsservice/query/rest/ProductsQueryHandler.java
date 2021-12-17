package com.appsdeveloperblog.estore.productsservice.query.rest;

import com.appsdeveloperblog.estore.productsservice.core.data.ProductEntity;
import com.appsdeveloperblog.estore.productsservice.core.data.ProductsRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductsQueryHandler {

    private final ProductsRepository productsRepository;

    public ProductsQueryHandler(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    // FindProductsQuery query is just a place holder parameter
    // it seems
    @QueryHandler
    public List<ProductRestModel> findProducts(FindProductsQuery query){

        List<ProductRestModel> productsRest = new ArrayList<>();

        List<ProductEntity> storedProducts = productsRepository.findAll();

        for(ProductEntity productEntity: storedProducts){
            ProductRestModel productRestModel = new ProductRestModel();
            BeanUtils.copyProperties(productEntity, productRestModel);
            productsRest.add(productRestModel);
        }

        return productsRest;
    }
}
