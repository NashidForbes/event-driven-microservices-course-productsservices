package com.appsdeveloperblog.estore.productsservice.core.handlers.errors;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ErrorMessage {

    private final Date timeStamp;
    private final String message;
}
