package com.demo.cryptosystem.controller;

import com.demo.cryptosystem.model.BTCRate;
import com.demo.cryptosystem.repository.BTCRateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@Profile("test")
@ExtendWith(MockitoExtension.class)
class ApiControllerTest {

	@Mock
	private BTCRateRepository mockBTCRateRepository;

	@Mock
	private BTCRate mockBTCRate;

	@InjectMocks
	private ApiController cut; //cut = class under test


	@Test
	public void testGetLatestRate_forSuccess_whenLatestRateIsAvailable() throws Exception {
		when(mockBTCRateRepository.findFirstByOrderByRequestTimestampDesc()).thenReturn(mockBTCRate);

		assertSame(mockBTCRate, cut.getLatestRate());
	}

	@Test
	public void testGetLatestRate_forException_whenLatestRateIsNotAvailable() throws Exception {
		when(mockBTCRateRepository.findFirstByOrderByRequestTimestampDesc()).thenReturn(null);

		assertThrows(ApiExceptionHandler.BTCRateNotFoundException.class, () -> cut.getLatestRate());
	}
}
