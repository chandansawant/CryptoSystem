package com.demo.cryptosystem.repository;

import com.demo.cryptosystem.model.BTCRate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class BTCRateRepositoryTest {

	private static DateTimeFormatter DD_MM_YYYY_HH_MM_SS = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

	@Autowired
	private BTCRateRepository cut; //cut = class under test

	@Test
	public void testFindFirstByOrderByRequestTimestampDesc_forSuccess_whenNoRecordExists() {
		//delete all records
		cut.deleteAll();

		//get latest record
		BTCRate latestRate = cut.findFirstByOrderByRequestTimestampDesc();

		//check that no record is returned
		assertNull(latestRate);
	}

	@Test
	public void testFindFirstByOrderByRequestTimestampDesc_forSuccess_whenMultipleRecordsExist() throws InterruptedException {

		//create 3 records with gap of 1 second
		BTCRate btcRate1 = new BTCRate();
		Thread.sleep(1000);
		BTCRate btcRate2 = new BTCRate();
		Thread.sleep(1000);
		BTCRate btcRate3 = new BTCRate();

		//save records
		cut.save(btcRate1);
		cut.save(btcRate2);
		cut.save(btcRate3);
		cut.flush();

		//get latest record
		BTCRate latestRate = cut.findFirstByOrderByRequestTimestampDesc();

		//check that latest record is returned
		assertSame(btcRate3, latestRate);
	}

	@Test
	public void testFindByRequestTimestampBetween_forSuccess_whenNoRecordExists() {

		//delete all records
		cut.deleteAll();

		//get records as per date range provided
		LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
		LocalDateTime now = LocalDateTime.now();
		List<BTCRate> btcRates = cut.findByRequestTimestampBetween(yesterday, now);

		//check that no record is returned
		assertTrue(btcRates.isEmpty());
	}

	/*
	 * initialize sample data from file
	 */
	@Test
	@Sql({"classpath:test-data.sql"})
	public void testFindByRequestTimestampBetween_forSuccess_whenMultipleRecordsExist() {

		//prepare date ranges
		LocalDateTime Dec_01_2020 = LocalDateTime.parse("01-12-2020 00:00:00", DD_MM_YYYY_HH_MM_SS);
		LocalDateTime Dec_02_2020 = LocalDateTime.parse("02-12-2020 00:00:00", DD_MM_YYYY_HH_MM_SS);
		LocalDateTime Dec_03_2020 = LocalDateTime.parse("03-12-2020 00:00:00", DD_MM_YYYY_HH_MM_SS);
		LocalDateTime Dec_04_2020 = LocalDateTime.parse("04-12-2020 00:00:00", DD_MM_YYYY_HH_MM_SS);

		//get total records available
		long totalRecordsAvailable = cut.count();

		//get records for 1st range
		List<BTCRate> btcRatesBetween1to4 = cut.findByRequestTimestampBetween(Dec_01_2020, Dec_04_2020);

		/*
		 * for each record in 1st range, remove corresponding value from set
		 * for correct results, set will be empty
		 */
		Set<String> btcRateValuesBetween1to4
				= new HashSet<>(Set.of(
					"[rate = 1.1, requestTimestamp = 2020-12-01T00:00]"
					, "[rate = 2.2, requestTimestamp = 2020-12-02T00:00]"
					, "[rate = 3.3, requestTimestamp = 2020-12-03T00:00]"
					, "[rate = 4.4, requestTimestamp = 2020-12-04T00:00]"));

		for (BTCRate btcRate : btcRatesBetween1to4)
			btcRateValuesBetween1to4.remove(btcRate.toString());

		//get records for 1st range
		List<BTCRate> btcRatesBetween2to3 = cut.findByRequestTimestampBetween(Dec_02_2020, Dec_03_2020);

		/*
		 * for each record in 2nd range, remove corresponding value from set
		 * for correct results, set will be empty
		 */
		Set<String> btcRateValuesBetween2to3
				= new HashSet<>(Set.of(
					"[rate = 2.2, requestTimestamp = 2020-12-02T00:00]"
					, "[rate = 3.3, requestTimestamp = 2020-12-03T00:00]"));

		for (BTCRate btcRate : btcRatesBetween2to3)
			btcRateValuesBetween2to3.remove(btcRate.toString());

		//validate results
		assertEquals(4, totalRecordsAvailable);
		assertEquals(4, btcRatesBetween1to4.size());
		assertTrue(btcRateValuesBetween1to4.isEmpty());
		assertEquals(2, btcRatesBetween2to3.size());
		assertTrue(btcRateValuesBetween2to3.isEmpty());
	}
}
