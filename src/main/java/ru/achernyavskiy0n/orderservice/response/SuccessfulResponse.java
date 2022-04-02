package ru.achernyavskiy0n.orderservice.response;

import lombok.Getter;

@Getter
public class SuccessfulResponse {

	private final boolean success = true;
	private final int status;
	private final String message;

	public SuccessfulResponse(int status, String message) {
		this.status = status;
		this.message = message;
	}
}