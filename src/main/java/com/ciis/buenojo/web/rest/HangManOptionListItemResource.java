package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.HangManOptionListItem;
import com.ciis.buenojo.repository.HangManOptionListItemRepository;
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
 * REST controller for managing HangManOptionListItem.
 */
@RestController
@RequestMapping("/api")
public class HangManOptionListItemResource {

    private final Logger log = LoggerFactory.getLogger(HangManOptionListItemResource.class);

    @Inject
    private HangManOptionListItemRepository hangManOptionListItemRepository;

    /**
     * POST  /hangManOptionListItems -> Create a new hangManOptionListItem.
     */
    @RequestMapping(value = "/hangManOptionListItems",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HangManOptionListItem> createHangManOptionListItem(@Valid @RequestBody HangManOptionListItem hangManOptionListItem) throws URISyntaxException {
        log.debug("REST request to save HangManOptionListItem : {}", hangManOptionListItem);
        if (hangManOptionListItem.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new hangManOptionListItem cannot already have an ID").body(null);
        }
        HangManOptionListItem result = hangManOptionListItemRepository.save(hangManOptionListItem);
        return ResponseEntity.created(new URI("/api/hangManOptionListItems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("hangManOptionListItem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hangManOptionListItems -> Updates an existing hangManOptionListItem.
     */
    @RequestMapping(value = "/hangManOptionListItems",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HangManOptionListItem> updateHangManOptionListItem(@Valid @RequestBody HangManOptionListItem hangManOptionListItem) throws URISyntaxException {
        log.debug("REST request to update HangManOptionListItem : {}", hangManOptionListItem);
        if (hangManOptionListItem.getId() == null) {
            return createHangManOptionListItem(hangManOptionListItem);
        }
        HangManOptionListItem result = hangManOptionListItemRepository.save(hangManOptionListItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("hangManOptionListItem", hangManOptionListItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hangManOptionListItems -> get all the hangManOptionListItems.
     */
    @RequestMapping(value = "/hangManOptionListItems",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<HangManOptionListItem> getAllHangManOptionListItems() {
        log.debug("REST request to get all HangManOptionListItems");
        return hangManOptionListItemRepository.findAll();
    }

    /**
     * GET  /hangManOptionListItems/:id -> get the "id" hangManOptionListItem.
     */
    @RequestMapping(value = "/hangManOptionListItems/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HangManOptionListItem> getHangManOptionListItem(@PathVariable Long id) {
        log.debug("REST request to get HangManOptionListItem : {}", id);
        return Optional.ofNullable(hangManOptionListItemRepository.findOne(id))
            .map(hangManOptionListItem -> new ResponseEntity<>(
                hangManOptionListItem,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /hangManOptionListItems/:id -> delete the "id" hangManOptionListItem.
     */
    @RequestMapping(value = "/hangManOptionListItems/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHangManOptionListItem(@PathVariable Long id) {
        log.debug("REST request to delete HangManOptionListItem : {}", id);
        hangManOptionListItemRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hangManOptionListItem", id.toString())).build();
    }
}
