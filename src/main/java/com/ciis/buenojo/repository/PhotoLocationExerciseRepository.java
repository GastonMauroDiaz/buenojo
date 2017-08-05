package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.PhotoLocationExercise;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the PhotoLocationExercise entity.
 */
public interface PhotoLocationExerciseRepository extends JpaRepository<PhotoLocationExercise,Long> {

    @Query("select distinct photoLocationExercise from PhotoLocationExercise photoLocationExercise left join fetch photoLocationExercise.landscapeKeywords left join fetch photoLocationExercise.satelliteImages")
    List<PhotoLocationExercise> findAllWithEagerRelationships();

    @Query("select photoLocationExercise from PhotoLocationExercise photoLocationExercise left join fetch photoLocationExercise.landscapeKeywords left join fetch photoLocationExercise.satelliteImages where photoLocationExercise.id =:id")
    PhotoLocationExercise findOneWithEagerRelationships(@Param("id") Long id);

}
