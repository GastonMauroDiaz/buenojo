package com.ciis.buenojo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ciis.buenojo.domain.HangManExerciseImageResource;

/**
 * Spring Data JPA repository for the HangManExerciseImageResource entity.
 */
public interface HangManExerciseImageResourceRepository extends JpaRepository<HangManExerciseImageResource,Long> {
    @Query(value="select eir.hang_man_exercise_id, ir.name "
    		+ " from hang_man_exercise_image_resource eir inner join image_resource ir on (eir.image_resource_id = ir.id) where eir.hang_man_exercise_id in (Select id from hang_man_exercise where hangman_game_container_id = ?1)", nativeQuery = true )
    public List<Object> findByContainer(Long containerId);
}
