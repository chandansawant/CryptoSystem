package com.demo.cryptosystem.repository;

import com.demo.cryptosystem.model.BTCRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/*
 * TODO -
 *  JpaRepository based implementation can be replaced with DAO layer
 *  caching can be used to store and return latest updated record
 */
@Repository
public interface BTCRateRepository extends JpaRepository<BTCRate, Long> {

	BTCRate findFirstByOrderByRequestTimestampDesc();

	List<BTCRate> findByRequestTimestampBetween(final LocalDateTime startDateTime, final LocalDateTime endDateTime);
}
