package com.ciis.buenojo.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ciis.buenojo.domain.HangManExerciseOption;
import com.ciis.buenojo.repository.HangManExerciseOptionRepository;
import com.ciis.buenojo.service.HangManExerciseOptionService;
import com.ciis.buenojo.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing HangManExerciseOption.
 */
@RestController
@RequestMapping("/api")
public class HangManExerciseOptionResource {

    private final Logger log = LoggerFactory.getLogger(HangManExerciseOptionResource.class);

    @Inject
    private HangManExerciseOptionRepository hangManExerciseOptionRepository;

    @Inject
    private HangManExerciseOptionService hangManExerciseOptionService;

    /**
     * POST  /hangManExerciseOptions -> Create a new hangManExerciseOption.
     */
    @RequestMapping(value = "/hangManExerciseOptions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HangManExerciseOption> createHangManExerciseOption(@Valid @RequestBody HangManExerciseOption hangManExerciseOption) throws URISyntaxException {
        log.debug("REST request to save HangManExerciseOption : {}", hangManExerciseOption);
        if (hangManExerciseOption.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new hangManExerciseOption cannot already have an ID").body(null);
        }
        HangManExerciseOption result = hangManExerciseOptionRepository.save(hangManExerciseOption);
        return ResponseEntity.created(new URI("/api/hangManExerciseOptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("hangManExerciseOption", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hangManExerciseOptions -> Updates an existing hangManExerciseOption.
     */
    @RequestMapping(value = "/hangManExerciseOptions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HangManExerciseOption> updateHangManExerciseOption(@Valid @RequestBody HangManExerciseOption hangManExerciseOption) throws URISyntaxException {
        log.debug("REST request to update HangManExerciseOption : {}", hangManExerciseOption);
        if (hangManExerciseOption.getId() == null) {
            return createHangManExerciseOption(hangManExerciseOption);
        }
        HangManExerciseOption result = hangManExerciseOptionRepository.save(hangManExerciseOption);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("hangManExerciseOption", hangManExerciseOption.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hangManExerciseOptions -> get all the hangManExerciseOptions.
     */
    @RequestMapping(value = "/hangManExerciseOptions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<HangManExerciseOption> getAllHangManExerciseOptions() {
        log.debug("REST request to get all HangManExerciseOptions");
        return hangManExerciseOptionRepository.findAll();
    }

    /**
     * GET  /hangManExerciseOptions/:id -> get the "id" hangManExerciseOption.
     */
    @RequestMapping(value = "/hangManExerciseOptions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HangManExerciseOption> getHangManExerciseOption(@PathVariable Long id) {
        log.debug("REST request to get HangManExerciseOption : {}", id);
        return Optional.ofNullable(hangManExerciseOptionRepository.findOne(id))
            .map(hangManExerciseOption -> new ResponseEntity<>(
                hangManExerciseOption,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /hangManExerciseOptions/:id -> delete the "id" hangManExerciseOption.
     */
    @RequestMapping(value = "/hangManExerciseOptions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHangManExerciseOption(@PathVariable Long id) {
        log.debug("REST request to delete HangManExerciseOption : {}", id);
        hangManExerciseOptionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hangManExerciseOption", id.toString())).build();
    }

    /**
     * GET  /hangManExercises -> get all the hangManExercises.
     */
    @RequestMapping(value = "/hangManExerciseOptions/container/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Object> getAllHangManOptionByContainer(@PathVariable Long id) {
        log.debug("REST request to get all HangManExercises");
        List<Object> output;
        output= hangManExerciseOptionService.getFixedOptionsRandomOrder(id, 10);
        for (Object o: output)
        	((Object[]) o)[1] = StringEscapeUtils.escapeHtml4(((Object[]) o)[1].toString());
        return output;
    }
}
