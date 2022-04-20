package ru.achernyavskiy0n.orderservice.kafka.messages;

import lombok.*;
import lombok.experimental.Accessors;
import ru.achernyavskiy0n.orderservice.domain.Order;

/**
 */
@Data
@Accessors
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class RequestMessage {
    private Order order;
}