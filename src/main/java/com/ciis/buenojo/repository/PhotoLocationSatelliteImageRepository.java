package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.PhotoLocationSatelliteImage;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the PhotoLocationSatelliteImage entity.
 */
public interface PhotoLocationSatelliteImageRepository extends JpaRepository<PhotoLocationSatelliteImage,Long> {

    @Query("select distinct photoLocationSatelliteImage from PhotoLocationSatelliteImage photoLocationSatelliteImage left join fetch photoLocationSatelliteImage.keywords")
    List<PhotoLocationSatelliteImage> findAllWithEagerRelationships();

    @Query("select photoLocationSatelliteImage from PhotoLocationSatelliteImage photoLocationSatelliteImage left join fetch photoLocationSatelliteImage.keywords where photoLocationSatelliteImage.id =:id")
    PhotoLocationSatelliteImage findOneWithEagerRelationships(@Param("id") Long id);
    
    @Query("select photoLocationSatelliteImage from PhotoLocationSatelliteImage photoLocationSatelliteImage left join fetch photoLocationSatelliteImage.keywords where photoLocationSatelliteImage.satelliteImage.name =:name")
    PhotoLocationSatelliteImage findOneWithEagerRelationshipsByName(@Param("name") String name);
}
