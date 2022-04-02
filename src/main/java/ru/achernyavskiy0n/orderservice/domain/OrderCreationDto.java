package ru.achernyavskiy0n.orderservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderCreationDto {

    private String username;
    private Double amount;
}