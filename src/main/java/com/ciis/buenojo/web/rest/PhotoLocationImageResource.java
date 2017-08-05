package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.PhotoLocationImage;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;
import com.ciis.buenojo.exceptions.BuenOjoInconsistencyException;
import com.ciis.buenojo.repository.PhotoLocationImageRepository;
import com.ciis.buenojo.service.PhotoLocationAnnotatedResourceFactory;
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
 * REST controller for managing PhotoLocationImage.
 */
@RestController
@RequestMapping("/api")
public class PhotoLocationImageResource {

    private final Logger log = LoggerFactory.getLogger(PhotoLocationImageResource.class);

    @Inject
    private PhotoLocationImageRepository photoLocationImageRepository;

    @Inject
    private PhotoLocationAnnotatedResourceFactory annotatedResourceFactory;
    /**
     * POST  /photoLocationImages -> Create a new photoLocationImage.
     */
    @RequestMapping(value = "/photoLocationImages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoLocationImage> createPhotoLocationImage(@RequestBody PhotoLocationImage photoLocationImage) throws URISyntaxException {
        log.debug("REST request to save PhotoLocationImage : {}", photoLocationImage);
        if (photoLocationImage.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new photoLocationImage cannot already have an ID").body(null);
        }
        PhotoLocationImage result = photoLocationImageRepository.save(photoLocationImage);
        return ResponseEntity.created(new URI("/api/photoLocationImages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("photoLocationImage", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /photoLocationImages -> Updates an existing photoLocationImage.
     */
    @RequestMapping(value = "/photoLocationImages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoLocationImage> updatePhotoLocationImage(@RequestBody PhotoLocationImage photoLocationImage) throws URISyntaxException {
        log.debug("REST request to update PhotoLocationImage : {}", photoLocationImage);
        if (photoLocationImage.getId() == null) {
            return createPhotoLocationImage(photoLocationImage);
        }
        PhotoLocationImage result = photoLocationImageRepository.save(photoLocationImage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("photoLocationImage", photoLocationImage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /photoLocationImages -> get all the photoLocationImages.
     */
    @RequestMapping(value = "/photoLocationImages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PhotoLocationImage> getAllPhotoLocationImages() {
        log.debug("REST request to get all PhotoLocationImages");
        return photoLocationImageRepository.findAllWithEagerRelationships();
    }

    /**
     * GET  /photoLocationImages/:id -> get the "id" photoLocationImage.
     */
    @RequestMapping(value = "/photoLocationImages/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoLocationImage> getPhotoLocationImage(@PathVariable Long id) {
        log.debug("REST request to get PhotoLocationImage : {}", id);
        return Optional.ofNullable(photoLocationImageRepository.findOneWithEagerRelationships(id))
            .map(photoLocationImage -> new ResponseEntity<>(
                photoLocationImage,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /photoLocationImages/:id -> delete the "id" photoLocationImage.
     */
    @RequestMapping(value = "/photoLocationImages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePhotoLocationImage(@PathVariable Long id) {
        log.debug("REST request to delete PhotoLocationImage : {}", id);
        photoLocationImageRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("photoLocationImage", id.toString())).build();
    }
    
    /**
     * GET  /photoLocationImages -> get all the photoLocationExtraImages.
     * @throws BuenOjoInconsistencyException 
     * @throws BuenOjoCSVParserException 
     */
    @RequestMapping(value = "/photoLocationImages/extra/exercise/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PhotoLocationImage> getPhotoLocationExtraImagesForExercise(@PathVariable Long id) throws BuenOjoCSVParserException, BuenOjoInconsistencyException {
        log.debug("REST request to get all PhotoLocationExtraImages");
        return annotatedResourceFactory.getExtraImagesForExercise(id);
    }
}
