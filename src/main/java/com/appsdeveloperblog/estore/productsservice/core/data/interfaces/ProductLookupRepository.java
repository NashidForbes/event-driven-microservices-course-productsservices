package com.appsdeveloperblog.estore.productsservice.core.data.interfaces;

import com.appsdeveloperblog.estore.productsservice.core.data.ProductLookupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Id;

public interface ProductLookupRepository extends JpaRepository<ProductLookupEntity, String> {
    ProductLookupEntity FindByProductIdOrTitle(String productId, String title);
}
