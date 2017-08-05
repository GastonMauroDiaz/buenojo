package com.ciis.buenojo.service;
import org.springframework.stereotype.Service;
import com.ciis.buenojo.repository.ActivityTraceRepository;
import com.ciis.buenojo.web.rest.dto.AverageScoreByExerciseTypeDTO;
import com.ciis.buenojo.web.rest.dto.ResolutionTimeByExerciseTypeDTO;
import java.util.List;
import javax.inject.Inject;


@Service
public class CourseStatisticsService {
	
	@Inject
	private ActivityTraceRepository traceRepository;
	
	
	public List<ResolutionTimeByExerciseTypeDTO> getAverageResolutionTimeByExerciseType(Long courseId) {
		
		return traceRepository.averageResolutionTimeByExerciseType(courseId);
		
	}
	
	public List<AverageScoreByExerciseTypeDTO> getAverageScoreByExerciseType(Long courseId) {
		
		return traceRepository.averageScoreByExerciseType(courseId);
	}
}
