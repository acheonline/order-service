package ru.achernyavskiy0n.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.achernyavskiy0n.orderservice.controller.ApiResponse;
import ru.achernyavskiy0n.orderservice.domain.Order;
import ru.achernyavskiy0n.orderservice.kafka.TransportRequestReplyBillingProcessor;
import ru.achernyavskiy0n.orderservice.kafka.messages.RequestMessage;
import ru.achernyavskiy0n.orderservice.kafka.messages.ResponseMessage;

/**
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final TransportRequestReplyBillingProcessor requestBillingProducer;

    public ResponseMessage create(Order order){
        return requestBillingProducer.sendRequestAndGetReply(new RequestMessage(order));
    }
}
