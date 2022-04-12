package ru.achernyavskiy0n.orderservice.kafka.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.achernyavskiy0n.orderservice.kafka.messages.NotificationMessage;
import ru.achernyavskiy0n.orderservice.kafka.messages.RequestMessage;
import ru.achernyavskiy0n.orderservice.kafka.messages.ResponseMessage;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Configuration
@RequiredArgsConstructor
public class KafkaProcessorConfiguration {

    private final KafkaProperties kafkaProperties;

    @Value("${order-service.transport.billing.response}")
    private String replyTopic;


    @Bean
    public ProducerFactory<String, NotificationMessage> producerFactory() {
        return createProducerFactory(NotificationMessage.class);
    }

    @Bean
    public KafkaTemplate<String, NotificationMessage> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }


    @Bean
    public ReplyingKafkaTemplate<String, RequestMessage, ResponseMessage> replyKafkaTemplate
            (ProducerFactory<String, RequestMessage> pf,
             KafkaMessageListenerContainer<String, ResponseMessage> lc) {
        return new ReplyingKafkaTemplate<>(pf, lc);
    }

    @Bean
    public ProducerFactory<String, RequestMessage> requestProducerFactory() {
        return createProducerFactory(RequestMessage.class);
    }

    @Bean
    public ConsumerFactory<String, ResponseMessage> replyConsumerFactory() {
        return createConsumerFactory(ResponseMessage.class, replyTopic);
    }


    @Bean
    public KafkaMessageListenerContainer<String, ResponseMessage> replyListenerContainer() {
        ContainerProperties containerProperties = new ContainerProperties(replyTopic);
        return new KafkaMessageListenerContainer<>(replyConsumerFactory(), containerProperties);
    }

    @SneakyThrows
    private <T> ConsumerFactory<String, T> createConsumerFactory(Class<T> valueType, String topic) {
        Map<String, Object> props = new HashMap<>();
        var consumer = kafkaProperties.getConsumer();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumer.getBootstrapServers());
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, consumer.getClientId() + "-" + topic);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumer.getGroupId());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumer.getAutoOffsetReset());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, valueType);

        return new DefaultKafkaConsumerFactory<>(
                props, new StringDeserializer(), new JsonDeserializer<>(valueType));
    }

    private <T> ProducerFactory<String, T> createProducerFactory(Class<T> clazz) {
        var producer = kafkaProperties.getProducer();

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producer.getBootstrapServers());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, producer.getClientId());
        props.put(ProducerConfig.ACKS_CONFIG, producer.getAcks());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, clazz);
        return new DefaultKafkaProducerFactory<>(props, new StringSerializer(), new JsonSerializer<>());
    }
}