package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.MultipleChoiceExerciseContainer;
import com.ciis.buenojo.repository.MultipleChoiceExerciseContainerRepository;
import com.ciis.buenojo.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing MultipleChoiceExerciseContainer.
 */
@RestController
@RequestMapping("/api")
public class MultipleChoiceExerciseContainerResource {

    private final Logger log = LoggerFactory.getLogger(MultipleChoiceExerciseContainerResource.class);

    @Inject
    private MultipleChoiceExerciseContainerRepository multipleChoiceExerciseContainerRepository;

    /**
     * POST  /multipleChoiceExerciseContainers -> Create a new multipleChoiceExerciseContainer.
     */
    @RequestMapping(value = "/multipleChoiceExerciseContainers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MultipleChoiceExerciseContainer> createMultipleChoiceExerciseContainer(@Valid @RequestBody MultipleChoiceExerciseContainer multipleChoiceExerciseContainer) throws URISyntaxException {
        log.debug("REST request to save MultipleChoiceExerciseContainer : {}", multipleChoiceExerciseContainer);
        if (multipleChoiceExerciseContainer.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new multipleChoiceExerciseContainer cannot already have an ID").body(null);
        }
        MultipleChoiceExerciseContainer result = multipleChoiceExerciseContainerRepository.save(multipleChoiceExerciseContainer);
        return ResponseEntity.created(new URI("/api/multipleChoiceExerciseContainers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("multipleChoiceExerciseContainer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /multipleChoiceExerciseContainers -> Updates an existing multipleChoiceExerciseContainer.
     */
    @RequestMapping(value = "/multipleChoiceExerciseContainers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MultipleChoiceExerciseContainer> updateMultipleChoiceExerciseContainer(@Valid @RequestBody MultipleChoiceExerciseContainer multipleChoiceExerciseContainer) throws URISyntaxException {
        log.debug("REST request to update MultipleChoiceExerciseContainer : {}", multipleChoiceExerciseContainer);
        if (multipleChoiceExerciseContainer.getId() == null) {
            return createMultipleChoiceExerciseContainer(multipleChoiceExerciseContainer);
        }
        MultipleChoiceExerciseContainer result = multipleChoiceExerciseContainerRepository.save(multipleChoiceExerciseContainer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("multipleChoiceExerciseContainer", multipleChoiceExerciseContainer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /multipleChoiceExerciseContainers -> get all the multipleChoiceExerciseContainers.
     */
    @RequestMapping(value = "/multipleChoiceExerciseContainers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MultipleChoiceExerciseContainer> getAllMultipleChoiceExerciseContainers() {
        log.debug("REST request to get all MultipleChoiceExerciseContainers");
        return multipleChoiceExerciseContainerRepository.findAll();
    }

    /**
     * GET  /multipleChoiceExerciseContainers/:id -> get the "id" multipleChoiceExerciseContainer.
     */
    @RequestMapping(value = "/multipleChoiceExerciseContainers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MultipleChoiceExerciseContainer> getMultipleChoiceExerciseContainer(@PathVariable Long id) {
        log.debug("REST request to get MultipleChoiceExerciseContainer : {}", id);
        return Optional.ofNullable(multipleChoiceExerciseContainerRepository.findOne(id))
            .map(multipleChoiceExerciseContainer -> new ResponseEntity<>(
                multipleChoiceExerciseContainer,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /multipleChoiceExerciseContainers/:id -> delete the "id" multipleChoiceExerciseContainer.
     */
    @RequestMapping(value = "/multipleChoiceExerciseContainers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMultipleChoiceExerciseContainer(@PathVariable Long id) {
        log.debug("REST request to delete MultipleChoiceExerciseContainer : {}", id);
        multipleChoiceExerciseContainerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("multipleChoiceExerciseContainer", id.toString())).build();
    }
}
