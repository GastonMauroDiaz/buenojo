package com.ciis.buenojo.web.rest;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.ciis.buenojo.service.CourseStatisticsService;
import com.ciis.buenojo.web.rest.dto.AverageScoreByExerciseTypeDTO;
import com.ciis.buenojo.web.rest.dto.ResolutionTimeByExerciseTypeDTO;
import com.codahale.metrics.annotation.Timed;
import javax.inject.Inject;
/**
 * REST controller for managing Course.
 */
@RestController
@RequestMapping("/api")
public class CourseStatisticsResource {
	
	
	 private final Logger log = LoggerFactory.getLogger(CourseStatisticsResource.class);
	 
	@Inject 
	private CourseStatisticsService statisticsService;
	
	/**
     * GET  /courseSatistics/:courseId/timeByExerciseType -> get the "id" course.
     */
    @RequestMapping(value = "/courseSatistics/{courseId}/timeByExerciseType",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ResolutionTimeByExerciseTypeDTO> getCourseStatisticsTimeByExerciseType(@PathVariable Long courseId) {
        log.debug("REST request to get courseSatistics timeByExerciseType for Course: {}", courseId);
        return statisticsService.getAverageResolutionTimeByExerciseType(courseId);
    }
    
    
	
	/**
     * GET  /courseSatistics/:courseId/timeByExerciseType -> get the "id" course.
     */
    @RequestMapping(value = "/courseSatistics/{courseId}/averageScoreByExerciseType",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AverageScoreByExerciseTypeDTO> getCourseStatisticsAverageScoreByExerciseType(@PathVariable Long courseId) {
        log.debug("REST request to get courseSatistics timeByExerciseType for Course: {}", courseId);
        return statisticsService.getAverageScoreByExerciseType(courseId);
    }

}
