package ru.achernyavskiy0n.orderservice.kafka;

import ru.achernyavskiy0n.orderservice.kafka.messages.NotificationMessage;

/**
 */
public interface TransportProducer {
    void sendNotification(NotificationMessage message);
}
