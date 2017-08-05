package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.MultipleChoiceAnswer;
import com.ciis.buenojo.repository.MultipleChoiceAnswerRepository;
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
 * REST controller for managing MultipleChoiceAnswer.
 */
@RestController
@RequestMapping("/api")
public class MultipleChoiceAnswerResource {

    private final Logger log = LoggerFactory.getLogger(MultipleChoiceAnswerResource.class);

    @Inject
    private MultipleChoiceAnswerRepository multipleChoiceAnswerRepository;

    /**
     * POST  /multipleChoiceAnswers -> Create a new multipleChoiceAnswer.
     */
    @RequestMapping(value = "/multipleChoiceAnswers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MultipleChoiceAnswer> createMultipleChoiceAnswer(@Valid @RequestBody MultipleChoiceAnswer multipleChoiceAnswer) throws URISyntaxException {
        log.debug("REST request to save MultipleChoiceAnswer : {}", multipleChoiceAnswer);
        if (multipleChoiceAnswer.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new multipleChoiceAnswer cannot already have an ID").body(null);
        }
        MultipleChoiceAnswer result = multipleChoiceAnswerRepository.save(multipleChoiceAnswer);
        return ResponseEntity.created(new URI("/api/multipleChoiceAnswers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("multipleChoiceAnswer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /multipleChoiceAnswers -> Updates an existing multipleChoiceAnswer.
     */
    @RequestMapping(value = "/multipleChoiceAnswers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MultipleChoiceAnswer> updateMultipleChoiceAnswer(@Valid @RequestBody MultipleChoiceAnswer multipleChoiceAnswer) throws URISyntaxException {
        log.debug("REST request to update MultipleChoiceAnswer : {}", multipleChoiceAnswer);
        if (multipleChoiceAnswer.getId() == null) {
            return createMultipleChoiceAnswer(multipleChoiceAnswer);
        }
        MultipleChoiceAnswer result = multipleChoiceAnswerRepository.save(multipleChoiceAnswer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("multipleChoiceAnswer", multipleChoiceAnswer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /multipleChoiceAnswers -> get all the multipleChoiceAnswers.
     */
    @RequestMapping(value = "/multipleChoiceAnswersByContainer/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MultipleChoiceAnswer> getAllMultipleChoiceAnswersByContainerId(@PathVariable Long id) {
        log.debug("REST request to get all MultipleChoiceAnswers by container Id: {}",id);
        return multipleChoiceAnswerRepository.findByContainerId(id);
    }

    /**
     * GET  /multipleChoiceAnswers -> get all the multipleChoiceAnswers.
     */
    @RequestMapping(value = "/multipleChoiceAnswers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MultipleChoiceAnswer> getAllMultipleChoiceAnswers() {
        log.debug("REST request to get all MultipleChoiceAnswers");
        return multipleChoiceAnswerRepository.findAll();
    }

 
    
    /**
     * GET  /multipleChoiceAnswers/:id -> get the "id" multipleChoiceAnswer.
     */
    @RequestMapping(value = "/multipleChoiceAnswers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MultipleChoiceAnswer> getMultipleChoiceAnswer(@PathVariable Long id) {
        log.debug("REST request to get MultipleChoiceAnswer : {}", id);
        return Optional.ofNullable(multipleChoiceAnswerRepository.findOne(id))
            .map(multipleChoiceAnswer -> new ResponseEntity<>(
                multipleChoiceAnswer,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /multipleChoiceAnswers/:id -> delete the "id" multipleChoiceAnswer.
     */
    @RequestMapping(value = "/multipleChoiceAnswers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMultipleChoiceAnswer(@PathVariable Long id) {
        log.debug("REST request to delete MultipleChoiceAnswer : {}", id);
        multipleChoiceAnswerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("multipleChoiceAnswer", id.toString())).build();
    }
}
