package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.LoadedExercise;
import com.ciis.buenojo.domain.LoaderTrace;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LoadedExercise entity.
 */
public interface LoadedExerciseRepository extends JpaRepository<LoadedExercise,Long> {
	List<LoadedExercise> findAllByLoaderTrace(LoaderTrace trace);
}
