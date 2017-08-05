package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.PhotoLocationBeacon;
import com.ciis.buenojo.repository.PhotoLocationBeaconRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing PhotoLocationBeacon.
 */
@RestController
@RequestMapping("/api")
public class PhotoLocationBeaconResource {

    private final Logger log = LoggerFactory.getLogger(PhotoLocationBeaconResource.class);

    @Inject
    private PhotoLocationBeaconRepository photoLocationBeaconRepository;

    /**
     * POST  /photoLocationBeacons -> Create a new photoLocationBeacon.
     */
    @RequestMapping(value = "/photoLocationBeacons",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoLocationBeacon> createPhotoLocationBeacon(@Valid @RequestBody PhotoLocationBeacon photoLocationBeacon) throws URISyntaxException {
        log.debug("REST request to save PhotoLocationBeacon : {}", photoLocationBeacon);
        if (photoLocationBeacon.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new photoLocationBeacon cannot already have an ID").body(null);
        }
        PhotoLocationBeacon result = photoLocationBeaconRepository.save(photoLocationBeacon);
        return ResponseEntity.created(new URI("/api/photoLocationBeacons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("photoLocationBeacon", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /photoLocationBeacons -> Updates an existing photoLocationBeacon.
     */
    @RequestMapping(value = "/photoLocationBeacons",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoLocationBeacon> updatePhotoLocationBeacon(@Valid @RequestBody PhotoLocationBeacon photoLocationBeacon) throws URISyntaxException {
        log.debug("REST request to update PhotoLocationBeacon : {}", photoLocationBeacon);
        if (photoLocationBeacon.getId() == null) {
            return createPhotoLocationBeacon(photoLocationBeacon);
        }
        PhotoLocationBeacon result = photoLocationBeaconRepository.save(photoLocationBeacon);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("photoLocationBeacon", photoLocationBeacon.getId().toString()))
            .body(result);
    }

    /**
     * GET  /photoLocationBeacons -> get all the photoLocationBeacons.
     */
    @RequestMapping(value = "/photoLocationBeacons",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PhotoLocationBeacon> getAllPhotoLocationBeacons(@RequestParam(required = false) String filter) {
        if ("exercise-is-null".equals(filter)) {
            log.debug("REST request to get all PhotoLocationBeacons where exercise is null");
            return StreamSupport
                .stream(photoLocationBeaconRepository.findAll().spliterator(), false)
                .filter(photoLocationBeacon -> photoLocationBeacon.getExercise() == null)
                .collect(Collectors.toList());
        }

        log.debug("REST request to get all PhotoLocationBeacons");
        return photoLocationBeaconRepository.findAll();
    }

    /**
     * GET  /photoLocationBeacons/:id -> get the "id" photoLocationBeacon.
     */
    @RequestMapping(value = "/photoLocationBeacons/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoLocationBeacon> getPhotoLocationBeacon(@PathVariable Long id) {
        log.debug("REST request to get PhotoLocationBeacon : {}", id);
        return Optional.ofNullable(photoLocationBeaconRepository.findOne(id))
            .map(photoLocationBeacon -> new ResponseEntity<>(
                photoLocationBeacon,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /photoLocationBeacons/:id -> delete the "id" photoLocationBeacon.
     */
    @RequestMapping(value = "/photoLocationBeacons/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePhotoLocationBeacon(@PathVariable Long id) {
        log.debug("REST request to delete PhotoLocationBeacon : {}", id);
        photoLocationBeaconRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("photoLocationBeacon", id.toString())).build();
    }
}
