package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.LoaderTrace;
import com.ciis.buenojo.repository.LoaderTraceRepository;
import com.ciis.buenojo.service.LoaderTraceService;
import com.ciis.buenojo.web.rest.util.HeaderUtil;
import com.ciis.buenojo.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 * REST controller for managing LoaderTrace.
 */
@RestController
@RequestMapping("/api")
public class LoaderTraceResource {

    private final Logger log = LoggerFactory.getLogger(LoaderTraceResource.class);

    @Inject
    private LoaderTraceRepository loaderTraceRepository;
    
    @Inject
    private LoaderTraceService traceService;
    /**
     * POST  /loaderTraces -> Create a new loaderTrace.
     */
    @RequestMapping(value = "/loaderTraces",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LoaderTrace> createLoaderTrace(@Valid @RequestBody LoaderTrace loaderTrace) throws URISyntaxException {
        log.debug("REST request to save LoaderTrace : {}", loaderTrace);
        if (loaderTrace.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new loaderTrace cannot already have an ID").body(null);
        }
        LoaderTrace result = loaderTraceRepository.save(loaderTrace);
        return ResponseEntity.created(new URI("/api/loaderTraces/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("loaderTrace", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /loaderTraces -> Updates an existing loaderTrace.
     */
    @RequestMapping(value = "/loaderTraces",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LoaderTrace> updateLoaderTrace(@Valid @RequestBody LoaderTrace loaderTrace) throws URISyntaxException {
        log.debug("REST request to update LoaderTrace : {}", loaderTrace);
        if (loaderTrace.getId() == null) {
            return createLoaderTrace(loaderTrace);
        }
        LoaderTrace result = loaderTraceRepository.save(loaderTrace);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("loaderTrace", loaderTrace.getId().toString()))
                .body(result);
    }

    /**
     * GET  /loaderTraces -> get all the loaderTraces.
     */
    @RequestMapping(value = "/loaderTraces",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<LoaderTrace>> getAllLoaderTraces(Pageable pageable)
        throws URISyntaxException {
        Page<LoaderTrace> page = loaderTraceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/loaderTraces");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /loaderTraces/:id -> get the "id" loaderTrace.
     */
    @RequestMapping(value = "/loaderTraces/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LoaderTrace> getLoaderTrace(@PathVariable Long id) {
        log.debug("REST request to get LoaderTrace : {}", id);
        return Optional.ofNullable(loaderTraceRepository.findOne(id))
            .map(loaderTrace -> new ResponseEntity<>(
                loaderTrace,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /loaderTraces/:id -> delete the "id" loaderTrace.
     */
    @RequestMapping(value = "/loaderTraces/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLoaderTrace(@PathVariable Long id) {
        log.debug("REST request to delete LoaderTrace : {}", id);
        LoaderTrace trace = loaderTraceRepository.findOne(id);
        
        if (trace == null) {
        	return ResponseEntity.notFound().headers(HeaderUtil.createAlert("No se pudo encontrar el trace", id.toString())).build();
        }
        
        traceService.deleteLoadedExercises(trace);

        
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("loaderTrace", id.toString())).build();
    }
}
