package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.Level;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Level entity.
 */
public interface LevelRepository extends JpaRepository<Level,Long> {

    @Query("select distinct level from Level level left join fetch level.activities")
    List<Level> findAllWithEagerRelationships();

    @Query("select level from Level level left join fetch level.activities where level.id =:id")
    Level findOneWithEagerRelationships(@Param("id") Long id);

}
