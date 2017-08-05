package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.MultipleChoiceExerciseContainer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MultipleChoiceExerciseContainer entity.
 */
public interface MultipleChoiceExerciseContainerRepository extends JpaRepository<MultipleChoiceExerciseContainer,Long> {

}
