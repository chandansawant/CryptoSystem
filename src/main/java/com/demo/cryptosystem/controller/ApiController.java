package com.demo.cryptosystem.controller;

import com.demo.cryptosystem.model.BTCRate;
import com.demo.cryptosystem.repository.BTCRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class ApiController {

	private static final String DATE_PATTERN = "ddMMyyyy";

	@Autowired
	private BTCRateRepository btcRateRepository;

	@GetMapping("/rate/latest")
	public BTCRate getLatestRate() {
		return Optional
				.ofNullable(btcRateRepository.findFirstByOrderByRequestTimestampDesc())
				.orElseThrow(ApiExceptionHandler.BTCRateNotFoundException::new);
	}

	@GetMapping("/rates/historic")
	public List<BTCRate> getHistoricRates(@RequestParam
										   @DateTimeFormat(pattern = DATE_PATTERN)
										   		final LocalDate startDate
										, @RequestParam
										   @DateTimeFormat(pattern = DATE_PATTERN)
										   		final LocalDate endDate) {
		return btcRateRepository
				.findByRequestTimestampBetween(
					startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
	}
}
