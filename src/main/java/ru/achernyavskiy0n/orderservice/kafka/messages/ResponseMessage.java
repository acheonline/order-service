package ru.achernyavskiy0n.orderservice.kafka.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 *
 */
@Data
@Accessors
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage {
    private String username;
    private ApiResponseStatus status;
    private String message;

    public enum ApiResponseStatus {

        OK(200, "OK"),
        INTERNAL_SERVER_ERROR(500, "OK"),
        CONFLICT(409, "Заказ уже создан");

        private final int value;
        private final String reasonPhrase;

        ApiResponseStatus(int value, String reasonPhrase) {
            this.value = value;
            this.reasonPhrase = reasonPhrase;
        }

        /**
         * Return the integer value of this status code.
         */
        public int value() {
            return this.value;
        }
    }
}