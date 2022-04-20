package ru.achernyavskiy0n.orderservice.kafka.impl;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;
import ru.achernyavskiy0n.orderservice.kafka.TransportRequestReplyBillingProcessor;
import ru.achernyavskiy0n.orderservice.kafka.messages.RequestMessage;
import ru.achernyavskiy0n.orderservice.kafka.messages.ResponseMessage;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 */
@Component
@RequiredArgsConstructor
public class KafkaTransportRequestReplyReplyBillingProcessor implements TransportRequestReplyBillingProcessor {

    private final ReplyingKafkaTemplate<String, RequestMessage, ResponseMessage> kafkaTemplate;

    @Value("${order-service.transport.billing.request}")
    private String billingRequestTopic;

    @Value("${order-service.transport.billing.response}")
    private String billingResponseTopic;

    @Override
    public ResponseMessage sendRequestAndGetReply(RequestMessage message) {
        var record = new ProducerRecord<String, RequestMessage>(billingRequestTopic, message);
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, billingResponseTopic.getBytes()));
        record.headers().add(new RecordHeader("X-Request-Id", generateHashKey(message)));
        var requestReplyFuture = kafkaTemplate.sendAndReceive(record);
        ResponseMessage resp = null;
        try {
            resp = requestReplyFuture.get(3, TimeUnit.SECONDS).value();
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        return resp;
    }

    private byte[] generateHashKey(RequestMessage message) {
        var str = message.getOrder().toString();
        return UUID.nameUUIDFromBytes(str.getBytes(StandardCharsets.UTF_8)).toString().getBytes();
    }
}