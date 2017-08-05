package com.ciis.buenojo.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RestController;

import com.ciis.buenojo.domain.CourseLevelMap;
import com.ciis.buenojo.repository.CourseLevelMapRepository;
import com.ciis.buenojo.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing CourseLevelMap.
 */
@RestController
@RequestMapping("/api")
public class CourseLevelMapResource {

    private final Logger log = LoggerFactory.getLogger(CourseLevelMapResource.class);

    @Inject
    private CourseLevelMapRepository courseLevelMapRepository;

    /**
     * POST  /courseLevelMaps -> Create a new courseLevelMap.
     */
    @RequestMapping(value = "/courseLevelMaps",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CourseLevelMap> createCourseLevelMap(@RequestBody CourseLevelMap courseLevelMap) throws URISyntaxException {
        log.debug("REST request to save CourseLevelMap : {}", courseLevelMap);
        if (courseLevelMap.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new courseLevelMap cannot already have an ID").body(null);
        }
        CourseLevelMap result = courseLevelMapRepository.save(courseLevelMap);
        return ResponseEntity.created(new URI("/api/courseLevelMaps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("courseLevelMap", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /courseLevelMaps -> Updates an existing courseLevelMap.
     */
    @RequestMapping(value = "/courseLevelMaps",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CourseLevelMap> updateCourseLevelMap(@RequestBody CourseLevelMap courseLevelMap) throws URISyntaxException {
        log.debug("REST request to update CourseLevelMap : {}", courseLevelMap);
        if (courseLevelMap.getId() == null) {
            return createCourseLevelMap(courseLevelMap);
        }
        CourseLevelMap result = courseLevelMapRepository.save(courseLevelMap);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("courseLevelMap", courseLevelMap.getId().toString()))
            .body(result);
    }

    /**
     * GET  /courseLevelMaps -> get all the courseLevelMaps.
     */
    @RequestMapping(value = "/courseLevelMaps",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CourseLevelMap> getAllCourseLevelMaps() {
        log.debug("REST request to get all CourseLevelMaps");
        return courseLevelMapRepository.findAll();
    }

    /**
     * GET  /courseLevelMaps/:id -> get the "id" courseLevelMap.
     */
    @RequestMapping(value = "/courseLevelMaps/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CourseLevelMap> getCourseLevelMap(@PathVariable Long id) {
        log.debug("REST request to get CourseLevelMap : {}", id);
        return Optional.ofNullable(courseLevelMapRepository.findOne(id))
            .map(courseLevelMap -> new ResponseEntity<>(
                courseLevelMap,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /courseLevelMaps/:id -> delete the "id" courseLevelMap.
     */
    @RequestMapping(value = "/courseLevelMaps/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCourseLevelMap(@PathVariable Long id) {
        log.debug("REST request to delete CourseLevelMap : {}", id);
        courseLevelMapRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("courseLevelMap", id.toString())).build();
    }
    
    /**
     * GET  /courseLevelMaps/course/:courseId -> get all the courseLevelMaps of the course.
     */
    @RequestMapping(value = "/courseLevelMaps/course/{courseId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CourseLevelMap> getAllCourseLevelMapsByCourse(@PathVariable Long courseId) {
        log.debug("REST request to get all CourseLevelMaps of Course {}", courseId);
        return  courseLevelMapRepository.findByCourse_Id(courseId);
      
    }

}
