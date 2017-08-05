package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.PhotoLocationKeyword;
import com.ciis.buenojo.repository.PhotoLocationKeywordRepository;
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
 * REST controller for managing PhotoLocationKeyword.
 */
@RestController
@RequestMapping("/api")
public class PhotoLocationKeywordResource {

    private final Logger log = LoggerFactory.getLogger(PhotoLocationKeywordResource.class);

    @Inject
    private PhotoLocationKeywordRepository photoLocationKeywordRepository;

    /**
     * POST  /photoLocationKeywords -> Create a new photoLocationKeyword.
     */
    @RequestMapping(value = "/photoLocationKeywords",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoLocationKeyword> createPhotoLocationKeyword(@Valid @RequestBody PhotoLocationKeyword photoLocationKeyword) throws URISyntaxException {
        log.debug("REST request to save PhotoLocationKeyword : {}", photoLocationKeyword);
        if (photoLocationKeyword.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new photoLocationKeyword cannot already have an ID").body(null);
        }
        PhotoLocationKeyword result = photoLocationKeywordRepository.save(photoLocationKeyword);
        return ResponseEntity.created(new URI("/api/photoLocationKeywords/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("photoLocationKeyword", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /photoLocationKeywords -> Updates an existing photoLocationKeyword.
     */
    @RequestMapping(value = "/photoLocationKeywords",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoLocationKeyword> updatePhotoLocationKeyword(@Valid @RequestBody PhotoLocationKeyword photoLocationKeyword) throws URISyntaxException {
        log.debug("REST request to update PhotoLocationKeyword : {}", photoLocationKeyword);
        if (photoLocationKeyword.getId() == null) {
            return createPhotoLocationKeyword(photoLocationKeyword);
        }
        PhotoLocationKeyword result = photoLocationKeywordRepository.save(photoLocationKeyword);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("photoLocationKeyword", photoLocationKeyword.getId().toString()))
            .body(result);
    }

    /**
     * GET  /photoLocationKeywords -> get all the photoLocationKeywords.
     */
    @RequestMapping(value = "/photoLocationKeywords",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PhotoLocationKeyword> getAllPhotoLocationKeywords() {
        log.debug("REST request to get all PhotoLocationKeywords");
        return photoLocationKeywordRepository.findAll();
    }

    /**
     * GET  /photoLocationKeywords/:id -> get the "id" photoLocationKeyword.
     */
    @RequestMapping(value = "/photoLocationKeywords/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoLocationKeyword> getPhotoLocationKeyword(@PathVariable Long id) {
        log.debug("REST request to get PhotoLocationKeyword : {}", id);
        return Optional.ofNullable(photoLocationKeywordRepository.findOne(id))
            .map(photoLocationKeyword -> new ResponseEntity<>(
                photoLocationKeyword,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /photoLocationKeywords/:id -> delete the "id" photoLocationKeyword.
     */
    @RequestMapping(value = "/photoLocationKeywords/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePhotoLocationKeyword(@PathVariable Long id) {
        log.debug("REST request to delete PhotoLocationKeyword : {}", id);
        photoLocationKeywordRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("photoLocationKeyword", id.toString())).build();
    }
}
