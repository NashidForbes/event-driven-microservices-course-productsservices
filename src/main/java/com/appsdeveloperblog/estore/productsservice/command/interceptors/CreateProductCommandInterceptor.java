package com.appsdeveloperblog.estore.productsservice.command.interceptors;

import com.appsdeveloperblog.estore.productsservice.command.rest.models.CreateProductCommand;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiFunction;

@Slf4j
@Component
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(List<? extends CommandMessage<?>> messages) {

        return (index, command) -> {

            log.info("Intercepted command: " + command.getPayloadType());

            // check that the command object is of the right type
            if (CreateProductCommand.class.equals((command.getPayloadType()))) {
                // cast to proper type
                CreateProductCommand createProductCommand = (CreateProductCommand) command.getPayload();
                
                // Validate Create Product Command
                // This violates DRY but it's just an example of creating validation at the
                // "Message Dispatch Interceptor" level
                if (createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("Price cannot be less than or equal to zero");
                }

                if (createProductCommand.getTitle() == null ||
                        createProductCommand.getTitle().isBlank()) {
                    throw new IllegalArgumentException("Title cannot be empty");
                }
            }

            return command;
        };
    }
}
