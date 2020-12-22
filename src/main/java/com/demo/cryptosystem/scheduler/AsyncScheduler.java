package com.demo.cryptosystem.scheduler;

import com.demo.cryptosystem.service.BTCRateLoaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AsyncScheduler {

	private static Logger log = LoggerFactory.getLogger(AsyncScheduler.class);

	@Autowired
	private BTCRateLoaderService btcRateLoaderService;

	@Scheduled(cron = "${check.period.cron.expression}")
	public void loadRate() {
		log.info("==== Starting async process to load latest rate");
		btcRateLoaderService.loadRate();
	}
}
