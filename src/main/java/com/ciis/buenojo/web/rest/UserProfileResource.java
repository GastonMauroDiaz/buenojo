package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.UserProfile;
import com.ciis.buenojo.repository.UserProfileRepository;
import com.ciis.buenojo.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserProfile.
 */
@RestController
@RequestMapping("/api")
public class UserProfileResource {

    private final Logger log = LoggerFactory.getLogger(UserProfileResource.class);

    @Inject
    private UserProfileRepository userProfileRepository;

    /**
     * POST  /userProfiles -> Create a new userProfile.
     */
    @RequestMapping(value = "/userProfiles",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserProfile> createUserProfile(@RequestBody UserProfile userProfile) throws URISyntaxException {
        log.debug("REST request to save UserProfile : {}", userProfile);
        if (userProfile.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new userProfile cannot already have an ID").body(null);
        }
        UserProfile result = userProfileRepository.save(userProfile);
        return ResponseEntity.created(new URI("/api/userProfiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userProfile", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /userProfiles -> Updates an existing userProfile.
     */
    @RequestMapping(value = "/userProfiles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserProfile> updateUserProfile(@RequestBody UserProfile userProfile) throws URISyntaxException {
        log.debug("REST request to update UserProfile : {}", userProfile);
        if (userProfile.getId() == null) {
            return createUserProfile(userProfile);
        }
        UserProfile result = userProfileRepository.save(userProfile);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userProfile", userProfile.getId().toString()))
            .body(result);
    }

    /**
     * GET  /userProfiles -> get all the userProfiles.
     */
    @RequestMapping(value = "/userProfiles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UserProfile> getAllUserProfiles() {
        log.debug("REST request to get all UserProfiles");
        return userProfileRepository.findAll();
    }

    /**
     * GET  /userProfiles/:id -> get the "id" userProfile.
     */
    @RequestMapping(value = "/userProfiles/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable Long id) {
        log.debug("REST request to get UserProfile : {}", id);
        return Optional.ofNullable(userProfileRepository.findOne(id))
            .map(userProfile -> new ResponseEntity<>(
                userProfile,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /userProfiles/:id -> get the "id" userProfile.
     */
    @RequestMapping(value = "/myProfile",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserProfile> getMyUserProfile() {
        log.debug("REST request to get own UserProfile");
        return Optional.ofNullable(userProfileRepository.findByUserIsCurrentUser())
            .map(userProfile -> new ResponseEntity<>(
                userProfile,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /userProfiles/:id -> delete the "id" userProfile.
     */
    @RequestMapping(value = "/userProfiles/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUserProfile(@PathVariable Long id) {
        log.debug("REST request to delete UserProfile : {}", id);
        userProfileRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userProfile", id.toString())).build();
    }
    
    /**
     * GET  /userProfiles/:id -> get the "id" userProfile.
     */
    @RequestMapping(value = "/users/{id}/profile",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserProfile> getProfile(@PathVariable Long id) {
        log.debug("REST request to get UserProfile for User : {}", id);
        return Optional.ofNullable(userProfileRepository.findOneByUserId(id))
            .map(userProfile -> new ResponseEntity<>(
                userProfile,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
