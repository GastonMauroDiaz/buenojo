package com.ciis.buenojo.service;

import java.time.ZonedDateTime;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.ciis.buenojo.domain.Activity;
import com.ciis.buenojo.domain.ActivityTrace;
import com.ciis.buenojo.domain.Enrollment;
import com.ciis.buenojo.exceptions.BuenOjoInconsistencyException;
import com.ciis.buenojo.repository.ActivityRepository;
import com.ciis.buenojo.repository.ActivityTraceRepository;
import com.ciis.buenojo.repository.EnrollmentRepository;

 

	
@Service
public class ActivityTraceService {
	
	@Inject
	private EnrollmentRepository enrollmentRepository;
	
	@Inject 
	private ActivityRepository activityRepository;
	
	public ActivityTrace startActivity(Long activityId) throws BuenOjoInconsistencyException {
		
		Activity activity = activityRepository.findOne(activityId);
		
		if (activity == null) throw new BuenOjoInconsistencyException("No Existe la actividad con ID '"+activityId+"'");
		
		ActivityTrace trace = new ActivityTrace();
		Enrollment enrollment = enrollmentRepository.findByUserIsCurrentUserAndCourse(activity.getLevel().getCourse().getId()).orElseThrow(() -> 
		new BuenOjoInconsistencyException("No existe un Enrollment para este usuario en el curso '"+activity.getLevel().getCourse().getId()+"'"));
		trace.setEnrollment(enrollment.getId());
		trace.setStartDate(ZonedDateTime.now());
		trace.setActivity(activity);
		
		
		return trace;
	}
	
	
}
