package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.Course;
import com.ciis.buenojo.domain.PhotoLocationExtraImage;
import com.ciis.buenojo.domain.PhotoLocationExtraSatelliteImage;
import com.ciis.buenojo.domain.PhotoLocationSatelliteImage;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;
import com.ciis.buenojo.repository.CourseRepository;
import com.ciis.buenojo.repository.PhotoLocationExtraSatelliteImageRepository;
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
 * REST controller for managing PhotoLocationExtraSatelliteImage.
 */
@RestController
@RequestMapping("/api")
public class PhotoLocationExtraSatelliteImageResource {

    private final Logger log = LoggerFactory.getLogger(PhotoLocationExtraSatelliteImageResource.class);
    
    @Inject
    private CourseRepository courseRepository;
    
    @Inject 
    private PhotoLocationAnnotatedResourceFactory annotatedResourceFactory;
    

    @Inject
    private PhotoLocationExtraSatelliteImageRepository photoLocationExtraSatelliteImageRepository;

    
    /**
     * POST  /photoLocationExtraSatelliteImages -> Create a new photoLocationExtraSatelliteImage.
     */
    @RequestMapping(value = "/photoLocationExtraSatelliteImages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoLocationExtraSatelliteImage> createPhotoLocationExtraSatelliteImage(@RequestBody PhotoLocationExtraSatelliteImage photoLocationExtraSatelliteImage) throws URISyntaxException {
        log.debug("REST request to save PhotoLocationExtraSatelliteImage : {}", photoLocationExtraSatelliteImage);
        if (photoLocationExtraSatelliteImage.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new photoLocationExtraSatelliteImage cannot already have an ID").body(null);
        }
        PhotoLocationExtraSatelliteImage result = photoLocationExtraSatelliteImageRepository.save(photoLocationExtraSatelliteImage);
        return ResponseEntity.created(new URI("/api/photoLocationExtraSatelliteImages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("photoLocationExtraSatelliteImage", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /photoLocationExtraSatelliteImages -> Updates an existing photoLocationExtraSatelliteImage.
     */
    @RequestMapping(value = "/photoLocationExtraSatelliteImages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoLocationExtraSatelliteImage> updatePhotoLocationExtraSatelliteImage(@RequestBody PhotoLocationExtraSatelliteImage photoLocationExtraSatelliteImage) throws URISyntaxException {
        log.debug("REST request to update PhotoLocationExtraSatelliteImage : {}", photoLocationExtraSatelliteImage);
        if (photoLocationExtraSatelliteImage.getId() == null) {
            return createPhotoLocationExtraSatelliteImage(photoLocationExtraSatelliteImage);
        }
        PhotoLocationExtraSatelliteImage result = photoLocationExtraSatelliteImageRepository.save(photoLocationExtraSatelliteImage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("photoLocationExtraSatelliteImage", photoLocationExtraSatelliteImage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /photoLocationExtraSatelliteImages -> get all the photoLocationExtraSatelliteImages.
     */
    @RequestMapping(value = "/photoLocationExtraSatelliteImages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PhotoLocationExtraSatelliteImage> getAllPhotoLocationExtraSatelliteImages() {
        log.debug("REST request to get all PhotoLocationExtraSatelliteImages");
        return photoLocationExtraSatelliteImageRepository.findAll();
    }

    /**
     * GET  /photoLocationExtraSatelliteImages/:id -> get the "id" photoLocationExtraSatelliteImage.
     */
    @RequestMapping(value = "/photoLocationExtraSatelliteImages/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoLocationExtraSatelliteImage> getPhotoLocationExtraSatelliteImage(@PathVariable Long id) {
        log.debug("REST request to get PhotoLocationExtraSatelliteImage : {}", id);
        return Optional.ofNullable(photoLocationExtraSatelliteImageRepository.findOne(id))
            .map(photoLocationExtraSatelliteImage -> new ResponseEntity<>(
                photoLocationExtraSatelliteImage,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /photoLocationExtraSatelliteImages/:id -> delete the "id" photoLocationExtraSatelliteImage.
     */
    @RequestMapping(value = "/photoLocationExtraSatelliteImages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePhotoLocationExtraSatelliteImage(@PathVariable Long id) {
        log.debug("REST request to delete PhotoLocationExtraSatelliteImage : {}", id);
        photoLocationExtraSatelliteImageRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("photoLocationExtraSatelliteImage", id.toString())).build();
    }
    
    /**
     * POST  /photoLocationExtraSatelliteImages/upload/:id -> Create a new photoLocationExtraSatelliteImage.
     * @throws BuenOjoCSVParserException 
     * @throws IOException 
     */
    @RequestMapping(value = "/photoLocationExtraSatelliteImages/upload/{id}",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> createSatelliteImagesFromCSVForCourse (@PathVariable Long id, @RequestParam("file") MultipartFile csvMetadataFile) throws URISyntaxException, IOException, BuenOjoCSVParserException {
        log.debug("REST request to save PhotoLocationExtraImages from CSV");
       
        if (csvMetadataFile == null)
    		return ResponseEntity.badRequest().headers(HeaderUtil.createBadRequestHeaderAlert("CSV file parameter missing")).body(null);
        
        Course course = courseRepository.findOne(id);
        
        if (course == null) {
        	return ResponseEntity.badRequest().headers(HeaderUtil.createBadRequestHeaderAlert("No course with ID "+id)).body(null);
        }
        Set<PhotoLocationSatelliteImage> images  = annotatedResourceFactory.importSatelliteImageKeywordsFromCSV(csvMetadataFile);
        
        
        ArrayList<PhotoLocationExtraSatelliteImage> result = new ArrayList<>(images.size());
        
        for (PhotoLocationSatelliteImage photoLocationExtraSatelliteImage : images) {
			
        	PhotoLocationExtraSatelliteImage extraImage = new PhotoLocationExtraSatelliteImage();
        	extraImage.setCourse(course);

        	extraImage.setImage(photoLocationExtraSatelliteImage);
        	result.add(extraImage);
        	
		}
        photoLocationExtraSatelliteImageRepository.save(result);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntitiesCreationAlert("imágenes extra creadas:", new Integer(result.size()).toString())).build();
    }
    
    /**
     * GET  /photoLocationExtraSatelliteImages -> get all the photoLocationExtraSatelliteImages.
     */
    @RequestMapping(value = "/photoLocationExtraSatelliteImages/exercise/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PhotoLocationExtraSatelliteImage> getPhotoLocationExtraSatelliteImagesForExercise(@PathVariable Long id) {
        log.debug("REST request to get PhotoLocationExtraSatelliteImages for exercise id:"+id);
        return photoLocationExtraSatelliteImageRepository.findAll();
    }
}
