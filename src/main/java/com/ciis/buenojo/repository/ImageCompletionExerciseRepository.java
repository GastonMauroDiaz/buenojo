package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.ImageCompletionExercise;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the ImageCompletionExercise entity.
 */
public interface ImageCompletionExerciseRepository extends JpaRepository<ImageCompletionExercise,Long> {
//	@Query("select distinct imageCompletionExercise from ImageCompletionExercise imageCompletionExercise left join fetch imageCompletionExercise.tags left join fetch imageCompletionExercise.tagCircles")//left join fetch imageCompletionExercise.satelliteImages
	@Query("select distinct imageCompletionExercise from ImageCompletionExercise imageCompletionExercise left join fetch imageCompletionExercise.tags left join fetch imageCompletionExercise.satelliteImages left join fetch imageCompletionExercise.tagCircles")
//    @Query("select distinct imageCompletionExercise from ImageCompletionExercise imageCompletionExercise left join fetch imageCompletionExercise.tags left join fetch imageCompletionExercise.satelliteImages")
    List<ImageCompletionExercise> findAllWithEagerRelationships();

    @Query("select imageCompletionExercise from ImageCompletionExercise imageCompletionExercise left join fetch imageCompletionExercise.tags left join fetch imageCompletionExercise.satelliteImages left join fetch imageCompletionExercise.tagCircles where imageCompletionExercise.id =:id")//
//    @Query("select imageCompletionExercise from ImageCompletionExercise imageCompletionExercise left join fetch imageCompletionExercise.tags left join fetch imageCompletionExercise.satelliteImages where imageCompletionExercise.id =:id")
    ImageCompletionExercise findOneWithEagerRelationships(@Param("id") Long id);

}
