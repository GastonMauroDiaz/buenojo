package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.Course;
import com.ciis.buenojo.domain.PhotoLocationExtraImage;
import com.ciis.buenojo.domain.PhotoLocationImage;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;
import com.ciis.buenojo.exceptions.BuenOjoInconsistencyException;
import com.ciis.buenojo.repository.CourseRepository;
import com.ciis.buenojo.repository.PhotoLocationExtraImageRepository;
import com.ciis.buenojo.service.PhotoLocationAnnotatedResourceFactory;
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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing PhotoLocationExtraImage.
 */
@RestController
@RequestMapping("/api")
public class PhotoLocationExtraImageResource {

    private final Logger log = LoggerFactory.getLogger(PhotoLocationExtraImageResource.class);

    @Inject
    private PhotoLocationExtraImageRepository photoLocationExtraImageRepository;
    
    @Inject
    private PhotoLocationAnnotatedResourceFactory annotatedResourceFactory;
    @Inject
    private CourseRepository CourseRepository;
    /**
     * POST  /photoLocationExtraImages -> Create a new photoLocationExtraImage.
     */
    @RequestMapping(value = "/photoLocationExtraImages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoLocationExtraImage> createPhotoLocationExtraImage(@RequestBody PhotoLocationExtraImage photoLocationExtraImage) throws URISyntaxException {
        log.debug("REST request to save PhotoLocationExtraImage : {}", photoLocationExtraImage);
        if (photoLocationExtraImage.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new photoLocationExtraImage cannot already have an ID").body(null);
        }
        PhotoLocationExtraImage result = photoLocationExtraImageRepository.save(photoLocationExtraImage);
        return ResponseEntity.created(new URI("/api/photoLocationExtraImages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("photoLocationExtraImage", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /photoLocationExtraImages -> Updates an existing photoLocationExtraImage.
     */
    @RequestMapping(value = "/photoLocationExtraImages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoLocationExtraImage> updatePhotoLocationExtraImage(@RequestBody PhotoLocationExtraImage photoLocationExtraImage) throws URISyntaxException {
        log.debug("REST request to update PhotoLocationExtraImage : {}", photoLocationExtraImage);
        if (photoLocationExtraImage.getId() == null) {
            return createPhotoLocationExtraImage(photoLocationExtraImage);
        }
        PhotoLocationExtraImage result = photoLocationExtraImageRepository.save(photoLocationExtraImage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("photoLocationExtraImage", photoLocationExtraImage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /photoLocationExtraImages -> get all the photoLocationExtraImages.
     */
    @RequestMapping(value = "/photoLocationExtraImages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PhotoLocationExtraImage> getAllPhotoLocationExtraImages() {
        log.debug("REST request to get all PhotoLocationExtraImages");
        return photoLocationExtraImageRepository.findAll();
    }

    /**
     * GET  /photoLocationExtraImages/:id -> get the "id" photoLocationExtraImage.
     */
    @RequestMapping(value = "/photoLocationExtraImages/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoLocationExtraImage> getPhotoLocationExtraImage(@PathVariable Long id) {
        log.debug("REST request to get PhotoLocationExtraImage : {}", id);
        return Optional.ofNullable(photoLocationExtraImageRepository.findOne(id))
            .map(photoLocationExtraImage -> new ResponseEntity<>(
                photoLocationExtraImage,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /photoLocationExtraImages/:id -> delete the "id" photoLocationExtraImage.
     */
    @RequestMapping(value = "/photoLocationExtraImages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePhotoLocationExtraImage(@PathVariable Long id) {
        log.debug("REST request to delete PhotoLocationExtraImage : {}", id);
        photoLocationExtraImageRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("photoLocationExtraImage", id.toString())).build();
    }
    
    /**
     * POST  /photoLocationExtraImages -> Create a new photoLocationExtraImage.
     * @throws BuenOjoCSVParserException 
     * @throws IOException 
     */
    @RequestMapping(value = "/photoLocationExtraImages/upload/{id}",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> createImagesFromCSVForCourse (@PathVariable Long id, @RequestParam("file") MultipartFile csvMetadataFile) throws URISyntaxException, IOException, BuenOjoCSVParserException {
        log.debug("REST request to save PhotoLocationExtraImages from CSV");
       
        if (csvMetadataFile == null)
    		return ResponseEntity.badRequest().headers(HeaderUtil.createBadRequestHeaderAlert("CSV file parameter missing")).body(null);
        
        Course course = CourseRepository.findOne(id);
        
        if (course == null) {
        	return ResponseEntity.badRequest().headers(HeaderUtil.createBadRequestHeaderAlert("No course with ID "+id)).body(null);
        }
        
        Set<PhotoLocationImage> images  = annotatedResourceFactory.importImageKeywordsFromCSV(csvMetadataFile);
        
        ArrayList<PhotoLocationExtraImage> result = new ArrayList<>(images.size());
        
        for (PhotoLocationImage photoLocationImage : images) {
			
        	PhotoLocationExtraImage extraImage = new PhotoLocationExtraImage();
        	extraImage.setCourse(course);

        	extraImage.setImage(photoLocationImage);
        	result.add(extraImage);
        	
		}
        
        photoLocationExtraImageRepository.save(result);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntitiesCreationAlert("imágenes extra creadas:", new Integer(result.size()).toString())).build();
    }
    
    

}
