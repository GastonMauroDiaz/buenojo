package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.SatelliteImage;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SatelliteImage entity.
 */
public interface SatelliteImageRepository extends JpaRepository<SatelliteImage,Long> {

	public SatelliteImage findOneByName(String name);
}
