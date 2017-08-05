package com.ciis.buenojo.web.rest;

import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ciis.buenojo.domain.nonPersistent.CourseInformation;
import com.ciis.buenojo.service.CourseInformationService;
import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing Course.
 */
@RestController
@RequestMapping("/api")
public class CourseInformationResource {

    private final Logger log = LoggerFactory.getLogger(CourseInformationResource.class);

    @Inject
    private CourseInformationService courseInformationService;

  
    /**
     * GET  /courses/:id -> get the "id" course.
     */
    @RequestMapping(value = "/courseInformation/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CourseInformation> getCourseInformation(@PathVariable Long id) {
        log.debug("REST request to get CourseInformation : {}", id);
        return Optional.ofNullable(courseInformationService.getCourseInformation(id))
            .map(course -> new ResponseEntity<>(
                course,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    /**
     * GET  /courseInformation/:courseId/user/:userId -> get the "id" course.
     */
    @RequestMapping(value = "/courseInformation/{courseId}/user/{userId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CourseInformation> getCourseInformationForUser(@PathVariable Long courseId, @PathVariable Long userId) {
        log.debug("REST request to get CourseInformation for Course : {"+courseId+"} and User{}", courseId);
        return Optional.ofNullable(courseInformationService.getCourseInformation(courseId, userId))
            .map(course -> new ResponseEntity<>(
                course,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    
}
