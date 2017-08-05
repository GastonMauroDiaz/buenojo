package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.PhotoLocationSightPair;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PhotoLocationSightPair entity.
 */
public interface PhotoLocationSightPairRepository extends JpaRepository<PhotoLocationSightPair,Long> {

}
