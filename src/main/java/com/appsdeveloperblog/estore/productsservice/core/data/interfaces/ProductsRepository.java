package com.appsdeveloperblog.estore.productsservice.core.data.interfaces;

import com.appsdeveloperblog.estore.productsservice.core.data.domains.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository<ProductEntity, String> {

    ProductEntity findByProductId(String productId);
    ProductEntity findByProductIdOrTitle(String productId, String title);
}
