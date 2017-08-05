package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.HangManGameContainer;
import com.ciis.buenojo.repository.HangManGameContainerRepository;
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
 * REST controller for managing HangManGameContainer.
 */
@RestController
@RequestMapping("/api")
public class HangManGameContainerResource {

    private final Logger log = LoggerFactory.getLogger(HangManGameContainerResource.class);

    @Inject
    private HangManGameContainerRepository hangManGameContainerRepository;

    /**
     * POST  /hangManGameContainers -> Create a new hangManGameContainer.
     */
    @RequestMapping(value = "/hangManGameContainers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HangManGameContainer> createHangManGameContainer(@Valid @RequestBody HangManGameContainer hangManGameContainer) throws URISyntaxException {
        log.debug("REST request to save HangManGameContainer : {}", hangManGameContainer);
        if (hangManGameContainer.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new hangManGameContainer cannot already have an ID").body(null);
        }
        HangManGameContainer result = hangManGameContainerRepository.save(hangManGameContainer);
        return ResponseEntity.created(new URI("/api/hangManGameContainers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("hangManGameContainer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hangManGameContainers -> Updates an existing hangManGameContainer.
     */
    @RequestMapping(value = "/hangManGameContainers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HangManGameContainer> updateHangManGameContainer(@Valid @RequestBody HangManGameContainer hangManGameContainer) throws URISyntaxException {
        log.debug("REST request to update HangManGameContainer : {}", hangManGameContainer);
        if (hangManGameContainer.getId() == null) {
            return createHangManGameContainer(hangManGameContainer);
        }
        HangManGameContainer result = hangManGameContainerRepository.save(hangManGameContainer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("hangManGameContainer", hangManGameContainer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hangManGameContainers -> get all the hangManGameContainers.
     */
    @RequestMapping(value = "/hangManGameContainers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<HangManGameContainer> getAllHangManGameContainers() {
        log.debug("REST request to get all HangManGameContainers");
        return hangManGameContainerRepository.findAll();
    }

    /**
     * GET  /hangManGameContainers/:id -> get the "id" hangManGameContainer.
     */
    @RequestMapping(value = "/hangManGameContainers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HangManGameContainer> getHangManGameContainer(@PathVariable Long id) {
        log.debug("REST request to get HangManGameContainer : {}", id);
        return Optional.ofNullable(hangManGameContainerRepository.findOne(id))
            .map(hangManGameContainer -> new ResponseEntity<>(
                hangManGameContainer,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /hangManGameContainers/:id -> delete the "id" hangManGameContainer.
     */
    @RequestMapping(value = "/hangManGameContainers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHangManGameContainer(@PathVariable Long id) {
        log.debug("REST request to delete HangManGameContainer : {}", id);
        hangManGameContainerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hangManGameContainer", id.toString())).build();
    }
}
