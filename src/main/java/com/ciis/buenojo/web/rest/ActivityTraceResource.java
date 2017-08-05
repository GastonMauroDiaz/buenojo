package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.Activity;
import com.ciis.buenojo.domain.ActivityTrace;
import com.ciis.buenojo.exceptions.BuenOjoInconsistencyException;
import com.ciis.buenojo.repository.ActivityTraceRepository;
import com.ciis.buenojo.service.ActivityTraceService;
import com.ciis.buenojo.web.rest.util.HeaderUtil;
import com.ciis.buenojo.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing ActivityTrace.
 */
@RestController
@RequestMapping("/api")
public class ActivityTraceResource {

    private final Logger log = LoggerFactory.getLogger(ActivityTraceResource.class);

    @Inject
    private ActivityTraceRepository activityTraceRepository;
    
    @Inject 
    private ActivityTraceService traceService;
    /**
     * POST  /activityTraces -> Create a new activityTrace.
     */
    @RequestMapping(value = "/activityTraces",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ActivityTrace> createActivityTrace(@Valid @RequestBody ActivityTrace activityTrace) throws URISyntaxException {
        log.debug("REST request to save ActivityTrace : {}", activityTrace);
        if (activityTrace.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new activityTrace cannot already have an ID").body(null);
        }
        ActivityTrace result = activityTraceRepository.save(activityTrace);
        return ResponseEntity.created(new URI("/api/activityTraces/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("activityTrace", result.getId().toString()))
                .body(result);
    }
    /**
     * POST  /activityTraces/start -> Create a new activityTrace from an activity, starting now for the current enrollment and course.
     * @throws BuenOjoInconsistencyException 
     */
    @RequestMapping(value = "/activityTraces/{activityId}/start",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ActivityTrace> startActivityTrace(@PathVariable Long activityId) throws URISyntaxException, BuenOjoInconsistencyException {
        log.debug("REST request to start Activity ID: {}", activityId);
        if (activityId == null) {
            return ResponseEntity.badRequest().header("Failure", "A new activityTrace cannot already have an ID").body(null);
        }
        ActivityTrace result = activityTraceRepository.save(traceService.startActivity(activityId));
        
        return ResponseEntity.ok().body(result);
    }
    /**
     * PUT  /activityTraces -> Updates an existing activityTrace.
     */
    @RequestMapping(value = "/activityTraces",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ActivityTrace> updateActivityTrace(@Valid @RequestBody ActivityTrace activityTrace) throws URISyntaxException {
        log.debug("REST request to update ActivityTrace : {}", activityTrace);
        if (activityTrace.getId() == null) {
            return createActivityTrace(activityTrace);
        }
        ActivityTrace result = activityTraceRepository.save(activityTrace);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("activityTrace", activityTrace.getId().toString()))
                .body(result);
    }
    /**
     * PUT  /activityTraces -> Updates an existing activityTrace.
     */
    @RequestMapping(value = "/activityTraces/end",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ActivityTrace> endActivityTrace(@Valid @RequestBody ActivityTrace activityTrace) throws URISyntaxException {
        log.debug("REST request to update ActivityTrace : {}", activityTrace);
        if (activityTrace.getId() == null) {
        	return ResponseEntity.badRequest().header("Failure", "no se puede finalizar un registro de actividad que nunca se creó").body(null);
        }
        
        if (activityTrace.getStartDate() == null) {
        	return ResponseEntity.badRequest().header("Failure", "no se puede finalizar un registro de actividad que nunca comenzó").body(null);
        }
        Comparator<ZonedDateTime> comparator = Comparator.comparing(zdt -> ((ZonedDateTime) zdt).truncatedTo(ChronoUnit.SECONDS));
        if (comparator.compare(activityTrace.getStartDate(), activityTrace.getEndDate()) >= 0) {
        	return ResponseEntity.badRequest().header("Failure", "un registro de actividad no puede tener una fecha de finalización anterior a la de comienzo").body(null);
        }
        
        ActivityTrace result = activityTraceRepository.save(activityTrace);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("activityTrace", activityTrace.getId().toString()))
                .body(result);
    }
    /**
     * GET  /activityTraces -> get all the activityTraces.
     */
    @RequestMapping(value = "/activityTraces",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ActivityTrace>> getAllActivityTraces(Pageable pageable, @RequestParam(required = false) String filter)
        throws URISyntaxException {
        if ("enrollment-is-null".equals(filter)) {
            log.debug("REST request to get all ActivityTraces where enrollment is null");
            return new ResponseEntity<>(StreamSupport
                .stream(activityTraceRepository.findAll().spliterator(), false)
                .filter(activityTrace -> activityTrace.getEnrollment() == null)
                .collect(Collectors.toList()), HttpStatus.OK);
        }
        
        if ("activity-is-null".equals(filter)) {
            log.debug("REST request to get all ActivityTraces where activity is null");
            return new ResponseEntity<>(StreamSupport
                .stream(activityTraceRepository.findAll().spliterator(), false)
                .filter(activityTrace -> activityTrace.getActivity() == null)
                .collect(Collectors.toList()), HttpStatus.OK);
        }
        
        Page<ActivityTrace> page = activityTraceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activityTraces");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /activityTraces -> get all the activityTraces.
     */
    @RequestMapping(value = "/activityTraces/enrollment/{enrollmentId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ActivityTrace>> getAllActivityTraces(Pageable pageable, @PathVariable Long enrollmentId)
        throws URISyntaxException {
  
        Page<ActivityTrace> page = activityTraceRepository.findAllByEnrollmentIdOrderByStartDateAsc(enrollmentId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/activityTraces/enrollment/");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /activityTraces/:id -> get the "id" activityTrace.
     */
    @RequestMapping(value = "/activityTraces/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ActivityTrace> getActivityTrace(@PathVariable Long id) {
        log.debug("REST request to get ActivityTrace : {}", id);
        return Optional.ofNullable(activityTraceRepository.findOne(id))
            .map(activityTrace -> new ResponseEntity<>(
                activityTrace,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /activityTraces/:id -> delete the "id" activityTrace.
     */
    @RequestMapping(value = "/activityTraces/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteActivityTrace(@PathVariable Long id) {
        log.debug("REST request to delete ActivityTrace : {}", id);
        activityTraceRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("activityTrace", id.toString())).build();
    }
}
