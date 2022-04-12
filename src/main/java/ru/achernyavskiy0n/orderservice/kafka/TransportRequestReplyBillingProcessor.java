package ru.achernyavskiy0n.orderservice.kafka;

import ru.achernyavskiy0n.orderservice.kafka.messages.RequestMessage;
import ru.achernyavskiy0n.orderservice.kafka.messages.ResponseMessage;

/**
 */
public interface TransportRequestReplyBillingProcessor {
    ResponseMessage sendRequestAndGetReply(RequestMessage message);
}