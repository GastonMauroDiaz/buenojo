package com.ciis.buenojo.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

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

import com.ciis.buenojo.domain.HangManExerciseImageResource;
import com.ciis.buenojo.repository.HangManExerciseImageResourceRepository;
import com.ciis.buenojo.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing HangManExerciseImageResource.
 */
@RestController
@RequestMapping("/api")
public class HangManExerciseImageResourceResource {

    private final Logger log = LoggerFactory.getLogger(HangManExerciseImageResourceResource.class);

    @Inject
    private HangManExerciseImageResourceRepository hangManExerciseImageResourceRepository;

    /**
     * POST  /hangManExerciseImageResources -> Create a new hangManExerciseImageResource.
     */
    @RequestMapping(value = "/hangManExerciseImageResources",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HangManExerciseImageResource> createHangManExerciseImageResource(@RequestBody HangManExerciseImageResource hangManExerciseImageResource) throws URISyntaxException {
        log.debug("REST request to save HangManExerciseImageResource : {}", hangManExerciseImageResource);
        if (hangManExerciseImageResource.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new hangManExerciseImageResource cannot already have an ID").body(null);
        }
        HangManExerciseImageResource result = hangManExerciseImageResourceRepository.save(hangManExerciseImageResource);
        return ResponseEntity.created(new URI("/api/hangManExerciseImageResources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("hangManExerciseImageResource", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hangManExerciseImageResources -> Updates an existing hangManExerciseImageResource.
     */
    @RequestMapping(value = "/hangManExerciseImageResources",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HangManExerciseImageResource> updateHangManExerciseImageResource(@RequestBody HangManExerciseImageResource hangManExerciseImageResource) throws URISyntaxException {
        log.debug("REST request to update HangManExerciseImageResource : {}", hangManExerciseImageResource);
        if (hangManExerciseImageResource.getId() == null) {
            return createHangManExerciseImageResource(hangManExerciseImageResource);
        }
        HangManExerciseImageResource result = hangManExerciseImageResourceRepository.save(hangManExerciseImageResource);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("hangManExerciseImageResource", hangManExerciseImageResource.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hangManExerciseImageResources -> get all the hangManExerciseImageResources.
     */
    @RequestMapping(value = "/hangManExerciseImageResources",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<HangManExerciseImageResource> getAllHangManExerciseImageResources(@RequestParam(required = false) String filter) {
        if ("imageresource-is-null".equals(filter)) {
            log.debug("REST request to get all HangManExerciseImageResources where imageResource is null");
            return StreamSupport
                .stream(hangManExerciseImageResourceRepository.findAll().spliterator(), false)
                .filter(hangManExerciseImageResource -> hangManExerciseImageResource.getImageResource() == null)
                .collect(Collectors.toList());
        }

        log.debug("REST request to get all HangManExerciseImageResources");
        return hangManExerciseImageResourceRepository.findAll();
    }

    /**
     * GET  /hangManExerciseImageResources/:id -> get the "id" hangManExerciseImageResource.
     */
    @RequestMapping(value = "/hangManExerciseImageResources/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HangManExerciseImageResource> getHangManExerciseImageResource(@PathVariable Long id) {
        log.debug("REST request to get HangManExerciseImageResource : {}", id);
        return Optional.ofNullable(hangManExerciseImageResourceRepository.findOne(id))
            .map(hangManExerciseImageResource -> new ResponseEntity<>(
                hangManExerciseImageResource,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /hangManExerciseImageResources/:id -> delete the "id" hangManExerciseImageResource.
     */
    @RequestMapping(value = "/hangManExerciseImageResources/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHangManExerciseImageResource(@PathVariable Long id) {
        log.debug("REST request to delete HangManExerciseImageResource : {}", id);
        hangManExerciseImageResourceRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hangManExerciseImageResource", id.toString())).build();
    }


    /**
     * GET  /hangManExercises -> get all the hangManExercises.
     */
    @RequestMapping(value = "/hangManExercisesImageResources/container/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Object> getAllHangManExercisesImageResourcesByContainer(@PathVariable Long id) {
        log.debug("REST request to get all HangManExercises");
        return hangManExerciseImageResourceRepository.findByContainer(id);
        
    }
    

}
