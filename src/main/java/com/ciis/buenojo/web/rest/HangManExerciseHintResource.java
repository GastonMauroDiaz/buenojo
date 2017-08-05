package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.HangManExercise;
import com.ciis.buenojo.domain.HangManExerciseHint;
import com.ciis.buenojo.repository.HangManExerciseHintRepository;
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
 * REST controller for managing HangManExerciseHint.
 */
@RestController
@RequestMapping("/api")
public class HangManExerciseHintResource {

    private final Logger log = LoggerFactory.getLogger(HangManExerciseHintResource.class);

    @Inject
    private HangManExerciseHintRepository hangManExerciseHintRepository;

    /**
     * POST  /hangManExerciseHints -> Create a new hangManExerciseHint.
     */
    @RequestMapping(value = "/hangManExerciseHints",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HangManExerciseHint> createHangManExerciseHint(@Valid @RequestBody HangManExerciseHint hangManExerciseHint) throws URISyntaxException {
        log.debug("REST request to save HangManExerciseHint : {}", hangManExerciseHint);
        if (hangManExerciseHint.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new hangManExerciseHint cannot already have an ID").body(null);
        }
        HangManExerciseHint result = hangManExerciseHintRepository.save(hangManExerciseHint);
        return ResponseEntity.created(new URI("/api/hangManExerciseHints/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("hangManExerciseHint", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hangManExerciseHints -> Updates an existing hangManExerciseHint.
     */
    @RequestMapping(value = "/hangManExerciseHints",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HangManExerciseHint> updateHangManExerciseHint(@Valid @RequestBody HangManExerciseHint hangManExerciseHint) throws URISyntaxException {
        log.debug("REST request to update HangManExerciseHint : {}", hangManExerciseHint);
        if (hangManExerciseHint.getId() == null) {
            return createHangManExerciseHint(hangManExerciseHint);
        }
        HangManExerciseHint result = hangManExerciseHintRepository.save(hangManExerciseHint);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("hangManExerciseHint", hangManExerciseHint.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hangManExerciseHints -> get all the hangManExerciseHints.
     */
    @RequestMapping(value = "/hangManExerciseHints",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<HangManExerciseHint> getAllHangManExerciseHints() {
        log.debug("REST request to get all HangManExerciseHints");
        return hangManExerciseHintRepository.findAll();
    }

    /**
     * GET  /hangManExerciseHints/:id -> get the "id" hangManExerciseHint.
     */
    @RequestMapping(value = "/hangManExerciseHints/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HangManExerciseHint> getHangManExerciseHint(@PathVariable Long id) {
        log.debug("REST request to get HangManExerciseHint : {}", id);
        return Optional.ofNullable(hangManExerciseHintRepository.findOne(id))
            .map(hangManExerciseHint -> new ResponseEntity<>(
                hangManExerciseHint,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /hangManExerciseHints/:id -> delete the "id" hangManExerciseHint.
     */
    @RequestMapping(value = "/hangManExerciseHints/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHangManExerciseHint(@PathVariable Long id) {
        log.debug("REST request to delete HangManExerciseHint : {}", id);
        hangManExerciseHintRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hangManExerciseHint", id.toString())).build();
    }

    /**
     * GET  /hangManExercises -> get all the hangManExercises.
     */
    @RequestMapping(value = "/hangManExerciseHints/container/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<HangManExercise> getAllHangManExercisesByContainer(@PathVariable Long id) {
        log.debug("REST request to get all HangManExercises");
        return hangManExerciseHintRepository.findByhangmanGameContainerId(id);
    }
    

}
