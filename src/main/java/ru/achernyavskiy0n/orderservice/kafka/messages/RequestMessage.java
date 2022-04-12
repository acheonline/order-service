package ru.achernyavskiy0n.orderservice.kafka.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.achernyavskiy0n.orderservice.domain.Order;

/**
 */
@Data
@Accessors
@AllArgsConstructor
@NoArgsConstructor
public class RequestMessage {
    private Order order;
}