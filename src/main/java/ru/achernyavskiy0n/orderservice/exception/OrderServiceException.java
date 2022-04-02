package ru.achernyavskiy0n.orderservice.exception;

public class OrderServiceException extends Exception {

	public OrderServiceException(Throwable cause) {
		super(cause.getMessage(), cause);
	}

	public OrderServiceException(String message) {
		super(message);
	}

}