package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.TagPair;
import com.ciis.buenojo.repository.TagPairRepository;
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
 * REST controller for managing TagPair.
 */
@RestController
@RequestMapping("/api")
public class TagPairResource {

    private final Logger log = LoggerFactory.getLogger(TagPairResource.class);

    @Inject
    private TagPairRepository tagPairRepository;

    /**
     * POST  /tagPairs -> Create a new tagPair.
     */
    @RequestMapping(value = "/tagPairs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TagPair> createTagPair(@Valid @RequestBody TagPair tagPair) throws URISyntaxException {
        log.debug("REST request to save TagPair : {}", tagPair);
        if (tagPair.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new tagPair cannot already have an ID").body(null);
        }
        TagPair result = tagPairRepository.save(tagPair);
        return ResponseEntity.created(new URI("/api/tagPairs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tagPair", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tagPairs -> Updates an existing tagPair.
     */
    @RequestMapping(value = "/tagPairs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TagPair> updateTagPair(@Valid @RequestBody TagPair tagPair) throws URISyntaxException {
        log.debug("REST request to update TagPair : {}", tagPair);
        if (tagPair.getId() == null) {
            return createTagPair(tagPair);
        }
        TagPair result = tagPairRepository.save(tagPair);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tagPair", tagPair.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tagPairs -> get all the tagPairs.
     */
    @RequestMapping(value = "/tagPairs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TagPair> getAllTagPairs() {
        log.debug("REST request to get all TagPairs");
        return tagPairRepository.findAll();
    }

    /**
     * GET  /tagPairs/:id -> get the "id" tagPair.
     */
    @RequestMapping(value = "/tagPairs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TagPair> getTagPair(@PathVariable Long id) {
        log.debug("REST request to get TagPair : {}", id);
        return Optional.ofNullable(tagPairRepository.findOne(id))
            .map(tagPair -> new ResponseEntity<>(
                tagPair,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tagPairs/:id -> delete the "id" tagPair.
     */
    @RequestMapping(value = "/tagPairs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTagPair(@PathVariable Long id) {
        log.debug("REST request to delete TagPair : {}", id);
        tagPairRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tagPair", id.toString())).build();
    }
}
