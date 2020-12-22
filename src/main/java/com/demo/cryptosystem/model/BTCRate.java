package com.demo.cryptosystem.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDateTime;

//TODO - validators can be added as per requirement
@Entity
public class BTCRate {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;

	//TODO - adjust decimal as per requirement
	@JsonProperty("last_trade_price")
	private BigDecimal rate;

	//TODO - timestamp precision can be extended up to nanoseconds if needed
	@JsonProperty("requestTimestamp")
	private final LocalDateTime requestTimestamp = LocalDateTime.now().withNano(0);

	@Override
	public String toString() {
		return MessageFormat.format("[rate = {0}, requestTimestamp = {1}]", rate, requestTimestamp);
	}
}
