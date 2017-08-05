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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ciis.buenojo.domain.ExerciseTip;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;
import com.ciis.buenojo.repository.ExerciseTipRepository;
import com.ciis.buenojo.service.ExerciseTipService;
import com.ciis.buenojo.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing ExerciseTip.
 */
@RestController
@RequestMapping("/api")
public class ExerciseTipResource {

    private final Logger log = LoggerFactory.getLogger(ExerciseTipResource.class);

    @Inject
    private ExerciseTipRepository exerciseTipRepository;

    @Inject
    private ExerciseTipService tipService;
    /**
     * POST  /exerciseTips -> Create a new exerciseTip.
     */
    @RequestMapping(value = "/exerciseTips",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExerciseTip> createExerciseTip(@Valid @RequestBody ExerciseTip exerciseTip) throws URISyntaxException {
        log.debug("REST request to save ExerciseTip : {}", exerciseTip);
        if (exerciseTip.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new exerciseTip cannot already have an ID").body(null);
        }
        ExerciseTip result = exerciseTipRepository.save(exerciseTip);
        return ResponseEntity.created(new URI("/api/exerciseTips/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("exerciseTip", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /exerciseTips -> Updates an existing exerciseTip.
     */
    @RequestMapping(value = "/exerciseTips",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExerciseTip> updateExerciseTip(@Valid @RequestBody ExerciseTip exerciseTip) throws URISyntaxException {
        log.debug("REST request to update ExerciseTip : {}", exerciseTip);
        if (exerciseTip.getId() == null) {
            return createExerciseTip(exerciseTip);
        }
        ExerciseTip result = exerciseTipRepository.save(exerciseTip);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("exerciseTip", exerciseTip.getId().toString()))
                .body(result);
    }

    /**
     * GET  /exerciseTips -> get all the exerciseTips.
     */
    @RequestMapping(value = "/exerciseTips",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ExerciseTip> getAllExerciseTips() {
        log.debug("REST request to get all ExerciseTips");
        return exerciseTipRepository.findAll();
    }

    /**
     * GET  /exerciseTips/:id -> get the "id" exerciseTip.
     */
    @RequestMapping(value = "/exerciseTips/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExerciseTip> getExerciseTip(@PathVariable Long id) {
        log.debug("REST request to get ExerciseTip : {}", id);
        return Optional.ofNullable(exerciseTipRepository.findOne(id))
            .map(exerciseTip -> new ResponseEntity<>(
                exerciseTip,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /exerciseTips/:id -> delete the "id" exerciseTip.
     */
    @RequestMapping(value = "/exerciseTips/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteExerciseTip(@PathVariable Long id) {
        log.debug("REST request to delete ExerciseTip : {}", id);
        exerciseTipRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("exerciseTip", id.toString())).build();
    }

    @RequestMapping(value = "/exerciseTips/upload/{courseId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> uploadExerciseTips(@RequestParam("file") MultipartFile file, @PathVariable Long courseId ) throws BuenOjoCSVParserException {
    	List<ExerciseTip> tips = tipService.tipsFromCSVForCourse(file, courseId);

    	return ResponseEntity.ok().headers(HeaderUtil.createEntitiesCreationAlert(ExerciseTip.class.getName(), new Integer(tips.size()).toString())).build();

    }

    @RequestMapping(value = "/exerciseTips",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAllExerciseTips() {
        log.debug("REST request to delete All ExerciseTips");
        exerciseTipRepository.deleteAll();
        return ResponseEntity.ok().headers(HeaderUtil.createAllEntityDeletionAlert("ExerciseTip")).build();
    }
    @RequestMapping(value = "/exerciseTips/random/{tagId}/satelliteImage/{image}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExerciseTip> getTip(@PathVariable Long tagId, @PathVariable Long image) {
        log.debug("REST request tip ImageCompletionExercise:" + tagId + "and tag "+ image) ;
         
        return tipService.tipForTagAndSatelliteImage(tagId, image)
                .map(exerciseTip -> new ResponseEntity<>(
                    exerciseTip,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
