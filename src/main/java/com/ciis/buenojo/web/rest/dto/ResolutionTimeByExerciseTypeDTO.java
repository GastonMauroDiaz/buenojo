package com.ciis.buenojo.web.rest.dto;

import java.util.Map;
import com.ciis.buenojo.domain.enumeration.ExerciseType;
import com.google.common.base.Optional;

public class ResolutionTimeByExerciseTypeDTO {
	
	private static final String DURATION = "duration";
	private static final String EXERCISE_TYPE = "exerciseType";
	
	private ExerciseType exerciseType;
	private Long duration;
	
	public ResolutionTimeByExerciseTypeDTO(Map<String,Object> map){
		Optional<String> type = Optional.fromNullable((String)map.get(EXERCISE_TYPE));
		Optional<Long> time = Optional.fromNullable((Long)map.get(DURATION));
		
		if (type.isPresent()){
			exerciseType = ExerciseType.valueOf(type.get());
		}
		
		if (time.isPresent()){
			duration = time.get();
		}
		
	}
	
	public ResolutionTimeByExerciseTypeDTO(Double duration,ExerciseType exerciseType) {
		this.exerciseType = exerciseType;
		this.duration = new Long(duration.longValue());
	}

	public ExerciseType getExerciseType() {
		return exerciseType;
	}
	public void setExerciseType(ExerciseType exerciseType) {
		this.exerciseType = exerciseType;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	
}
