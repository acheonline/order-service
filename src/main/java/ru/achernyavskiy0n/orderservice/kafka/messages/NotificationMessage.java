package ru.achernyavskiy0n.orderservice.kafka.messages;

import lombok.*;
import lombok.experimental.Accessors;

/**
 *
 */
@Setter
@Getter
@RequiredArgsConstructor
@ToString
public class NotificationMessage {
    private String username;
    private String payload;

    public NotificationMessage(String username) {
        this.username = username;
    }
}
