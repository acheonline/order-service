package ru.achernyavskiy0n.orderservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.achernyavskiy0n.orderservice.domain.Order;
import ru.achernyavskiy0n.orderservice.kafka.TransportNotificationProducer;
import ru.achernyavskiy0n.orderservice.kafka.messages.NotificationMessage;
import ru.achernyavskiy0n.orderservice.service.OrderService;

/**
 *
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;
    private final TransportNotificationProducer notificationProducer;

    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ApiResponse createOrder(
            @RequestBody Order order) {
        var resp = service.create(order);
        if (resp == null) {
            var errPayload = "Ошибка создания заказа для пользователя '" + order.getUsername() + "'";
            log.error("Api Response: {}", errPayload);
            return createApiResponse(false, order.getUsername(), errPayload, 500);
        } else {
            log.info("Api Response: {}", resp);
            return createApiResponse(resp.getStatus().value() == 200, resp.getUsername(), resp.getMessage(), resp.getStatus().value());
        }
    }

    private ApiResponse createApiResponse(boolean success, String userId, String message, int status) {
        var notification = new NotificationMessage(userId);
        notification.setPayload(message);
        notificationProducer.sendNotification(notification);
        return ApiResponse.builder().success(success).message(message).status(status).build();
    }
}
