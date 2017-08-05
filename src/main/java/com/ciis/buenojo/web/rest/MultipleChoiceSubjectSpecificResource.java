package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.MultipleChoiceSubjectSpecific;
import com.ciis.buenojo.repository.MultipleChoiceSubjectSpecificRepository;
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
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing MultipleChoiceSubjectSpecific.
 */
@RestController
@RequestMapping("/api")
public class MultipleChoiceSubjectSpecificResource {

    private final Logger log = LoggerFactory.getLogger(MultipleChoiceSubjectSpecificResource.class);

    @Inject
    private MultipleChoiceSubjectSpecificRepository multipleChoiceSubjectSpecificRepository;

    /**
     * POST  /multipleChoiceSubjectSpecifics -> Create a new multipleChoiceSubjectSpecific.
     */
    @RequestMapping(value = "/multipleChoiceSubjectSpecifics",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MultipleChoiceSubjectSpecific> createMultipleChoiceSubjectSpecific(@RequestBody MultipleChoiceSubjectSpecific multipleChoiceSubjectSpecific) throws URISyntaxException {
        log.debug("REST request to save MultipleChoiceSubjectSpecific : {}", multipleChoiceSubjectSpecific);
        if (multipleChoiceSubjectSpecific.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new multipleChoiceSubjectSpecific cannot already have an ID").body(null);
        }
        MultipleChoiceSubjectSpecific result = multipleChoiceSubjectSpecificRepository.save(multipleChoiceSubjectSpecific);
        return ResponseEntity.created(new URI("/api/multipleChoiceSubjectSpecifics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("multipleChoiceSubjectSpecific", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /multipleChoiceSubjectSpecifics -> Updates an existing multipleChoiceSubjectSpecific.
     */
    @RequestMapping(value = "/multipleChoiceSubjectSpecifics",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MultipleChoiceSubjectSpecific> updateMultipleChoiceSubjectSpecific(@RequestBody MultipleChoiceSubjectSpecific multipleChoiceSubjectSpecific) throws URISyntaxException {
        log.debug("REST request to update MultipleChoiceSubjectSpecific : {}", multipleChoiceSubjectSpecific);
        if (multipleChoiceSubjectSpecific.getId() == null) {
            return createMultipleChoiceSubjectSpecific(multipleChoiceSubjectSpecific);
        }
        MultipleChoiceSubjectSpecific result = multipleChoiceSubjectSpecificRepository.save(multipleChoiceSubjectSpecific);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("multipleChoiceSubjectSpecific", multipleChoiceSubjectSpecific.getId().toString()))
            .body(result);
    }

    /**
     * GET  /multipleChoiceSubjectSpecifics -> get all the multipleChoiceSubjectSpecifics.
     */
    @RequestMapping(value = "/multipleChoiceSubjectSpecifics",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MultipleChoiceSubjectSpecific> getAllMultipleChoiceSubjectSpecifics() {
        log.debug("REST request to get all MultipleChoiceSubjectSpecifics");
        return multipleChoiceSubjectSpecificRepository.findAll();
    }

    /**
     * GET  /multipleChoiceSubjectSpecifics/:id -> get the "id" multipleChoiceSubjectSpecific.
     */
    @RequestMapping(value = "/multipleChoiceSubjectSpecifics/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MultipleChoiceSubjectSpecific> getMultipleChoiceSubjectSpecific(@PathVariable Long id) {
        log.debug("REST request to get MultipleChoiceSubjectSpecific : {}", id);
        return Optional.ofNullable(multipleChoiceSubjectSpecificRepository.findOne(id))
            .map(multipleChoiceSubjectSpecific -> new ResponseEntity<>(
                multipleChoiceSubjectSpecific,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /multipleChoiceSubjectSpecifics/:id -> delete the "id" multipleChoiceSubjectSpecific.
     */
    @RequestMapping(value = "/multipleChoiceSubjectSpecifics/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMultipleChoiceSubjectSpecific(@PathVariable Long id) {
        log.debug("REST request to delete MultipleChoiceSubjectSpecific : {}", id);
        multipleChoiceSubjectSpecificRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("multipleChoiceSubjectSpecific", id.toString())).build();
    }
}
