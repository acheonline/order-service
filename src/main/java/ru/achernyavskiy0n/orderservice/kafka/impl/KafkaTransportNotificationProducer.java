package ru.achernyavskiy0n.orderservice.kafka.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ru.achernyavskiy0n.orderservice.kafka.TransportNotificationProducer;
import ru.achernyavskiy0n.orderservice.kafka.messages.NotificationMessage;

import java.util.UUID;

/**
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaTransportNotificationProducer implements TransportNotificationProducer {

  private final KafkaTemplate<String, NotificationMessage> kafkaTemplate;

  @Value("${order-service.transport.topics.notification.notify}")
  private String notificationTopic;

  @Override
  public void sendNotification(NotificationMessage message) {
    var key = UUID.randomUUID().toString();
    ListenableFuture<SendResult<String, NotificationMessage>> future =
        kafkaTemplate.send(new ProducerRecord<>(notificationTopic, key, message));
    future.addCallback(getCallback(message.getUsername(), notificationTopic));
  }

  private ListenableFutureCallback<SendResult<String, ?>> getCallback(
      String username, String topic) {
    return new ListenableFutureCallback<>() {
      @Override
      public void onSuccess(SendResult<String, ?> result) {
        log.info(
            "Сообщение для пользователя: '{}' успешно отправлено в сервис Notification топик: '{}'",
            username,
            result.getProducerRecord().topic());
      }

      @Override
      public void onFailure(@NonNull Throwable ex) {
        log.error("Не удалось отправить новость в топик: " + topic, ex);
      }
    };
  }
}
