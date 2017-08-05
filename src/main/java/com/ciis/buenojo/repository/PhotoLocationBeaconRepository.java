package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.PhotoLocationBeacon;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PhotoLocationBeacon entity.
 */
public interface PhotoLocationBeaconRepository extends JpaRepository<PhotoLocationBeacon,Long> {

}
