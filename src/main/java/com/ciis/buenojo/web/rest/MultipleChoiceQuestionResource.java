package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.MultipleChoiceQuestion;
import com.ciis.buenojo.repository.MultipleChoiceQuestionRepository;
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
 * REST controller for managing MultipleChoiceQuestion.
 */
@RestController
@RequestMapping("/api")
public class MultipleChoiceQuestionResource {

    private final Logger log = LoggerFactory.getLogger(MultipleChoiceQuestionResource.class);

    @Inject
    private MultipleChoiceQuestionRepository multipleChoiceQuestionRepository;

    /**
     * POST  /multipleChoiceQuestions -> Create a new multipleChoiceQuestion.
     */
    @RequestMapping(value = "/multipleChoiceQuestions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MultipleChoiceQuestion> createMultipleChoiceQuestion(@Valid @RequestBody MultipleChoiceQuestion multipleChoiceQuestion) throws URISyntaxException {
        log.debug("REST request to save MultipleChoiceQuestion : {}", multipleChoiceQuestion);
        if (multipleChoiceQuestion.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new multipleChoiceQuestion cannot already have an ID").body(null);
        }
        MultipleChoiceQuestion result = multipleChoiceQuestionRepository.save(multipleChoiceQuestion);
        return ResponseEntity.created(new URI("/api/multipleChoiceQuestions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("multipleChoiceQuestion", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /multipleChoiceQuestions -> Updates an existing multipleChoiceQuestion.
     */
    @RequestMapping(value = "/multipleChoiceQuestions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MultipleChoiceQuestion> updateMultipleChoiceQuestion(@Valid @RequestBody MultipleChoiceQuestion multipleChoiceQuestion) throws URISyntaxException {
        log.debug("REST request to update MultipleChoiceQuestion : {}", multipleChoiceQuestion);
        if (multipleChoiceQuestion.getId() == null) {
            return createMultipleChoiceQuestion(multipleChoiceQuestion);
        }
        MultipleChoiceQuestion result = multipleChoiceQuestionRepository.save(multipleChoiceQuestion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("multipleChoiceQuestion", multipleChoiceQuestion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /multipleChoiceQuestions -> get all the multipleChoiceQuestions.
     */
    @RequestMapping(value = "/multipleChoiceQuestions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MultipleChoiceQuestion> getAllMultipleChoiceQuestions() {
        log.debug("REST request to get all MultipleChoiceQuestions");
        return multipleChoiceQuestionRepository.findAll();
    }

    /**
     * GET  /multipleChoiceQuestions -> get all the multipleChoiceQuestions.
     */
    @RequestMapping(value = "/multipleChoiceQuestionsByContainer/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MultipleChoiceQuestion> getAllMultipleChoiceQuestionsByContainer(@PathVariable Long id) {
        log.debug("REST request to get all MultipleChoiceQuestions By ContainerId "+ id.toString());
        return multipleChoiceQuestionRepository.findBymultipleChoiceExerciseContainerId(id);
    }


    
    /**
     * GET  /multipleChoiceQuestions/:id -> get the "id" multipleChoiceQuestion.
     */
    @RequestMapping(value = "/multipleChoiceQuestions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MultipleChoiceQuestion> getMultipleChoiceQuestion(@PathVariable Long id) {
        log.debug("REST request to get MultipleChoiceQuestion : {}", id);
        return Optional.ofNullable(multipleChoiceQuestionRepository.findOne(id))
            .map(multipleChoiceQuestion -> new ResponseEntity<>(
                multipleChoiceQuestion,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /multipleChoiceQuestions/:id -> delete the "id" multipleChoiceQuestion.
     */
    @RequestMapping(value = "/multipleChoiceQuestions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMultipleChoiceQuestion(@PathVariable Long id) {
        log.debug("REST request to delete MultipleChoiceQuestion : {}", id);
        multipleChoiceQuestionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("multipleChoiceQuestion", id.toString())).build();
    }
}
