package com.ciis.buenojo.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ciis.buenojo.domain.Activity;
import com.ciis.buenojo.domain.ActivityTransition;
import com.ciis.buenojo.exceptions.BuenOjoInconsistencyException;
import com.ciis.buenojo.repository.ActivityRepository;
import com.ciis.buenojo.service.CourseLevelSessionService;
import com.ciis.buenojo.web.rest.util.HeaderUtil;
import com.ciis.buenojo.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing Activity.
 */
@RestController
@RequestMapping("/api")
public class ActivityResource {

    private final Logger log = LoggerFactory.getLogger(ActivityResource.class);

    @Inject
    private ActivityRepository activityRepository;

    @Inject 
    private CourseLevelSessionService courseLevelSessionService;
    
    /**
     * POST  /activities -> Create a new activity.
     */
    @RequestMapping(value = "/activities",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Activity> createActivity(@Valid @RequestBody Activity activity) throws URISyntaxException {
        log.debug("REST request to save Activity : {}", activity);
        if (activity.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new activity cannot already have an ID").body(null);
        }
        Activity result = activityRepository.save(activity);
        return ResponseEntity.created(new URI("/api/activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("activity", result.getId().toString()))
            .body(result);
    }
    
    /**
     * POST  /activities -> get a the next activity.
     * @throws BuenOjoInconsistencyException 
     */
    @RequestMapping(value = "/activities/{courseId}/next/{won}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ActivityTransition> nextActivity(@PathVariable Long courseId, @PathVariable Boolean won) throws URISyntaxException {
    	
    	
        log.debug("REST request to get next activity for course: {}", courseId);
//        if (activity.getId() != null) {
//            return ResponseEntity.badRequest().header("Failure", "A new activity cannot already have an ID").body(null);
//        }
        try {
        	ActivityTransition transition = courseLevelSessionService.nextActivity(won, courseId);
        	if (transition != null){
        		return new ResponseEntity<ActivityTransition> (transition,HttpStatus.OK);
        	}
        	return ResponseEntity.badRequest().headers(HeaderUtil.createBadRequestHeaderAlert("error al obtener una actividad sigiente para el curso '"+courseId+"'")).body(null);
                    	
        } catch (BuenOjoInconsistencyException e){
        	return ResponseEntity.badRequest().headers(HeaderUtil.createBadRequestHeaderAlert("error al obtener una actividad sigiente para el curso '"+courseId+"'")).body(null);
        }
    }

    /**
     * PUT  /activities -> Updates an existing activity.
     */
    @RequestMapping(value = "/activities",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Activity> updateActivity(@Valid @RequestBody Activity activity) throws URISyntaxException {
        log.debug("REST request to update Activity : {}", activity);
        if (activity.getId() == null) {
            return createActivity(activity);
        }
        Activity result = activityRepository.save(activity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("activity", activity.getId().toString()))
            .body(result);
    }
    /**
     * GET  /activities -> get all the activities.
     */
    @RequestMapping(value = "/activities/level/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Activity>> getAllActivitiesByLevel(@PathVariable Long id)
        throws URISyntaxException {
        
    	List<Activity> activities = new ArrayList<>(activityRepository.findByLevelId(id));
    	
    	return new ResponseEntity<>(activities,HttpStatus.OK);
    }
    
    /**
     * GET  /activities -> get all the activities.
     */
    @RequestMapping(value = "/activities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Activity>> getAllActivities(Pageable pageable)
        throws URISyntaxException {
        Page<Activity> page = activityRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /activities/:id -> get the "id" activity.
     */
    @RequestMapping(value = "/activities/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Activity> getActivity(@PathVariable Long id) {
        log.debug("REST request to get Activity : {}", id);
        return Optional.ofNullable(activityRepository.findOne(id))
            .map(activity -> new ResponseEntity<>(
                activity,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /activities/:id -> delete the "id" activity.
     */
    @RequestMapping(value = "/activities/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        log.debug("REST request to delete Activity : {}", id);
        activityRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("activity", id.toString())).build();
    }
}
