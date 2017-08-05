package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.HangManExercise;
import com.ciis.buenojo.domain.HangManExerciseHint;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the HangManExerciseHint entity.
 */
public interface HangManExerciseHintRepository extends JpaRepository<HangManExerciseHint,Long> {
	public List<HangManExercise> findByhangManExercise(Long id);
	   @Query(value="select eh.hang_man_exercise_id, eh.text, eh.ord, eh.x, eh.y "
	    		+ " from hang_man_exercise_hint eh where eh.hang_man_exercise_id in (Select id from hang_man_exercise where hangman_game_container_id = ?1)", nativeQuery = true )
	   public List<HangManExercise> findByhangmanGameContainerId(Long id);

}
