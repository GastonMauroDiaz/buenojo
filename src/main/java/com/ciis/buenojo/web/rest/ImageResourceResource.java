package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.ImageResource;
import com.ciis.buenojo.exceptions.BuenOjoFileException;
import com.ciis.buenojo.exceptions.BuenOjoInconsistencyException;
import com.ciis.buenojo.repository.ImageResourceRepository;
import com.ciis.buenojo.service.ImageResourceService;
import com.ciis.buenojo.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ImageResource.
 */
@RestController
@RequestMapping("/api")
public class ImageResourceResource {

    private final Logger log = LoggerFactory.getLogger(ImageResourceResource.class);

    @Inject
    private ImageResourceRepository imageResourceRepository;
    
    @Inject 
    private ImageResourceService imageResourceService;
    /**
     * POST  /imageResources -> Create a new imageResource.
     * @throws IOException 
     * @throws BuenOjoFileException 
     * @throws BuenOjoInconsistencyException 
     */
    @RequestMapping(value = "/imageResources",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ImageResource> createImageResource(@Valid @RequestBody ImageResource imageResource) throws URISyntaxException, IOException, BuenOjoFileException, BuenOjoInconsistencyException {
        log.debug("REST request to save ImageResource : {}", imageResource);
        if (imageResource.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new imageResource cannot already have an ID").body(null);
        }
        
        ImageResource result = imageResourceService.createImageResource(imageResource);
        
        return ResponseEntity.created(new URI("/api/imageResources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("imageResource", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /imageResources -> Updates an existing imageResource.
     * @throws IOException 
     * @throws BuenOjoFileException 
     * @throws BuenOjoInconsistencyException 
     */
    @RequestMapping(value = "/imageResources",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ImageResource> updateImageResource(@Valid @RequestBody ImageResource imageResource) throws URISyntaxException, IOException, BuenOjoFileException, BuenOjoInconsistencyException {
        log.debug("REST request to update ImageResource : {}", imageResource);
        if (imageResource.getId() == null) {
            return createImageResource(imageResource);
        }
        ImageResource result =imageResourceService.updateImageResource(imageResource);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("imageResource", imageResource.getId().toString()))
            .body(result);
    }

    /**
     * GET  /imageResources -> get all the imageResources.
     */
    @RequestMapping(value = "/imageResources",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ImageResource> getAllImageResources() {
        log.debug("REST request to get all ImageResources");
        return imageResourceRepository.findAll();
    }

    /**
     * GET  /imageResources/:id -> get the "id" imageResource.
     */
    @RequestMapping(value = "/imageResources/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ImageResource> getImageResource(@PathVariable Long id) {
        log.debug("REST request to get ImageResource : {}", id);
        return Optional.ofNullable(imageResourceRepository.findOne(id))
            .map(imageResource -> new ResponseEntity<>(
                imageResource,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /imageResources/:id -> delete the "id" imageResource.
     * @throws IOException 
     */
    @RequestMapping(value = "/imageResources/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteImageResource(@PathVariable Long id) throws IOException {
        log.debug("REST request to delete ImageResource : {}", id);
        
        imageResourceService.deleteImageResource(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("imageResource", id.toString())).build();
    }
}
