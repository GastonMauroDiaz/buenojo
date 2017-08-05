package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.ExerciseTip;
import com.ciis.buenojo.domain.enumeration.SatelliteImageType;
import com.ciis.buenojo.domain.Tag;
import com.ciis.buenojo.domain.enumeration.Region;

import java.util.EnumSet;
import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the ExerciseTip entity.
 */
public interface ExerciseTipRepository extends JpaRepository<ExerciseTip,Long> {

@Query("select tip from ExerciseTip tip inner join fetch tip.regions r inner join fetch tip.imageTypes t where r in :regions and t in :imgTypes and tip.tag= :tag")
public List<ExerciseTip> findByTagRegionAndSatelliteImageType(@Param("tag") Tag tag, @Param("regions") EnumSet<Region> regions,@Param("imgTypes") EnumSet<SatelliteImageType> type);

}
