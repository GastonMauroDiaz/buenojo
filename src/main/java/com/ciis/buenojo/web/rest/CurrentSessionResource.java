package com.ciis.buenojo.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ciis.buenojo.domain.CurrentSession;
import com.ciis.buenojo.repository.CurrentSessionRepository;
import com.ciis.buenojo.service.CurrentSessionService;
import com.ciis.buenojo.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing CurrentSession.
 */
@RestController
@RequestMapping("/api")
public class CurrentSessionResource {

    private final Logger log = LoggerFactory.getLogger(CurrentSessionResource.class);

    @Inject
    private CurrentSessionRepository currentSessionRepository;

    @Inject
    private CurrentSessionService currentSessionService;

    /**
     * POST  /currentSessions -> Create a new currentSession.
     */
    @RequestMapping(value = "/currentSessions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CurrentSession> createCurrentSession(@RequestBody CurrentSession currentSession) throws URISyntaxException {
        log.debug("REST request to save CurrentSession : {}", currentSession);
        if (currentSession.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new currentSession cannot already have an ID").body(null);
        }
        CurrentSession result = currentSessionRepository.save(currentSession);
        return ResponseEntity.created(new URI("/api/currentSessions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("currentSession", result.getId().toString()))
            .body(result);
    }

    /**
     * POST  /currentSessions -> Create a new currentSession.
     */
    @RequestMapping(value = "/currentSessionsCustom",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CurrentSession> createCurrentSessionCustom(@RequestBody CurrentSession currentSession) throws URISyntaxException {
        log.debug("REST request to save CurrentSession : {}", currentSession);
        if (currentSession.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new currentSession cannot already have an ID").body(null);
        }
        CurrentSession result = currentSessionService.getCurrentSession();
        return ResponseEntity.created(new URI("/api/currentSessions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("currentSession", result.getId().toString()))
            .body(result);
    }
    
    /**
     * PUT  /currentSessions -> Updates an existing currentSession.
     */
    @RequestMapping(value = "/currentSessions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CurrentSession> updateCurrentSession(@RequestBody CurrentSession currentSession) throws URISyntaxException {
        log.debug("REST request to update CurrentSession : {}", currentSession);
        if (currentSession.getId() == null) {
            return createCurrentSession(currentSession);
        }
        CurrentSession result = currentSessionRepository.save(currentSession);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("currentSession", currentSession.getId().toString()))
            .body(result);
    }


    /**
     * PUT  /currentSessions -> Updates an existing currentSession.
     */
    @RequestMapping(value = "/currentSessionsTempId",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CurrentSession> updateCurrentSession(@RequestBody Long tempId) throws URISyntaxException {
        log.debug("REST request to update CurrentSession temp Id : {}", tempId);
        Optional<CurrentSession> currentSession;
        currentSession =currentSessionService.updateCurrentSessionTempId(tempId);		
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("currentSession", currentSession.get().getId().toString()))
            .body(currentSession.get());
    }

    /**
     * GET  /currentSessions -> get all the currentSessions.
     */
    @RequestMapping(value = "/currentSessions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CurrentSession> getAllCurrentSessions(@RequestParam(required = false) String filter) {
        if ("user-is-null".equals(filter)) {
            log.debug("REST request to get all CurrentSessions where user is null");
            return StreamSupport
                .stream(currentSessionRepository.findAll().spliterator(), false)
                .filter(currentSession -> currentSession.getUser() == null)
                .collect(Collectors.toList());
        }

        if ("courselevelsession-is-null".equals(filter)) {
            log.debug("REST request to get all CurrentSessions where courseLevelSession is null");
            return StreamSupport
                .stream(currentSessionRepository.findAll().spliterator(), false)
                .filter(currentSession -> currentSession.getCourseLevelSession() == null)
                .collect(Collectors.toList());
        }

        log.debug("REST request to get all CurrentSessions");
        return currentSessionRepository.findAll();
    }

    /**
     * GET  /currentSessions/:id -> get the "id" currentSession.
     */
    @RequestMapping(value = "/currentSessions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CurrentSession> getCurrentSession(@PathVariable Long id) {
        log.debug("REST request to get CurrentSession : {}", id);
        return Optional.ofNullable(currentSessionRepository.findOne(id))
            .map(currentSession -> new ResponseEntity<>(
                currentSession,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /currentSessions/:id -> delete the "id" currentSession.
     */
    @RequestMapping(value = "/currentSessions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCurrentSession(@PathVariable Long id) {
        log.debug("REST request to delete CurrentSession : {}", id);
        currentSessionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("currentSession", id.toString())).build();
    }
}
