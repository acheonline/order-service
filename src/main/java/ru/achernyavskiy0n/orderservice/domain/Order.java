package ru.achernyavskiy0n.orderservice.domain;

import lombok.*;

/**
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {
    private String username;
    private Double amount;
}