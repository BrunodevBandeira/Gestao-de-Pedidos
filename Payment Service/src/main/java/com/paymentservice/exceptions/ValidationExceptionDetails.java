package com.paymentservice.exceptions;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ValidationExceptionDetails extends  ExceptionDetails{
    private String fields;
    private String fielsMessage;
}
