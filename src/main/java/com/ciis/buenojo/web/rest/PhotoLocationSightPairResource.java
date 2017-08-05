package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.PhotoLocationSightPair;
import com.ciis.buenojo.domain.parsers.PhotoLocationSightPairCSVParser;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;
import com.ciis.buenojo.exceptions.BuenOjoInconsistencyException;
import com.ciis.buenojo.repository.PhotoLocationSightPairRepository;
import com.ciis.buenojo.service.PhotoLocationSightPairFactory;
import com.ciis.buenojo.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.validation.Valid;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PhotoLocationSightPair.
 */
@RestController
@RequestMapping("/api")
public class PhotoLocationSightPairResource {

    private final Logger log = LoggerFactory.getLogger(PhotoLocationSightPairResource.class);

    @Inject
    private PhotoLocationSightPairRepository photoLocationSightPairRepository;
    
    @Inject
    private PhotoLocationSightPairFactory sightPairFactory;
    /**
     * POST  /photoLocationSightPairs -> Create a new photoLocationSightPair.
     */
    @RequestMapping(value = "/photoLocationSightPairs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoLocationSightPair> createPhotoLocationSightPair(@Valid @RequestBody PhotoLocationSightPair photoLocationSightPair) throws URISyntaxException {
        log.debug("REST request to save PhotoLocationSightPair : {}", photoLocationSightPair);
        if (photoLocationSightPair.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new photoLocationSightPair cannot already have an ID").body(null);
        }
        PhotoLocationSightPair result = photoLocationSightPairRepository.save(photoLocationSightPair);
        return ResponseEntity.created(new URI("/api/photoLocationSightPairs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("photoLocationSightPair", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /photoLocationSightPairs -> Updates an existing photoLocationSightPair.
     */
    @RequestMapping(value = "/photoLocationSightPairs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoLocationSightPair> updatePhotoLocationSightPair(@Valid @RequestBody PhotoLocationSightPair photoLocationSightPair) throws URISyntaxException {
        log.debug("REST request to update PhotoLocationSightPair : {}", photoLocationSightPair);
        if (photoLocationSightPair.getId() == null) {
            return createPhotoLocationSightPair(photoLocationSightPair);
        }
        PhotoLocationSightPair result = photoLocationSightPairRepository.save(photoLocationSightPair);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("photoLocationSightPair", photoLocationSightPair.getId().toString()))
            .body(result);
    }

    /**
     * GET  /photoLocationSightPairs -> get all the photoLocationSightPairs.
     */
    @RequestMapping(value = "/photoLocationSightPairs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PhotoLocationSightPair> getAllPhotoLocationSightPairs() {
        log.debug("REST request to get all PhotoLocationSightPairs");
        return photoLocationSightPairRepository.findAll();
    }

    /**
     * GET  /photoLocationSightPairs/:id -> get the "id" photoLocationSightPair.
     */
    @RequestMapping(value = "/photoLocationSightPairs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoLocationSightPair> getPhotoLocationSightPair(@PathVariable Long id) {
        log.debug("REST request to get PhotoLocationSightPair : {}", id);
        return Optional.ofNullable(photoLocationSightPairRepository.findOne(id))
            .map(photoLocationSightPair -> new ResponseEntity<>(
                photoLocationSightPair,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /photoLocationSightPairs/:id -> delete the "id" photoLocationSightPair.
     */
    @RequestMapping(value = "/photoLocationSightPairs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePhotoLocationSightPair(@PathVariable Long id) {
        log.debug("REST request to delete PhotoLocationSightPair : {}", id);
        photoLocationSightPairRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("photoLocationSightPair", id.toString())).build();
    }
    
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/photoLocationSightPairs/upload/{exerciseId}",
    				method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Void> upload(@RequestParam("file") MultipartFile file, @PathVariable Long exerciseId ) throws URISyntaxException, BuenOjoCSVParserException, BuenOjoInconsistencyException, IOException{
    	
    	List<PhotoLocationSightPair> sightPairs = sightPairFactory.sightPairsFromFileForExercise(file, exerciseId);
    	
    	return ResponseEntity.ok().headers(HeaderUtil.createEntitiesCreationAlert("Miras", new Integer(sightPairs.size()).toString())).build();
    	
    }
}
