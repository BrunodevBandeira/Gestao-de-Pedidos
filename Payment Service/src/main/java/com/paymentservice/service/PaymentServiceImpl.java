package com.paymentservice.service;

import com.paymentservice.dtos.RequestPayment;
import com.paymentservice.dtos.ResponsePayment;
import com.paymentservice.event.StockReservedEvent;
import com.paymentservice.exceptions.BadRequestException;
import com.paymentservice.mapper.PaymentMapper;
import com.paymentservice.model.PaymentModel;
import com.paymentservice.producer.PaymentProducer;
import com.paymentservice.redis.PaymentIdempotencyService;
import com.paymentservice.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentProducer paymentProducer;
    private final PaymentIdempotencyService paymentIdempotencyService;

    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentMapper paymentMapper,
                              PaymentProducer paymentProducer, PaymentIdempotencyService paymentIdempotencyService) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.paymentProducer = paymentProducer;
        this.paymentIdempotencyService = paymentIdempotencyService;
    }

    @Override
    public ResponsePayment getPaymentById(UUID id) {
        return paymentMapper.toDTO(getID(id));
    }

    @Override
    public Page<ResponsePayment> getAllPayment(Pageable pageable) {
        return paymentRepository.findAll(pageable).map(paymentMapper::toDTO);
    }

    @Transactional
    @Override
    public ResponsePayment createPayment(RequestPayment requestPayment) {
        PaymentModel model = paymentMapper.toModel(requestPayment);
        PaymentModel saved = paymentRepository.save(model);
        return paymentMapper.toDTO(saved);
    }

    @Transactional
    @Override
    public ResponsePayment updatePayment(RequestPayment requestPayment) {
        PaymentModel paymentModel = paymentMapper.toModel(requestPayment);
        PaymentModel saved = paymentRepository.save(paymentModel);
        return paymentMapper.toDTO(saved);
    }

    @Transactional
    @Override
    public void deletePayment(UUID id) {
        paymentRepository.delete(getID(id));
    }

    private PaymentModel getID(UUID id) {
        return paymentRepository.findById(id).orElseThrow(() -> new BadRequestException("Payment Not Found: " + id));
    }

    @Transactional
    @Override
    public void processStockReserved(StockReservedEvent event) {

        if (!paymentIdempotencyService.tryMarkProcessed(event.orderId())) {
            System.out.println("[PAYMENT] Pedido " + event.orderId() + " já processado. Ignorando duplicata.");
            return;
        }

        if (!event.reserved()) {
            System.out.println("[PAYMENT] Pedido " + event.orderId() + " sem estoque; nada a cobrar.");
            return;
        }

        boolean approved = event.valueTotal() != null && event.valueTotal() > 0;

        PaymentModel payment = PaymentModel.builder()
                .orderId(event.orderId())
                .productName(event.productName())
                .amount(event.valueTotal())
                .status(approved ? "APPROVED" : "FAILED")
                .transactionId(UUID.randomUUID().toString())
                .processedAt(LocalDateTime.now())
                .build();
        paymentRepository.save(payment);

        System.out.println("[PAYMENT] Pedido " + event.orderId() + " -> " + payment.getStatus()
                + " | valor=" + payment.getAmount());

        paymentProducer.publishPaymentResult(
                event.orderId(), approved, event.valueTotal(), payment.getTransactionId());
    }
}
