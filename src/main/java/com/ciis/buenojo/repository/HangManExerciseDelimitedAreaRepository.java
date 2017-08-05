package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.HangManExerciseDelimitedArea;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the HangManExerciseDelimitedArea entity.
 */
public interface HangManExerciseDelimitedAreaRepository extends JpaRepository<HangManExerciseDelimitedArea,Long> {
	   @Query(value="select eir.hang_man_exercise_id, eir.x, eir.y, eir.radius "
	    		+ " from hang_man_exercise_delimited_area eir where eir.hang_man_exercise_id in (Select id from hang_man_exercise where hangman_game_container_id = ?1)", nativeQuery = true )
	    
	public List<Object> findByContainer(Long containerId);
}
