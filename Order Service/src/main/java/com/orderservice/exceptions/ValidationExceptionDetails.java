package com.orderservice.exceptions;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ValidationExceptionDetails extends ExceptionDetails {
    private String fields;         // quais campos falharam
    private String fieldsMessage;  // as mensagens de cada um
}
