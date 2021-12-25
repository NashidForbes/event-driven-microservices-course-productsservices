package com.appsdeveloperblog.estore.productsservice.core.data.interfaces;

import com.appsdeveloperblog.estore.productsservice.core.data.domains.ProductLookupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductLookupRepository extends JpaRepository<ProductLookupEntity, String> {
    ProductLookupEntity findByProductIdOrTitle(String productId, String title);
}
