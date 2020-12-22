package com.demo.cryptosystem.service;

import com.demo.cryptosystem.model.BTCRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseActions;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@Profile("test")
@ExtendWith(MockitoExtension.class)
class BTCRateFetcherServiceTest {

	private static final String TEST_URL = "http://localhost/tickers/BTC-USD";
	private static final String JSON_RATE = "{\"symbol\":\"BTC-USD\",\"price_24h\":1.1,\"volume_24h\":123.45678901,\"last_trade_price\":12345.6}";
	private static final String EXPECTED_RATE = "[rate = 12,345.6, requestTimestamp = " + LocalDate.now() + "]";

	@Mock
	private RestTemplateBuilder mockRestTemplateBuilder;

	@Mock
	private BTCRate mockBTCRate;

	private RestTemplate restTemplate = new RestTemplate();
	private MockRestServiceServer mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).build();
	private ResponseActions responseActions
				= mockRestServiceServer
					.expect(once(), requestTo(TEST_URL))
					.andExpect(method(HttpMethod.GET));

	//cut = class under test
	private BTCRateFetcherService cut;


	@BeforeEach
	void setUp() {
		when(mockRestTemplateBuilder.build()).thenReturn(restTemplate);

		cut = new BTCRateFetcherService(TEST_URL, mockRestTemplateBuilder);
	}

	@Test
	void testGetRate_forSuccess_whenExternalRequestSendsRate() {
		responseActions.andRespond(withSuccess(JSON_RATE, MediaType.APPLICATION_JSON));
		BTCRate btcRate = cut.getRate();

		assertEquals(EXPECTED_RATE, btcRate.toString().substring(0, btcRate.toString().length() - 10) + "]");
	}

	@Test
	void testGetRate_forException_whenExternalRequestSendsBadRequest() {
		responseActions.andRespond(withBadRequest());

		assertThrows(HttpClientErrorException.BadRequest.class, () -> cut.getRate());
	}

	@Test
	void testGetRate_forException_whenExternalRequestSendsNoData() {
		responseActions.andRespond(withNoContent());

		assertThrows(NoSuchElementException.class, () -> cut.getRate());
	}
}
