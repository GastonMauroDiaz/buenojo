package com.ciis.buenojo.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciis.buenojo.domain.Activity;
import com.ciis.buenojo.domain.ActivityTrace;
import com.ciis.buenojo.domain.ActivityTransition;
import com.ciis.buenojo.domain.Course;
import com.ciis.buenojo.domain.CourseLevelMap;
import com.ciis.buenojo.domain.CourseLevelSession;
import com.ciis.buenojo.domain.CurrentSession;
import com.ciis.buenojo.domain.Enrollment;
import com.ciis.buenojo.domain.Level;
import com.ciis.buenojo.domain.User;
import com.ciis.buenojo.domain.enumeration.CourseLevelStatus;
import com.ciis.buenojo.domain.enumeration.EnrollmentStatus;
import com.ciis.buenojo.domain.factories.CourseLevelSessionSimpleFactory;
import com.ciis.buenojo.domain.factories.EnrollmentSimpleFactory;
import com.ciis.buenojo.domain.util.BuenOjoRandomUtils;
import com.ciis.buenojo.exceptions.BuenOjoInconsistencyException;
import com.ciis.buenojo.repository.ActivityRepository;
import com.ciis.buenojo.repository.ActivityTraceRepository;
import com.ciis.buenojo.repository.CourseLevelMapRepository;
import com.ciis.buenojo.repository.CourseLevelSessionRepository;
import com.ciis.buenojo.repository.CourseRepository;
import com.ciis.buenojo.repository.CurrentSessionRepository;
import com.ciis.buenojo.repository.EnrollmentRepository;
import com.ciis.buenojo.repository.UserRepository;
import com.ciis.buenojo.security.CustomUserDetails;
/**
 * Service class for managing users.
 */
@Service
@Transactional
public class CourseLevelSessionService {

	private final Logger log = LoggerFactory.getLogger(CourseLevelSessionService.class);


	@Inject
	private CourseLevelSessionRepository courseLevelSessionRepository;

	@Inject
	private CourseLevelMapRepository courseLevelMapRepository;

	@Inject
	private CourseRepository courseRepository;
	@Inject
	private UserRepository userRepository;

	@Inject
	private CurrentSessionRepository currentSessionRepository;

	private CourseLevelSessionSimpleFactory courseLevelSessionSimpleFactory=  new CourseLevelSessionSimpleFactory();

	private EnrollmentSimpleFactory enrollmentSimpleFactory=  new EnrollmentSimpleFactory();

	@Inject
	private EnrollmentRepository enrollmentRepository;

	@Inject
	private ActivityTraceRepository traceRepository;

	@Inject
	private ActivityRepository activityRepository;

	public void removeAll() {
		List<CourseLevelSession> courseLevelSessions = courseLevelSessionRepository.findAll();
		for (CourseLevelSession courseLevelSession : courseLevelSessions) {
			log.debug("Deleting not activated user {}", courseLevelSession.getId());
			courseLevelSessionRepository.delete(courseLevelSession);
		}
	}

	public void removeAllByCourseAndCurrentUser(Long courseId) {
		List<CourseLevelSession> courseLevelSessions = courseLevelSessionRepository.findByUserIsCurrentUserAndCourse(courseId);

//		for (CourseLevelSession courseLevelSession : courseLevelSessions) {
//			log.debug("Deleting not activated user {}", courseLevelSession.getId());
//			courseLevelSessionRepository.delete(courseLevelSession);
//			
//		}
		
		courseLevelSessionRepository.delete(courseLevelSessions);

	}
	@Transactional
	public CourseLevelSession addPointsPercentageAndExerciseCountToCourseLevelSession(CourseLevelSession courseLevelSession, Double experiencePoints, Double percentage)
	{
		Double currentExperiencePoints;
		
		Integer currentExerciseCompletedCount;
		Integer currentApprovedExercises = courseLevelSession.getApprovedExercises();
		currentExperiencePoints= courseLevelSession.getExperiencePoints();
		currentExperiencePoints+= experiencePoints;
		if (experiencePoints>0){
			courseLevelSession.setApprovedExercises(currentApprovedExercises+1);
		}
		
		courseLevelSession.setExperiencePoints(currentExperiencePoints);
		currentExerciseCompletedCount = courseLevelSession.getExerciseCompletedCount();
		currentExerciseCompletedCount++;
		courseLevelSession.setPercentage(courseLevelSession.estimatePercentage());
		courseLevelSession.setExerciseCompletedCount(currentExerciseCompletedCount);
		courseLevelSessionRepository.saveAndFlush(courseLevelSession);
		return courseLevelSession;
	}

	@Transactional
	public CourseLevelSession updateStatusCourseLevelSession(CourseLevelSession courseLevelSession, Double experiencePoints, Double percentage)
	{
		if (courseLevelSession.getPercentage()>=100)
			courseLevelSession.setStatus(CourseLevelStatus.Done);
		courseLevelSessionRepository.saveAndFlush(courseLevelSession);
		return courseLevelSession;
	}

	@Transactional
	public Enrollment finishEnrollment(Enrollment enrollment){
		List<CourseLevelSession> courseLevelSessions = courseLevelSessionRepository.findByUserIsCurrentUserAndCourse(enrollment.getCourse().getId());
		
		for (CourseLevelSession courseLevelSession : courseLevelSessions) {
			courseLevelSession.setStatus(CourseLevelStatus.Done);
			
		}
		courseLevelSessionRepository.save(courseLevelSessions);
		enrollment.setStatus(EnrollmentStatus.Finished);
		return enrollmentRepository.save(enrollment);
		
	}
	public void createAllCourseLevelSessionByCourseAndCurrentUser(Long courseId) {
		List<CourseLevelSession> courseLevelSessions = new ArrayList<CourseLevelSession>();
		List<CourseLevelMap> courseLevelMaps = courseLevelMapRepository.findByCourse_Id(courseId);

		Optional<com.ciis.buenojo.domain.User> user = currentUser();

		for (CourseLevelMap courseLevelMap : courseLevelMaps) {
			log.debug("Creating custom courseLevelMap {}", courseLevelMap.getId());
			CourseLevelSession courseLevelSession = courseLevelSessionSimpleFactory.getCourseLevelSessionEmpty();
			//Start first level
			if (courseLevelMap.getParent()== null) {
				courseLevelSession.setStatus(CourseLevelStatus.InProgress);
			}
			courseLevelSession.setUser(user.get());
			courseLevelSession.setCourseLevelMap(courseLevelMap);
			courseLevelSessions.add(courseLevelSession);
		}

		courseLevelSessionRepository.save(courseLevelSessions);
		courseLevelSessionRepository.flush();
	}

	private Optional<User> currentUser() {

		return userRepository.findOneByLogin( ((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
	}
	public void createLevelSessionForCurrentUser() {
		List<CourseLevelSession> courseLevelSessions = new ArrayList<CourseLevelSession>();

		Optional<com.ciis.buenojo.domain.User> user = currentUser();
		CourseLevelMap courseLevelMap = new CourseLevelMap();
		// courseLevelMap.setId(-1l); //BUG: [ERROR] com.ciis.buenojo.aop.logging.LoggingAspect - Exception in com.ciis.buenojo.service.CourseLevelSessionService.enforceEnrollment() with cause = javax.persistence.EntityNotFoundException: Unable to find com.ciis.buenojo.domain.CourseLevelMap with id -1
		
		log.debug("Creating custom courseLevelMap {}", courseLevelMap.getId());
		CourseLevelSession courseLevelSession = courseLevelSessionSimpleFactory.getCourseLevelSessionEmpty();
		courseLevelSession.setUser(user.get());
		courseLevelSession.setCourseLevelMap(courseLevelMap);
		courseLevelSessions.add(courseLevelSession);


		courseLevelSessionRepository.save(courseLevelSessions);
		courseLevelSessionRepository.flush();
	}


	public void createAllCourseLevelSessionByCourseAndCurrentUserRandom(Long courseId) {
		List<CourseLevelSession> courseLevelSessions = new ArrayList<CourseLevelSession>();
		List<CourseLevelMap> courseLevelMaps = courseLevelMapRepository.findByCourse_Id(courseId);
		Optional<com.ciis.buenojo.domain.User> user = userRepository.findOneByLogin( ((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());

		for (CourseLevelMap courseLevelMap : courseLevelMaps) {
			log.debug("Creating custom courseLevelMap {}", courseLevelMap.getId());
			CourseLevelSession courseLevelSession = courseLevelSessionSimpleFactory.getCourseLevelSessionRandom();
			courseLevelSession.setUser(user.get());
			courseLevelSession.setCourseLevelMap(courseLevelMap);
			courseLevelSessions.add(courseLevelSession);
		}

		courseLevelSessionRepository.save(courseLevelSessions);
		courseLevelSessionRepository.flush();
	}


	public List<CourseLevelSession> enforceEnrollment(Long courseId) throws BuenOjoInconsistencyException
	{

		Enrollment currentEnrollment=null;
		Optional<Enrollment> enrollment;
		Optional<User> optionalUser = userRepository.findOneByLogin( ((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());

		User user = optionalUser.orElseThrow(() -> new BuenOjoInconsistencyException("No hay un usuario logueado"));

		Course course;

		removeAllByCourseAndCurrentUser(courseId);
		createAllCourseLevelSessionByCourseAndCurrentUser(courseId);
		try {
			enrollment = enrollmentRepository.findByUserIsCurrentUserAndCourse(courseId);
		} catch (Exception e){
			throw new BuenOjoInconsistencyException("Existe mas de un Enrollment para el usuario '"+ user.getLogin()+"'");
		}
		course = courseRepository.findOne(courseId);

		if (course == null) {
			throw new BuenOjoInconsistencyException("no existe un curso con el ID '"+"'");
		}

		currentEnrollment = enrollment.orElseGet(() -> {
			return enrollmentSimpleFactory.getEnrollment(user, course, EnrollmentStatus.Started);
		});
		if (currentEnrollment.getCurrentLevel() == null) {
			CourseLevelMap map = courseLevelMapRepository.findOneByCourse_IdAndParentIsNull(courseId).orElseThrow(() -> new BuenOjoInconsistencyException("El curso '"+courseId+"' no tiene un nivel inicial"));
			currentEnrollment.setCurrentLevel(map.getLevel());
		}
		currentEnrollment.setStatus(EnrollmentStatus.Started);

		enrollmentRepository.saveAndFlush(currentEnrollment);
		return courseLevelSessionRepository.findByUserIsCurrentUser();
	}

	public List<CourseLevelSession> randomEnrollment(Long courseId)
	{
		List<CurrentSession> currentSessions= currentSessionRepository.findByUserIsCurrentUser();
		currentSessionRepository.delete(currentSessions);

		removeAllByCourseAndCurrentUser(courseId);
		createAllCourseLevelSessionByCourseAndCurrentUserRandom(courseId);

		return courseLevelSessionRepository.findByUserIsCurrentUser();
	}

	/**
	 * Returns an object indication which is the next activity (if applicable).
	 * @param previous
	 * @return a transition representing the previous activity and the following
	 * @throws BuenOjoInconsistencyException
	 */
	public ActivityTransition nextActivity(Boolean won, Long courseId) throws BuenOjoInconsistencyException {
		User user = currentUser().orElseThrow(() -> new BuenOjoInconsistencyException("No hay un usuario logueado!"));

		Course course = courseRepository.findOne(courseId);
		if (course == null) {
			throw new BuenOjoInconsistencyException("El curso '"+courseId+"' no existe");

		}
		ActivityTransition transition = new ActivityTransition();
		CourseLevelSession courseLevelSession = courseLevelSessionRepository.findOneByUserIsCurrentUserAndCourseAndStatus(courseId, CourseLevelStatus.InProgress);
		
		Enrollment currentEnrollment = enrollmentRepository.findByUserIsCurrentUserAndCourse(course.getId()).orElseThrow(() -> new BuenOjoInconsistencyException("El usuario '"+ user.getLogin()+"' no está enrolado en el curso '"+course.getName()+"'"));

		if (courseLevelSession == null) {
			transition.setCourseFinished(true);
			transition.setNext(null);
			
		}
//		ZonedDateTime time = traceRepository.findLatestForEnrollment(currentEnrollment);
		Optional<ActivityTrace> t = traceRepository.findTopByEnrollmentIdOrderByStartDateDesc(currentEnrollment.getId());
		if (t.isPresent()){
			transition.setPrevious( t.get().getActivity());
		}
		

		if (courseLevelSession == null) {
			transition.setCourseFinished(true);
			transition.setNext(null);
			transition.setPrevious(null);
			return transition;
		}
		
		Optional <CourseLevelMap> nextMap = Optional.empty();
		Integer victories = traceRepository.countByEnrollmentAndLevelWhereIsPassed(currentEnrollment.getId(), currentEnrollment.getCurrentLevel());

		if (won) {
//			// update 
////			courseLevelSession.setApprovedExercises(courseLevelSession.getApprovedExercises()+1);
//			courseLevelSession.setPercentage(courseLevelSession.estimatePercentage());
			
			if (courseLevelSession.getCourseLevelMap().getThreshold() <= victories) {
				List<CourseLevelMap> nextLevels = courseLevelMapRepository.findByCourseAndParent(courseLevelSession.getCourseLevelMap().getCourse(), transition.getPrevious().getLevel());
				if (nextLevels.size() >0){
					if (nextLevels.size()==1){
						nextMap = Optional.of(nextLevels.get(0));
					}else {
						nextMap = BuenOjoRandomUtils.getRandom(nextLevels);
					}
				} else {
					transition.setCourseFinished(true);
				}
			}
		}

		// update course level session if user passed this level
		if (nextMap.isPresent()){
			courseLevelSession.setStatus(CourseLevelStatus.Done);
			courseLevelSessionRepository.saveAndFlush(courseLevelSession);
			courseLevelSession = courseLevelSessionRepository.findOneByCourseLevelMap(nextMap.get());
			courseLevelSession.setStatus(CourseLevelStatus.InProgress);
			courseLevelSessionRepository.saveAndFlush(courseLevelSession);
			
		}

		if (transition.getCourseFinished()){
			courseLevelSession.setStatus(CourseLevelStatus.Done);
			currentEnrollment.setStatus(EnrollmentStatus.Finished);
			enrollmentRepository.save(currentEnrollment);
			courseLevelSessionRepository.saveAndFlush(courseLevelSession);
		}else {
		setNextActivity(transition, currentEnrollment, courseLevelSession.getCourseLevelMap().getLevel());
		
			if (transition.getLevelUp()) {
				
				currentEnrollment.setCurrentLevel(transition.getNext().getLevel());
				enrollmentRepository.save(currentEnrollment);
			}
		}
		// determine whether this is a consistency error or if the user has passed all the exercises 
		if (courseLevelSession.getCourseLevelMap().getThreshold() <= victories && transition.getNext() == null ){
			transition.setCourseFinished(true);
		}else if (transition.getNext() == null){
			throw new BuenOjoInconsistencyException("No se pudo establecer una actividad siguiente para activity '" +
			transition.getPrevious() +
				"' para el nivel '"+courseLevelSession.getCourseLevelMap().getLevel()+"'");
		}
		return transition;

	}


	/**
	 * Get next activity with the given criteria:
	 * <br>
	 * let allActivities be all the activities for this level
	 * <br>
	 * let passedActivities be all passed activities for this enrollment and level
	 * <br>
	 * let failedActivities be all activities that where not passed
	 * <br>
	 * let incompleteActivities be all activities that where not completed ( endDate = passed = NULL)
	 * <br>
	 * let unattemptedActivities be (allActivities) - (passedActivities + failedActivities + incompleteActivities)
	 * <br>
	 * if unattemptedActivities is not empty, choose randomly one activity. Otherwise, select one activity from incompleteActivities falling back on the failedActivities
	 * @param transition
	 * @param enrollment
	 * @param level
	 * @throws BuenOjoInconsistencyException
	 */
	private void setNextActivity(ActivityTransition transition, Enrollment enrollment, Level level) throws BuenOjoInconsistencyException {

		Set<Activity> allActivities = level.getActivities();
		if (allActivities.isEmpty()){
			throw new BuenOjoInconsistencyException("No hay actividades disponibles. ¿Cargaron la tabla Activity?");
		}

		Set<Activity> passedActivities = filterActivities(traceRepository.findAllByEnrollmentAndLevelWhereIsPassed(enrollment.getId(), level));
		Set<Activity> failedActivites = filterActivities(traceRepository.findAllByEnrollmentAndLevelWhereIsNotPassed(enrollment.getId(), level));
		Set<Activity> incompleteActivities = filterActivities(traceRepository.findAllByEnrollmentAndLevelWhereEndDateIsNullAndIsNotPassedOrNull(enrollment.getId(), level));
		incompleteActivities.removeAll(passedActivities);
		Set<Activity> unattemptedActivities = new HashSet<>(allActivities);
		unattemptedActivities.removeAll(passedActivities);
		unattemptedActivities.removeAll(incompleteActivities);


		Optional<Activity> next = unattemptedActivities.stream().findAny();

		if (!next.isPresent()){
			next = incompleteActivities.stream().findAny();

			if(!next.isPresent()){
				next = failedActivites.stream().findAny();
				if (!next.isPresent()){
					next = Optional.empty();
				}
			}
		}

		if (next.isPresent()){
			transition.setNext(next.get());
			boolean levelUp = transition.getPrevious()!=null && !transition.getPrevious().getLevel().equals(transition.getNext().getLevel());
			transition.setLevelUp(levelUp);
		}else {
			transition.setNext(null);
		}
	}

	private Set<Activity> filterActivities(List<ActivityTrace> traces){

		Set<Activity> activities = traces.stream().map(t -> t.getActivity()).collect(Collectors.toSet());


		return activities;
	}

}
