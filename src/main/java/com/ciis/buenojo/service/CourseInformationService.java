package com.ciis.buenojo.service;

import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciis.buenojo.domain.CourseLevelSession;
import com.ciis.buenojo.domain.CurrentSession;
import com.ciis.buenojo.domain.User;
import com.ciis.buenojo.domain.factories.CourseInformationSimpleFactory;
import com.ciis.buenojo.domain.factories.CourseLevelSessionSimpleFactory;
import com.ciis.buenojo.domain.nonPersistent.CourseInformation;
import com.ciis.buenojo.repository.CourseLevelMapRepository;
import com.ciis.buenojo.repository.CourseLevelSessionRepository;

@Service
@Transactional
public class CourseInformationService {

	private final Logger log = LoggerFactory.getLogger(CourseInformationService.class);

	@Inject
	private CourseLevelSessionRepository courseLevelSessionRepository;
	@Inject
	private CourseLevelMapRepository courseLevelMapRepository;

	@Inject
	private CourseLevelMapService courseLevelMapService;

	@Inject
	private CurrentSessionService currentSessionService;

	@Inject
	private CourseLevelSessionService courseLevelSessionService;

	@Inject
	private CourseInformationSimpleFactory courseInformationSimpleFactory;

	@Inject
	private CourseLevelSessionSimpleFactory courseLevelSessionSimpleFactory;

	public CourseInformation getCourseInformation(Long courseId)
	{
		return getCourseInformation(courseId, null);
	}
	
	public CourseInformation getCourseInformation(Long courseId, Long userId) {
		CourseInformation courseInformation;
		CourseLevelSession courseLevelSession;
		CurrentSession currentSession;
		User user;
		Map<String, Object> map = null;
		if (userId != null){
			courseInformation = courseLevelSessionRepository.findInformationByUserIdAndCourseId(userId, courseId);
		}else {
			courseInformation = courseLevelSessionRepository.findInformationByUserIsCurrentUserAndCourse(courseId);
		}
//		courseInformation= courseInformationSimpleFactory.getCourseInformation((Long)map.get("exerciseCount"), (Long)map.get("approvedExercises")); 
//		courseInformation.setApprovedPercentage((Double)map.get("approvedPercentage"));
		courseInformation.setCourseStages(courseLevelMapService.getMaxStages(courseId));
		currentSession = currentSessionService.getCurrentSession();
		user = currentSession.getUser();
		if (currentSession.getCourseLevelSession()==null)
		{
			courseLevelSession =courseLevelSessionSimpleFactory.getCourseLevelSessionEmpty(user, courseLevelMapRepository.findByCourse_IdAndParentIsNull(courseId).get(0));
			courseLevelSessionRepository.saveAndFlush(courseLevelSession);
			currentSession.setCourseLevelSession(courseLevelSession);
		}

		courseInformation.setCurrentStage(courseLevelMapService.getStage(courseId,  currentSession.getCourseLevelSession().getCourseLevelMap().getLevel().getId()));

		courseInformation.setRemainingStages(courseInformation.getCourseStages()-courseInformation.getCurrentStage());
		return courseInformation;
		
	}
}
