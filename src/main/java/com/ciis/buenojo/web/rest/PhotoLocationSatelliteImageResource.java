package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.PhotoLocationSatelliteImage;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;
import com.ciis.buenojo.exceptions.BuenOjoInconsistencyException;

import com.ciis.buenojo.repository.PhotoLocationSatelliteImageRepository;
import com.ciis.buenojo.service.PhotoLocationAnnotatedResourceFactory;
import com.ciis.buenojo.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * REST controller for managing PhotoLocationSatelliteImage.
 */
@RestController
@RequestMapping("/api")
public class PhotoLocationSatelliteImageResource {

    private final Logger log = LoggerFactory.getLogger(PhotoLocationSatelliteImageResource.class);

    @Inject
    private PhotoLocationSatelliteImageRepository photoLocationSatelliteImageRepository;
    
    @Inject 
    private PhotoLocationAnnotatedResourceFactory annotatedResourceFactory;
    /**
     * POST  /photoLocationSatelliteImages -> Create a new photoLocationSatelliteImage.
     */
    @RequestMapping(value = "/photoLocationSatelliteImages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoLocationSatelliteImage> createPhotoLocationSatelliteImage(@RequestBody PhotoLocationSatelliteImage photoLocationSatelliteImage) throws URISyntaxException {
        log.debug("REST request to save PhotoLocationSatelliteImage : {}", photoLocationSatelliteImage);
        if (photoLocationSatelliteImage.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new photoLocationSatelliteImage cannot already have an ID").body(null);
        }
        PhotoLocationSatelliteImage result = photoLocationSatelliteImageRepository.save(photoLocationSatelliteImage);
        return ResponseEntity.created(new URI("/api/photoLocationSatelliteImages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("photoLocationSatelliteImage", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /photoLocationSatelliteImages -> Updates an existing photoLocationSatelliteImage.
     */
    @RequestMapping(value = "/photoLocationSatelliteImages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoLocationSatelliteImage> updatePhotoLocationSatelliteImage(@RequestBody PhotoLocationSatelliteImage photoLocationSatelliteImage) throws URISyntaxException {
        log.debug("REST request to update PhotoLocationSatelliteImage : {}", photoLocationSatelliteImage);
        if (photoLocationSatelliteImage.getId() == null) {
            return createPhotoLocationSatelliteImage(photoLocationSatelliteImage);
        }
        PhotoLocationSatelliteImage result = photoLocationSatelliteImageRepository.save(photoLocationSatelliteImage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("photoLocationSatelliteImage", photoLocationSatelliteImage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /photoLocationSatelliteImages -> get all the photoLocationSatelliteImages.
     */
    @RequestMapping(value = "/photoLocationSatelliteImages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PhotoLocationSatelliteImage> getAllPhotoLocationSatelliteImages() {
        log.debug("REST request to get all PhotoLocationSatelliteImages");
        return photoLocationSatelliteImageRepository.findAllWithEagerRelationships();
    }

    /**
     * GET  /photoLocationSatelliteImages/:id -> get the "id" photoLocationSatelliteImage.
     */
    @RequestMapping(value = "/photoLocationSatelliteImages/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoLocationSatelliteImage> getPhotoLocationSatelliteImage(@PathVariable Long id) {
        log.debug("REST request to get PhotoLocationSatelliteImage : {}", id);
        return Optional.ofNullable(photoLocationSatelliteImageRepository.findOneWithEagerRelationships(id))
            .map(photoLocationSatelliteImage -> new ResponseEntity<>(
                photoLocationSatelliteImage,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /photoLocationSatelliteImages/:id -> delete the "id" photoLocationSatelliteImage.
     */
    @RequestMapping(value = "/photoLocationSatelliteImages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePhotoLocationSatelliteImage(@PathVariable Long id) {
        log.debug("REST request to delete PhotoLocationSatelliteImage : {}", id);
        photoLocationSatelliteImageRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("photoLocationSatelliteImage", id.toString())).build();
    }
    
    /**
     * GET  /photoLocationImages -> get all the photoLocationExtraImages.
     * @throws BuenOjoInconsistencyException 
     * @throws BuenOjoCSVParserException 
     */
    @RequestMapping(value = "/photoLocationSatelliteImages/extra/exercise/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PhotoLocationSatelliteImage> getPhotoLocationExtraSatelliteImagesForExercise(@PathVariable Long id) throws BuenOjoCSVParserException, BuenOjoInconsistencyException {
        log.debug("REST request to get all PhotoLocationExtraImages");
        
        return annotatedResourceFactory.getExtraSatelliteImagesForExercise(id);
    }
    
    
}
