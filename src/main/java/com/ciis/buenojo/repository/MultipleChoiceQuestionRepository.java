package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.MultipleChoiceQuestion;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MultipleChoiceQuestion entity.
 */
public interface MultipleChoiceQuestionRepository extends JpaRepository<MultipleChoiceQuestion,Long> {
     public List<MultipleChoiceQuestion> findBymultipleChoiceExerciseContainerId(Long id);
}
