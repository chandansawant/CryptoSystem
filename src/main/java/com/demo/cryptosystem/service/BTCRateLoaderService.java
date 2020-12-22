package com.demo.cryptosystem.service;

import com.demo.cryptosystem.model.BTCRate;
import com.demo.cryptosystem.repository.BTCRateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class BTCRateLoaderService {

	private static Logger log = LoggerFactory.getLogger(BTCRateLoaderService.class);

	@Autowired
	private BTCRateFetcherService btcRateFetcherService;

	@Autowired
	private BTCRateRepository btcRateRepository;


	/*
	 * TODO -
	 *  for production version, configure parameters like
	 *  executor service, separate thread pools, thread pool size, task queue limit, etc.
	 */
	@Async
	public void loadRate() {
		try {
			log.info("Fetch latest rate");
			BTCRate btcRate = btcRateFetcherService.getRate();
			log.info("Save latest received rate");
			btcRateRepository.saveAndFlush(btcRate);
		} catch (Exception ex) {
			log.error("Failed to load latest rate - " + ex.getMessage());
		}
	}
}
