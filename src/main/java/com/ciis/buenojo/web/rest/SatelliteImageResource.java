package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.SatelliteImage;
import com.ciis.buenojo.domain.factories.SatelliteImageFactory;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;
import com.ciis.buenojo.exceptions.BuenOjoFileException;
import com.ciis.buenojo.exceptions.BuenOjoInconsistencyException;
import com.ciis.buenojo.exceptions.InvalidSatelliteImageType;
import com.ciis.buenojo.repository.SatelliteImageRepository;
import com.ciis.buenojo.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing SatelliteImage.
 */
@RestController
@RequestMapping("/api")
public class SatelliteImageResource {

    private final Logger log = LoggerFactory.getLogger(SatelliteImageResource.class);

    @Inject
    private SatelliteImageRepository satelliteImageRepository;

    @Inject
    private SatelliteImageFactory satelliteImageFactory;

    /**
     * POST  /satelliteImages -> Create a new satelliteImage.
     */
    @RequestMapping(value = "/satelliteImages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SatelliteImage> createSatelliteImage(@Valid @RequestBody SatelliteImage satelliteImage) throws URISyntaxException {
        log.debug("REST request to save SatelliteImage : {}", satelliteImage);
        if (satelliteImage.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new satelliteImage cannot already have an ID").body(null);
        }
        SatelliteImage result = satelliteImageRepository.save(satelliteImage);
        return ResponseEntity.created(new URI("/api/satelliteImages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("satelliteImage", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /satelliteImages -> Updates an existing satelliteImage.
     */
    @RequestMapping(value = "/satelliteImages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SatelliteImage> updateSatelliteImage(@Valid @RequestBody SatelliteImage satelliteImage) throws URISyntaxException {
        log.debug("REST request to update SatelliteImage : {}", satelliteImage);
        if (satelliteImage.getId() == null) {
            return createSatelliteImage(satelliteImage);
        }
        SatelliteImage result = satelliteImageRepository.save(satelliteImage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("satelliteImage", satelliteImage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /satelliteImages -> get all the satelliteImages.
     */
    @RequestMapping(value = "/satelliteImages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SatelliteImage> getAllSatelliteImages() {
        log.debug("REST request to get all SatelliteImages");
        return satelliteImageRepository.findAll();
    }

    /**
     * GET  /satelliteImages/:id -> get the "id" satelliteImage.
     */
    @RequestMapping(value = "/satelliteImages/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SatelliteImage> getSatelliteImage(@PathVariable Long id) {
        log.debug("REST request to get SatelliteImage : {}", id);
        return Optional.ofNullable(satelliteImageRepository.findOne(id))
            .map(satelliteImage -> new ResponseEntity<>(
                satelliteImage,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /satelliteImages/:id -> delete the "id" satelliteImage.
     */
    @RequestMapping(value = "/satelliteImages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSatelliteImage(@PathVariable Long id) {
        log.debug("REST request to delete SatelliteImage : {}", id);
        satelliteImageRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("satelliteImage", id.toString())).build();
    }

    @RequestMapping(value= "/satelliteImages/upload",
    		method = RequestMethod.POST,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SatelliteImage> uploadSatelliteImage(@RequestParam("imageFile")MultipartFile imageFile, @RequestParam("metadata") MultipartFile csvMetadataFile) throws URISyntaxException, InvalidSatelliteImageType, IOException, BuenOjoFileException, BuenOjoInconsistencyException{
    	if (imageFile == null)
    		return ResponseEntity.badRequest().headers(HeaderUtil.createBadRequestHeaderAlert("Image file parameter missing")).body(null);
    	if (csvMetadataFile == null)
    		return ResponseEntity.badRequest().headers(HeaderUtil.createBadRequestHeaderAlert("CSV file parameter missing")).body(null);



    	SatelliteImage satelliteImage;

    	try {
    		satelliteImage = satelliteImageFactory.imageFromFile(imageFile, csvMetadataFile);
    	} catch (BuenOjoCSVParserException b){
    		return ResponseEntity.badRequest().headers(HeaderUtil.createBadRequestHeaderAlert("CSV file format incorrect")).body(null);
    	}
    	return createSatelliteImage(satelliteImage);
    }

}
