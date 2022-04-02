package ru.achernyavskiy0n.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.achernyavskiy0n.orderservice.exception.OrderServiceException;
import ru.achernyavskiy0n.orderservice.kafka.TransportProducer;
import ru.achernyavskiy0n.orderservice.kafka.messages.NotificationMessage;
import ru.achernyavskiy0n.orderservice.response.SuccessfulResponse;
import ru.achernyavskiy0n.orderservice.response.UserAccountInfo;

import java.net.URI;

/**
 *
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final TransportProducer transportProducer;

    @Value("${order-service.transport.billing.host}")
    private String billingHost;

    @Value("${order-service.transport.billing.get-account-id}")
    private String get;

    private final RestTemplate restTemplate;

    public void create(String username, Double amount) throws OrderServiceException {
        var notification = new NotificationMessage(username);

        String payload;
        var getUrl = URI.create(billingHost.concat("/").concat(get).concat("/").concat(username));
        log.info("GET запрос отправлен на сервис Billing: {}", getUrl);
        var responseFromBillingGetAccount = restTemplate.getForEntity(getUrl, UserAccountInfo.class);
        if ((responseFromBillingGetAccount.getBody() == null) || (responseFromBillingGetAccount.getBody().getAccountId() == null)) {
            payload = "Ошибка при создании заказа для пользователя '" + username + "' на сумму: '" + amount + "'. ";
            throw new OrderServiceException("AccountId для пользователя '" + username + "' не найден");
        } else {
            var accountId = responseFromBillingGetAccount.getBody().getAccountId();
            var patchUrl = URI.create(billingHost.concat("/").concat(get).concat("/").concat(accountId));
            log.info("PATCH зазпрос отправлен на сервис Billing: {}", patchUrl);
            var billingResponse = restTemplate.patchForObject(patchUrl, amount, SuccessfulResponse.class);
            if ((billingResponse != null) && (!billingResponse.isSuccess())) {
                payload = "Ошибка при создании заказа для пользователя '" + username + "' на сумму: '" + amount + "'. ";
                throw new OrderServiceException("Не достаточно денег на счете '" + accountId + "' у пользователя '" + username + ".");
            } else {
                payload = "Успешно создан заказ для пользователя '" + username + "' на сумму: '" + amount + "'. ";
                log.info(payload);
            }
        }
        notification.setPayload(payload);
        transportProducer.sendNotification(notification);
    }
}
