package com.ciis.buenojo.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciis.buenojo.domain.CourseLevelSession;
import com.ciis.buenojo.domain.CurrentSession;
import com.ciis.buenojo.domain.factories.CourseLevelSessionSimpleFactory;
import com.ciis.buenojo.domain.factories.CurrentSessionSimpleFactory;
import com.ciis.buenojo.repository.CourseLevelSessionRepository;
import com.ciis.buenojo.repository.CurrentSessionRepository;
import com.ciis.buenojo.repository.UserRepository;
import com.ciis.buenojo.security.CustomUserDetails;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class CurrentSessionService {

	private final Logger log = LoggerFactory.getLogger(CurrentSessionService.class);

	@Inject
	private CourseLevelSessionRepository courseLevelSessionRepository;

	@Inject
	private CurrentSessionRepository currentSessionRepository;

	@Inject
	private UserRepository userRepository;

	private CourseLevelSessionSimpleFactory courseLevelSessionSimpleFactory = new CourseLevelSessionSimpleFactory();

	private CurrentSessionSimpleFactory currentSessionSimpleFactory = new CurrentSessionSimpleFactory();

	private Optional<CurrentSession> cachedCurrentSession = Optional.empty();

	private CourseLevelSessionService courseLevelSessionService = new CourseLevelSessionService();

	public CurrentSession getCurrentSession() {
/*		if (getCachedCurrentSession().isPresent())
			return getCachedCurrentSession().get();
		else {
	*/		Optional<CurrentSession> currentSession;
			Optional<com.ciis.buenojo.domain.User> user;
			user = userRepository.findOneByLogin(
					((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
							.getUsername());
			List<CurrentSession> currentSessions = currentSessionRepository.findByUserIsCurrentUser();
			if (!currentSessions.isEmpty()) {
		//		setCachedCurrentSession(Optional.of(currentSessions.get(0)));
				return currentSessions.get(0);
			} else if (user.isPresent()) {
				currentSession = Optional.of(currentSessionSimpleFactory.getNewSessionFor(user.get()));
				setCachedCurrentSession(currentSession);
				currentSessionRepository.saveAndFlush(currentSession.get());
				return currentSession.get();
			} else {
				log.error("Orphan Session. Forcing Logout");
				SecurityContextHolder.getContext().setAuthentication(null);
				return null;
			}
		}
	//}

	public CurrentSession getCurrentSessionAndUpdateCourseLevelSession(CourseLevelSession courseLevelSession) {
		CurrentSession currentSession;
		currentSession = getCurrentSession();
		currentSession.setCourseLevelSession(courseLevelSession);
		currentSessionRepository.saveAndFlush(currentSession);
		return currentSession;
	}

	@Transactional
	public CurrentSession addPointsPercentageAndExerciseCountToCourseLevelSessionFromCurrentSession(
			Double experiencePoints, Double percentage) {
		CurrentSession currentSession;
		CourseLevelSession courseLevelSession;
		currentSession = getCurrentSession();
		courseLevelSession = currentSession.getCourseLevelSession();

		courseLevelSessionService.addPointsPercentageAndExerciseCountToCourseLevelSession(courseLevelSession,
				experiencePoints, percentage);
		courseLevelSessionService.updateStatusCourseLevelSession(courseLevelSession, experiencePoints, percentage);

		courseLevelSessionRepository.saveAndFlush(courseLevelSession);
		return currentSession;
	}

	private Optional<CurrentSession> getCachedCurrentSession() {
		return cachedCurrentSession;
	}

	public void setCachedCurrentSession(Optional<CurrentSession> cachedCurrentSession) {
		this.cachedCurrentSession = cachedCurrentSession;
	}

	public Optional<CurrentSession> updateCurrentSessionTempId(Long tempId) {
		getCurrentSession().setTempId(tempId);
		currentSessionRepository.saveAndFlush(getCurrentSession());
		return Optional.of(getCurrentSession());
	}

	public Long getCurrentSessionTempId(Long tempId) {
		return getCurrentSession().getTempId();

	}

}
