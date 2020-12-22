package com.demo.cryptosystem.service;

import com.demo.cryptosystem.model.BTCRate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class BTCRateFetcherService {

	private static Logger log = LoggerFactory.getLogger(BTCRateFetcherService.class);

	private final String URL;
	private final RestTemplate restTemplate;

	public BTCRateFetcherService(@Value("${rate.provider.url}") final String url
								, final RestTemplateBuilder restTemplateBuilder) {
		this.URL = url;
		this.restTemplate = restTemplateBuilder.build();

		log.info("Rate provider url = [{}]", URL);
	}

	public BTCRate getRate() {
		//TODO - validators can be added
		log.info("Requesting latest rate");
		BTCRate btcRate = Optional
							.ofNullable(restTemplate.getForObject(URL, BTCRate.class))
							.orElseThrow();
		log.info("Received latest rate = {}", btcRate);
		return btcRate;
	}
}
