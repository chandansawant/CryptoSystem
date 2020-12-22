package com.demo.cryptosystem.service;

import com.demo.cryptosystem.model.BTCRate;
import com.demo.cryptosystem.repository.BTCRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Profile("test")
@ExtendWith(MockitoExtension.class)
class BTCRateLoaderServiceTest {

	@Mock
	private BTCRateFetcherService mockBTCRateFetcherService;

	@Mock
	private BTCRateRepository mockBTCRateRepository;

	@Mock
	private BTCRate mockBTCRate;

	@InjectMocks
	private BTCRateLoaderService cut; //cut = class under test


	@BeforeEach
	void setUp() {
		when(mockBTCRateFetcherService.getRate()).thenReturn(mockBTCRate);
	}

	@Test
	void testLoadRate_forSuccess_whenRateIsReceivedAndSavedSuccessfully() {
		assertDoesNotThrow(() -> cut.loadRate());
		verify(mockBTCRateFetcherService, times(1)).getRate();
		verify(mockBTCRateRepository, times(1)).saveAndFlush(any(BTCRate.class));
	}

	@Test
	void testLoadRate_forSuccess_whenExceptionFromRateFetcherService() {
		setupForExceptionFromRateFetcherService();

		assertDoesNotThrow(() -> cut.loadRate());
		verify(mockBTCRateFetcherService, times(1)).getRate();
		verify(mockBTCRateRepository, never()).saveAndFlush(any(BTCRate.class));
	}

	@Test
	void testLoadRate_forSuccess_whenExceptionFromRepository() {
		setupForExceptionFromRepository();

		assertDoesNotThrow(() -> cut.loadRate());
		verify(mockBTCRateFetcherService, times(1)).getRate();
		verify(mockBTCRateRepository, times(1)).saveAndFlush(any(BTCRate.class));
	}

	void setupForExceptionFromRateFetcherService() {
		when(mockBTCRateFetcherService.getRate()).thenThrow(new NoSuchElementException());
	}

	void setupForExceptionFromRepository() {
		when(mockBTCRateRepository.saveAndFlush(any(BTCRate.class))).thenThrow(NullPointerException.class);
	}
}
