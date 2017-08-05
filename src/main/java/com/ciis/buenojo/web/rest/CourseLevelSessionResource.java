package com.ciis.buenojo.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ciis.buenojo.domain.CourseLevelMap;
import com.ciis.buenojo.domain.CourseLevelSession;
import com.ciis.buenojo.domain.enumeration.CourseLevelStatus;
import com.ciis.buenojo.exceptions.BuenOjoInconsistencyException;
import com.ciis.buenojo.repository.CourseLevelSessionRepository;
import com.ciis.buenojo.service.CourseLevelSessionService;
import com.ciis.buenojo.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing CourseLevelSession.
 */
@RestController
@RequestMapping("/api")
public class CourseLevelSessionResource {

    private final Logger log = LoggerFactory.getLogger(CourseLevelSessionResource.class);

    @Inject
    private CourseLevelSessionRepository courseLevelSessionRepository;

    @Inject
    private CourseLevelSessionService courseLevelSessionService;

    /**
     * POST  /courseLevelSessions -> Create a new courseLevelSession.
     */
    @RequestMapping(value = "/courseLevelSessions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CourseLevelSession> createCourseLevelSession(@Valid @RequestBody CourseLevelSession courseLevelSession) throws URISyntaxException {
        log.debug("REST request to save CourseLevelSession : {}", courseLevelSession);
        if (courseLevelSession.getId() != null) {
        	return ResponseEntity.badRequest().header("Failure", "A new courseLevelSession cannot already have an ID").body(null);
        }
        CourseLevelSession result = courseLevelSessionRepository.save(courseLevelSession);
        return ResponseEntity.created(new URI("/api/courseLevelSessions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("courseLevelSession", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /courseLevelSessions -> Updates an existing courseLevelSession.
     */
    @RequestMapping(value = "/courseLevelSessions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CourseLevelSession> updateCourseLevelSession(@Valid @RequestBody CourseLevelSession courseLevelSession) throws URISyntaxException {
        log.debug("REST request to update CourseLevelSession : {}", courseLevelSession);
        if (courseLevelSession.getId() == null) {
            return createCourseLevelSession(courseLevelSession);
        }
        CourseLevelSession result = courseLevelSessionRepository.save(courseLevelSession);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("courseLevelSession", courseLevelSession.getId().toString()))
            .body(result);
    }

    /**
     * GET  /courseLevelSessions -> Updates an existing courseLevelSession.
     */
    @RequestMapping(value = "/courseLevelSessions/{id}/add/{points}/{experience}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CourseLevelSession> addPointsPercentageAndExerciseCountToCourseLevelSession(@PathVariable Long id, @PathVariable Double points, @PathVariable Double experience) throws URISyntaxException {
        CourseLevelSession courseLevelSession= courseLevelSessionRepository.findOne(id);
    	log.debug("REST request to update CourseLevelSession : {}", courseLevelSession);

        CourseLevelSession result = courseLevelSessionService.addPointsPercentageAndExerciseCountToCourseLevelSession(courseLevelSession, points, experience);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("courseLevelSession", courseLevelSession.getId().toString()))
            .body(result);
    }


    /**
     * GET  /courseLevelSessions -> get all the courseLevelSessions.
     */
    @RequestMapping(value = "/courseLevelSessions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CourseLevelSession> getAllCourseLevelSessions() {
        log.debug("REST request to get all CourseLevelSessions");
        return courseLevelSessionRepository.findAll();
    }

    /**
     * GET  /courseLevelSessions/:id -> get the "id" courseLevelSession.
     */
    @RequestMapping(value = "/courseLevelSessions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CourseLevelSession> getCourseLevelSession(@PathVariable Long id) {
        log.debug("REST request to get CourseLevelSession : {}", id);
        return Optional.ofNullable(courseLevelSessionRepository.findOne(id))
            .map(courseLevelSession -> new ResponseEntity<>(
                courseLevelSession,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /courseLevelSessions/:id -> delete the "id" courseLevelSession.
     */
    @RequestMapping(value = "/courseLevelSessions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCourseLevelSession(@PathVariable Long id) {
        log.debug("REST request to delete CourseLevelSession : {}", id);
        courseLevelSessionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("courseLevelSession", id.toString())).build();
    }


    /**
     * GET  /courseLevelSession/courseLevelMap/:courseLevelMaps -> get all the courseLevelMaps of the course.
     */
    @RequestMapping(value = "/courseLevelSessionCurrentUserAndCourse/{courseId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CourseLevelSession> getAllCourseLevelSessionByCourseAndCurrentUser(@PathVariable Long courseId) {
        log.debug("REST request to get all CourseLevelSession of current user for Course , {}", courseId);
        List<CourseLevelSession> courseLevelSessions =courseLevelSessionRepository.findByUserIsCurrentUserAndCourse(courseId);
        if (courseLevelSessions.size()==0)
        {
       /* Orphan course, add points from exercise without course */
        	courseLevelSessionService.createLevelSessionForCurrentUser();
            courseLevelSessions =courseLevelSessionRepository.findByUserIsCurrentUserAndCourse(courseId);

        }
        log.debug("Course Level Sessions: "+ courseLevelSessions.size() );
        return courseLevelSessions;
    }
    /**
     * GET  /currentCourseLevelSessionCurrentUserAndCourse/:courseId -> get current course level session for this course
     */
    @RequestMapping(value = "/currentCourseLevelSessionCurrentUserAndCourse/{courseId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CourseLevelSession> getCurrentCourseLevelSessionByCourseAndCurrentUser(@PathVariable Long courseId) {
        log.debug("REST request to get Current CourseLevelSession of Course {}", courseId);
        CourseLevelSession courseLevelSession=courseLevelSessionRepository.findOneByUserIsCurrentUserAndCourseAndStatus(courseId, CourseLevelStatus.InProgress);
        
        return Optional.ofNullable(courseLevelSession)
                .map(session -> new ResponseEntity<>(
                    courseLevelSession,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    /**
     * GET  /courseLevelSessions/user/{userId}/course/{courseId}-> get current course level session for this course and user
     */
    @RequestMapping(value = "/courseLevelSessions/user/{userId}/course/{courseId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CourseLevelSession> getCurrentCourseLevelSessionByCourseAndUser(@PathVariable Long courseId, @PathVariable Long userId) {
        log.debug("REST request to get CourseLevelSessions of user {"+userId+"} for Course {}", courseId);
       
        return courseLevelSessionRepository.findByUserIdAndCourseId(userId, courseId);
        		
    }
    
    /**
     * GET  /enforceEnrollment/:courseId -> get all the courseLevelMaps of the course.
     */
    @RequestMapping(value = "/enforceEnrollment/{courseId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CourseLevelSession> enforceEnrollment(@PathVariable Long courseId) throws BuenOjoInconsistencyException {
        log.debug("REST request to enforce enrolment of the current user in the current course, sets all level as not started, cleaning all previous data first", courseId);
             return courseLevelSessionService.enforceEnrollment(courseId);

}

    /**
     * GET  /enforceEnrollment/:courseId -> get all the courseLevelMaps of the course.
     */
    @RequestMapping(value = "/enforceRandomEnrollment/{courseId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CourseLevelSession> enforceRandomEnrollment(@PathVariable Long courseId) {
        log.debug("REST request to enforce enrolment of the current user in the current course, sets all level as not started, cleaning all previous data first", courseId);
             return courseLevelSessionService.randomEnrollment(courseId);

}
}
