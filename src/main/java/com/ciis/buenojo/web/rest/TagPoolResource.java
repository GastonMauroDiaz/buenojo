package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.TagPool;
import com.ciis.buenojo.repository.TagPoolRepository;
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
 * REST controller for managing TagPool.
 */
@RestController
@RequestMapping("/api")
public class TagPoolResource {

    private final Logger log = LoggerFactory.getLogger(TagPoolResource.class);

    @Inject
    private TagPoolRepository tagPoolRepository;

    /**
     * POST  /tagPools -> Create a new tagPool.
     */
    @RequestMapping(value = "/tagPools",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TagPool> createTagPool(@Valid @RequestBody TagPool tagPool) throws URISyntaxException {
        log.debug("REST request to save TagPool : {}", tagPool);
        if (tagPool.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new tagPool cannot already have an ID").body(null);
        }
        TagPool result = tagPoolRepository.save(tagPool);
        return ResponseEntity.created(new URI("/api/tagPools/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tagPool", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tagPools -> Updates an existing tagPool.
     */
    @RequestMapping(value = "/tagPools",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TagPool> updateTagPool(@Valid @RequestBody TagPool tagPool) throws URISyntaxException {
        log.debug("REST request to update TagPool : {}", tagPool);
        if (tagPool.getId() == null) {
            return createTagPool(tagPool);
        }
        TagPool result = tagPoolRepository.save(tagPool);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tagPool", tagPool.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tagPools -> get all the tagPools.
     */
    @RequestMapping(value = "/tagPools",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TagPool> getAllTagPools() {
        log.debug("REST request to get all TagPools");
        return tagPoolRepository.findAll();
    }

    /**
     * GET  /tagPools/:id -> get the "id" tagPool.
     */
    @RequestMapping(value = "/tagPools/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TagPool> getTagPool(@PathVariable Long id) {
        log.debug("REST request to get TagPool : {}", id);
        return Optional.ofNullable(tagPoolRepository.findOne(id))
            .map(tagPool -> new ResponseEntity<>(
                tagPool,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tagPools/:id -> delete the "id" tagPool.
     */
    @RequestMapping(value = "/tagPools/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTagPool(@PathVariable Long id) {
        log.debug("REST request to delete TagPool : {}", id);
        tagPoolRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tagPool", id.toString())).build();
    }
}
