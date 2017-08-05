package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.Course;
import com.ciis.buenojo.domain.Enrollment;
import com.ciis.buenojo.domain.enumeration.EnrollmentStatus;
import com.ciis.buenojo.repository.EnrollmentRepository;
import com.ciis.buenojo.service.CourseLevelSessionService;
import com.ciis.buenojo.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Enrollment.
 */
@RestController
@RequestMapping("/api")
public class EnrollmentResource {

    private final Logger log = LoggerFactory.getLogger(EnrollmentResource.class);

    @Inject
    private EnrollmentRepository enrollmentRepository;
    
    @Inject
    private CourseLevelSessionService sessionService;
    /**
     * POST  /enrollments -> Create a new enrollment.
     */
    @RequestMapping(value = "/enrollments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Enrollment> createEnrollment(@RequestBody Enrollment enrollment) throws URISyntaxException {
        log.debug("REST request to save Enrollment : {}", enrollment);
        if (enrollment.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new enrollment cannot already have an ID").body(null);
        }
        Enrollment result = enrollmentRepository.save(enrollment);
        return ResponseEntity.created(new URI("/api/enrollments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("enrollment", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /enrollments -> Updates an existing enrollment.
     */
    @RequestMapping(value = "/enrollments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Enrollment> updateEnrollment(@RequestBody Enrollment enrollment) throws URISyntaxException {
        log.debug("REST request to update Enrollment : {}", enrollment);
        if (enrollment.getId() == null) {
            return createEnrollment(enrollment);
        }
        Enrollment result = enrollmentRepository.save(enrollment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("enrollment", enrollment.getId().toString()))
            .body(result);
    }
    
    /**
     * GET  /courses/:id/enrollments -> get the enrollments for the course with "id".
     */
    @RequestMapping(value = "/courses/{id}/enrollments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Enrollment> getCourseEnrollments(@PathVariable Long id) {
        log.debug("REST request to get all enrollments for Course : {}", id);
        return enrollmentRepository.findAllByCourseId(id);
    }
    
    /**
     * PUT  /enrollments -> Updates an existing enrollment.
     */
    @RequestMapping(value = "/enrollments/finish",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> finishEnrollment(@RequestBody Enrollment enrollment) throws URISyntaxException {
        log.debug("REST request to update Enrollment : {}", enrollment);
        if (enrollment.getId() == null) {
            return ResponseEntity.notFound().headers(HeaderUtil.createEntityUpdateAlert("Enrollment", "no se puede finalizar un enrollment que no existe")).build();
        }
        
        
        if (sessionService.finishEnrollment(enrollment)==null){
        	return ResponseEntity.notFound().headers(HeaderUtil.createEntityUpdateAlert("Enrollment", "no se puede finalizar un enrollment:"+enrollment)).build();
        }
        
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("Enrollment", "finalizó con éxito")).build();
    }
    
    /**
     * GET  /enrollments -> get all the enrollments.
     */
    @RequestMapping(value = "/enrollments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Enrollment> getAllEnrollments() {
        log.debug("REST request to get all Enrollments");
        return enrollmentRepository.findAll();
    }

    /**
     * GET  /enrollments -> get all the enrollments of the currentUser.
     */
    @RequestMapping(value = "/enrollmentsCurrentUser",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Enrollment> getAllEnrollmentsCurrentUser() {
        log.debug("REST request to get all Enrollments of the current user");
        return enrollmentRepository.findByUserIsCurrentUser();
    }
    /**
     * GET  /enrollments -> get all the enrollments of the currentUser.
     */
    @RequestMapping(value = "/enrollmentCurrentUser/{courseId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Enrollment> getAllEnrollmentsCurrentUserOnCourse(@PathVariable Long courseId) {
        log.debug("REST request to get all Enrollments of the current user");
        EnumSet<EnrollmentStatus> inProgress = EnumSet.of(EnrollmentStatus.InProgress, EnrollmentStatus.Started);
        
        
        Optional<Enrollment> enrollment = enrollmentRepository.findByUserIsCurrentUser().stream().
        		filter(e -> e.getCourse().getId().equals(courseId) && inProgress.contains(e.getStatus())).findFirst();
        return enrollment
                .map(e -> new ResponseEntity<Enrollment>(e,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    /**
     * GET  /enrollments/:id -> get the "id" enrollment.
     */
    @RequestMapping(value = "/enrollments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Enrollment> getEnrollment(@PathVariable Long id) {
        log.debug("REST request to get Enrollment : {}", id);
        return Optional.ofNullable(enrollmentRepository.findOne(id))
            .map(enrollment -> new ResponseEntity<>(
                enrollment,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    /**
     * GET  /enrollments/:id -> get the "id" enrollment.
     */
    @RequestMapping(value = "/enrollments/user/{userId}/course/{courseId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Enrollment> getEnrollmentForUserAndCourse(@PathVariable Long userId,@PathVariable Long courseId) {
        log.debug("REST request to get Enrollment For User: {}", userId);
        return Optional.ofNullable(enrollmentRepository.findOneByUserIdAndCourseId(userId, courseId))
            .map(enrollment -> new ResponseEntity<>(
                enrollment,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    /**
     * DELETE  /enrollments/:id -> delete the "id" enrollment.
     */
    @RequestMapping(value = "/enrollments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        log.debug("REST request to delete Enrollment : {}", id);
        enrollmentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("enrollment", id.toString())).build();
    }
}
