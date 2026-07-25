package com.paymentservice.mapper;

import com.paymentservice.dtos.RequestPayment;
import com.paymentservice.dtos.ResponsePayment;
import com.paymentservice.model.PaymentModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentModel toModel(RequestPayment requestPayment);
    ResponsePayment toDTO(PaymentModel paymentModel);

}
