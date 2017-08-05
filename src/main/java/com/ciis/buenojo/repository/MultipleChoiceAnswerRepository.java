package com.ciis.buenojo.repository;

import com.ciis.buenojo.domain.MultipleChoiceAnswer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MultipleChoiceAnswer entity.
 */
public interface MultipleChoiceAnswerRepository extends JpaRepository<MultipleChoiceAnswer,Long> {
	 public List<MultipleChoiceAnswer> findByMultipleChoiceQuestion_id(Long questionId);
	 
	 @Query("SELECT ma FROM MultipleChoiceAnswer ma INNER JOIN ma.multipleChoiceQuestion mcq INNER JOIN mcq.multipleChoiceExerciseContainer mcec WHERE mcec.id= ?1")
	 public List<MultipleChoiceAnswer> findByContainerId(Long containerId);
}
