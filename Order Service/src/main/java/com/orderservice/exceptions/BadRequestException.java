package com.orderservice.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

//Essa classe serve para criar uma exceção personalizada para
// representar erros de requisição inválida (HTTP 400 - Bad Request).
@ResponseStatus
public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
