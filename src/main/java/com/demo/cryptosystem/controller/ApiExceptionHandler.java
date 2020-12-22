package com.demo.cryptosystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

	public static class BTCRateNotFoundException extends RuntimeException {
	}

	@ExceptionHandler(BTCRateNotFoundException.class)
	public ResponseEntity noDataFound() {
		return new ResponseEntity("BTC rate not found", HttpStatus.NOT_FOUND);
	}
}
