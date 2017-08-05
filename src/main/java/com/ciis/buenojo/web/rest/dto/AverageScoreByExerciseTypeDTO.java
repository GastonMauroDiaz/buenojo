package com.ciis.buenojo.web.rest.dto;

import com.ciis.buenojo.domain.enumeration.ExerciseType;

public class AverageScoreByExerciseTypeDTO {
	private ExerciseType exerciseType;
	private Double score;
	public ExerciseType getExerciseType() {
		return exerciseType;
	}
	public void setExerciseType(ExerciseType exerciseType) {
		this.exerciseType = exerciseType;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public AverageScoreByExerciseTypeDTO(ExerciseType exerciseType, Double score) {
		this.exerciseType = exerciseType;
		this.score = score;
	}
	
	
}
