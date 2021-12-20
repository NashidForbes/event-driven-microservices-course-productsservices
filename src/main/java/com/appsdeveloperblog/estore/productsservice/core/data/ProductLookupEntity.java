package com.appsdeveloperblog.estore.productsservice.core.data;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="productlookup")
@Data
public class ProductLookupEntity implements Serializable {

    private static final long serialVersionUID = -4350833891880621287L;

    @Id
    private String productId;
    @Column(unique = true)
    private String title;
}
