package com.appsdeveloperblog.estore.productsservice.query.rest;

import com.appsdeveloperblog.estore.productsservice.core.data.ProductEntity;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsQueryController {

    // QueryGateway: The command bus implementation?
    @Autowired
    QueryGateway queryGateway;

    @GetMapping
    public List<ProductRestModel> getProducts(){
        FindProductsQuery findProductsQuery = new FindProductsQuery();
        List<ProductRestModel> products = queryGateway.query(findProductsQuery,
                ResponseTypes.multipleInstancesOf(ProductRestModel.class)).join();

        return products;
    }


}
