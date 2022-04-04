package ru.achernyavskiy0n.orderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.achernyavskiy0n.orderservice.domain.OrderCreationDto;
import ru.achernyavskiy0n.orderservice.exception.OrderServiceException;
import ru.achernyavskiy0n.orderservice.response.SuccessfulResponse;
import ru.achernyavskiy0n.orderservice.service.OrderService;

/**
 *
 */
@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private OrderService service;

    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessfulResponse> createOrder(
            @RequestBody OrderCreationDto orderCreationDto) throws OrderServiceException {
        service.create(orderCreationDto.getUsername(), orderCreationDto.getAmount());
        return ResponseEntity.ok(
                new SuccessfulResponse(true,HttpStatus.OK.value(), "Order created successful"));
    }
}
