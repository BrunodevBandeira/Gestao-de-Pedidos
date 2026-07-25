package com.paymentservice.service;

import com.paymentservice.dtos.RequestPayment;
import com.paymentservice.dtos.ResponsePayment;
import com.paymentservice.event.StockReservedEvent;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PaymentService {

    ResponsePayment getPaymentById(UUID id);
    Page<ResponsePayment> getAllPayment(Pageable pageable);
    ResponsePayment createPayment(RequestPayment requestPayment);
    ResponsePayment updatePayment(RequestPayment requestPayment);
    void deletePayment(UUID id);

    void processStockReserved(StockReservedEvent event);
}
