package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.MultipleChoiceSubject;
import com.ciis.buenojo.repository.MultipleChoiceSubjectRepository;
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
 * REST controller for managing MultipleChoiceSubject.
 */
@RestController
@RequestMapping("/api")
public class MultipleChoiceSubjectResource {

    private final Logger log = LoggerFactory.getLogger(MultipleChoiceSubjectResource.class);

    @Inject
    private MultipleChoiceSubjectRepository multipleChoiceSubjectRepository;

    /**
     * POST  /multipleChoiceSubjects -> Create a new multipleChoiceSubject.
     */
    @RequestMapping(value = "/multipleChoiceSubjects",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MultipleChoiceSubject> createMultipleChoiceSubject(@Valid @RequestBody MultipleChoiceSubject multipleChoiceSubject) throws URISyntaxException {
        log.debug("REST request to save MultipleChoiceSubject : {}", multipleChoiceSubject);
        if (multipleChoiceSubject.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new multipleChoiceSubject cannot already have an ID").body(null);
        }
        MultipleChoiceSubject result = multipleChoiceSubjectRepository.save(multipleChoiceSubject);
        return ResponseEntity.created(new URI("/api/multipleChoiceSubjects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("multipleChoiceSubject", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /multipleChoiceSubjects -> Updates an existing multipleChoiceSubject.
     */
    @RequestMapping(value = "/multipleChoiceSubjects",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MultipleChoiceSubject> updateMultipleChoiceSubject(@Valid @RequestBody MultipleChoiceSubject multipleChoiceSubject) throws URISyntaxException {
        log.debug("REST request to update MultipleChoiceSubject : {}", multipleChoiceSubject);
        if (multipleChoiceSubject.getId() == null) {
            return createMultipleChoiceSubject(multipleChoiceSubject);
        }
        MultipleChoiceSubject result = multipleChoiceSubjectRepository.save(multipleChoiceSubject);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("multipleChoiceSubject", multipleChoiceSubject.getId().toString()))
            .body(result);
    }

    /**
     * GET  /multipleChoiceSubjects -> get all the multipleChoiceSubjects.
     */
    @RequestMapping(value = "/multipleChoiceSubjects",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MultipleChoiceSubject> getAllMultipleChoiceSubjects() {
        log.debug("REST request to get all MultipleChoiceSubjects");
        return multipleChoiceSubjectRepository.findAll();
    }

    /**
     * GET  /multipleChoiceSubjects/:id -> get the "id" multipleChoiceSubject.
     */
    @RequestMapping(value = "/multipleChoiceSubjects/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MultipleChoiceSubject> getMultipleChoiceSubject(@PathVariable Long id) {
        log.debug("REST request to get MultipleChoiceSubject : {}", id);
        return Optional.ofNullable(multipleChoiceSubjectRepository.findOne(id))
            .map(multipleChoiceSubject -> new ResponseEntity<>(
                multipleChoiceSubject,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /multipleChoiceSubjects/:id -> delete the "id" multipleChoiceSubject.
     */
    @RequestMapping(value = "/multipleChoiceSubjects/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMultipleChoiceSubject(@PathVariable Long id) {
        log.debug("REST request to delete MultipleChoiceSubject : {}", id);
        multipleChoiceSubjectRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("multipleChoiceSubject", id.toString())).build();
    }
}
