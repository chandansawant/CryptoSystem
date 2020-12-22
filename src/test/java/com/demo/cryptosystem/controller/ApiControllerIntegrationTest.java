package com.demo.cryptosystem.controller;

import com.demo.cryptosystem.model.BTCRate;
import com.demo.cryptosystem.repository.BTCRateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiControllerIntegrationTest {

	private static final String GET_LATEST_RATE_URL = "/rate/latest";
	private static final String GET_HISTORIC_RATES_URL = "/rates/historic";

	private static final String PARAM_START_DATE = "startDate";
	private static final String PARAM_END_DATE = "endDate";

	private static final String PARAM_VALUE_START_DATE = "31122020";
	private static final String PARAM_VALUE_END_DATE = "01012020";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BTCRateRepository mockBTCRateRepository;

	@Autowired
	private ApiController cut; //cut = class under test

	private BTCRate btcRate = new BTCRate();
	private List<BTCRate> btcRates = List.of(btcRate, btcRate);


	@Test
	public void testGetLatestRate_forOkStatus_whenLatestRateIsAvailable() throws Exception {
		when(mockBTCRateRepository
					.findFirstByOrderByRequestTimestampDesc())
				.thenReturn(btcRate);

		mockMvc
			.perform(get(GET_LATEST_RATE_URL))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().string(
					containsString("{\"last_trade_price\":null,\"requestTimestamp\":")));
	}

	@Test
	public void testGetLatestRate_forNotFoundStatus_whenLatestRateIsNotAvailable() throws Exception {
		when(mockBTCRateRepository
					.findFirstByOrderByRequestTimestampDesc())
				.thenReturn(null);

		mockMvc
			.perform(get(GET_LATEST_RATE_URL))
			.andDo(print())
			.andExpect(status().isNotFound())
			.andExpect(content().string(containsString("BTC rate not found")));
	}

	@Test
	public void testGetHistoricRates_forOkStatus_whenHistoricRatesAreAvailable() throws Exception {
		when(mockBTCRateRepository
					.findByRequestTimestampBetween(
							any(LocalDateTime.class), any(LocalDateTime.class)))
				.thenReturn(btcRates);

		mockMvc
			.perform(get(GET_HISTORIC_RATES_URL)
				.param(PARAM_START_DATE, PARAM_VALUE_START_DATE)
				.param(PARAM_END_DATE, PARAM_VALUE_END_DATE))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().string(
					containsString("[{\"last_trade_price\":null,\"requestTimestamp\":")));
	}

	@Test
	public void testGetHistoricRates_forOkStatus_whenHistoricRatesAreNotAvailable() throws Exception {
		when(mockBTCRateRepository
					.findByRequestTimestampBetween(
							any(LocalDateTime.class), any(LocalDateTime.class)))
				.thenReturn(Collections.EMPTY_LIST);

		mockMvc
			.perform(get(GET_HISTORIC_RATES_URL)
				.param(PARAM_START_DATE, PARAM_VALUE_START_DATE)
				.param(PARAM_END_DATE, PARAM_VALUE_END_DATE))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().string(is("[]")));
	}

	@Test
	public void testGetHistoricRates_forBadRequestStatus_whenStartDateParameterIsNotInDdMmYyyyFormat()
			throws Exception {
		when(mockBTCRateRepository
				.findByRequestTimestampBetween(
						any(LocalDateTime.class), any(LocalDateTime.class)))
				.thenReturn(Collections.EMPTY_LIST);

		mockMvc
			.perform(get(GET_HISTORIC_RATES_URL)
				.param(PARAM_START_DATE, "31-12-2020")
				.param(PARAM_END_DATE, PARAM_VALUE_END_DATE))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(content().string(emptyOrNullString()));
	}

	@Test
	public void testGetHistoricRates_forBadRequestStatus_whenEndDateParameterIsNotInDdMmYyyyFormat()
			throws Exception {
		when(mockBTCRateRepository
				.findByRequestTimestampBetween(
						any(LocalDateTime.class), any(LocalDateTime.class)))
				.thenReturn(Collections.EMPTY_LIST);

		mockMvc
			.perform(get(GET_HISTORIC_RATES_URL)
				.param(PARAM_START_DATE, PARAM_VALUE_START_DATE)
				.param(PARAM_END_DATE, "01-01-2021"))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(content().string(emptyOrNullString()));
	}
}
