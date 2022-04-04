package ru.achernyavskiy0n.orderservice.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessfulResponse {

    private boolean success;
    private int status;
    private String message;

}