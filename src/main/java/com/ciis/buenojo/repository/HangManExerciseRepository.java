package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.HangManExercise;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the HangManExercise entity.
 */
public interface HangManExerciseRepository extends JpaRepository<HangManExercise,Long> {


	public List<HangManExercise> findByhangmanGameContainerIdOrderByExerciseOrder(Long id);

}
