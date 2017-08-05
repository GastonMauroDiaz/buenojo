package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.Level;
import com.ciis.buenojo.repository.LevelRepository;
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
 * REST controller for managing Level.
 */
@RestController
@RequestMapping("/api")
public class LevelResource {

    private final Logger log = LoggerFactory.getLogger(LevelResource.class);

    @Inject
    private LevelRepository levelRepository;

    /**
     * POST  /levels -> Create a new level.
     */
    @RequestMapping(value = "/levels",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Level> createLevel(@Valid @RequestBody Level level) throws URISyntaxException {
        log.debug("REST request to save Level : {}", level);
        if (level.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new level cannot already have an ID").body(null);
        }
        Level result = levelRepository.save(level);
        return ResponseEntity.created(new URI("/api/levels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("level", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /levels -> Updates an existing level.
     */
    @RequestMapping(value = "/levels",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Level> updateLevel(@Valid @RequestBody Level level) throws URISyntaxException {
        log.debug("REST request to update Level : {}", level);
        if (level.getId() == null) {
            return createLevel(level);
        }
        Level result = levelRepository.save(level);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("level", level.getId().toString()))
            .body(result);
    }

    /**
     * GET  /levels -> get all the levels.
     */
    @RequestMapping(value = "/levels",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Level> getAllLevels() {
        log.debug("REST request to get all Levels");
        return levelRepository.findAll();
    }

    /**
     * GET  /levels/:id -> get the "id" level.
     */
    @RequestMapping(value = "/levels/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Level> getLevel(@PathVariable Long id) {
        log.debug("REST request to get Level : {}", id);
        return Optional.ofNullable(levelRepository.findOneWithEagerRelationships(id))
            .map(level -> new ResponseEntity<>(
                level,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /levels/:id -> delete the "id" level.
     */
    @RequestMapping(value = "/levels/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLevel(@PathVariable Long id) {
        log.debug("REST request to delete Level : {}", id);
        levelRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("level", id.toString())).build();
    }
}
