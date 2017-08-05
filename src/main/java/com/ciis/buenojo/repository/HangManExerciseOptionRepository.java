package com.ciis.buenojo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ciis.buenojo.domain.HangManExerciseOption;

/**
 * Spring Data JPA repository for the HangManExerciseOption entity.
 */
public interface HangManExerciseOptionRepository extends JpaRepository<HangManExerciseOption,Long> {
	public List<HangManExerciseOption> findByHangManExercise(Long id);
	public List<HangManExerciseOption> findByHangManExerciseId(Long id);
	@Query(value="select eh.id, eh.text, eh.hang_man_exercise_id "
	    		+ " from hang_man_exercise_option eh where eh.hang_man_exercise_id in (Select id from hang_man_exercise where hangman_game_container_id = ?1)", nativeQuery = true )
	
	public List<Object> findByHangManContainer(Long id);

	   
	   
}
