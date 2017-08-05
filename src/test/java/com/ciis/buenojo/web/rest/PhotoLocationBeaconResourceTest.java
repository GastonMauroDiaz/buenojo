package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.PhotoLocationBeacon;
import com.ciis.buenojo.repository.PhotoLocationBeaconRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PhotoLocationBeaconResource REST controller.
 *
 * @see PhotoLocationBeaconResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PhotoLocationBeaconResourceTest {


    private static final Integer DEFAULT_X = 1;
    private static final Integer UPDATED_X = 2;

    private static final Integer DEFAULT_Y = 1;
    private static final Integer UPDATED_Y = 2;

    private static final Integer DEFAULT_TOLERANCE = 0;
    private static final Integer UPDATED_TOLERANCE = 1;

    @Inject
    private PhotoLocationBeaconRepository photoLocationBeaconRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPhotoLocationBeaconMockMvc;

    private PhotoLocationBeacon photoLocationBeacon;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PhotoLocationBeaconResource photoLocationBeaconResource = new PhotoLocationBeaconResource();
        ReflectionTestUtils.setField(photoLocationBeaconResource, "photoLocationBeaconRepository", photoLocationBeaconRepository);
        this.restPhotoLocationBeaconMockMvc = MockMvcBuilders.standaloneSetup(photoLocationBeaconResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        photoLocationBeacon = new PhotoLocationBeacon();
        photoLocationBeacon.setX(DEFAULT_X);
        photoLocationBeacon.setY(DEFAULT_Y);
        photoLocationBeacon.setTolerance(DEFAULT_TOLERANCE);
    }

    @Test
    @Transactional
    public void createPhotoLocationBeacon() throws Exception {
        int databaseSizeBeforeCreate = photoLocationBeaconRepository.findAll().size();

        // Create the PhotoLocationBeacon

        restPhotoLocationBeaconMockMvc.perform(post("/api/photoLocationBeacons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationBeacon)))
                .andExpect(status().isCreated());

        // Validate the PhotoLocationBeacon in the database
        List<PhotoLocationBeacon> photoLocationBeacons = photoLocationBeaconRepository.findAll();
        assertThat(photoLocationBeacons).hasSize(databaseSizeBeforeCreate + 1);
        PhotoLocationBeacon testPhotoLocationBeacon = photoLocationBeacons.get(photoLocationBeacons.size() - 1);
        assertThat(testPhotoLocationBeacon.getX()).isEqualTo(DEFAULT_X);
        assertThat(testPhotoLocationBeacon.getY()).isEqualTo(DEFAULT_Y);
        assertThat(testPhotoLocationBeacon.getTolerance()).isEqualTo(DEFAULT_TOLERANCE);
    }

    @Test
    @Transactional
    public void checkYIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoLocationBeaconRepository.findAll().size();
        // set the field null
        photoLocationBeacon.setY(null);

        // Create the PhotoLocationBeacon, which fails.

        restPhotoLocationBeaconMockMvc.perform(post("/api/photoLocationBeacons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationBeacon)))
                .andExpect(status().isBadRequest());

        List<PhotoLocationBeacon> photoLocationBeacons = photoLocationBeaconRepository.findAll();
        assertThat(photoLocationBeacons).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkToleranceIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoLocationBeaconRepository.findAll().size();
        // set the field null
        photoLocationBeacon.setTolerance(null);

        // Create the PhotoLocationBeacon, which fails.

        restPhotoLocationBeaconMockMvc.perform(post("/api/photoLocationBeacons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationBeacon)))
                .andExpect(status().isBadRequest());

        List<PhotoLocationBeacon> photoLocationBeacons = photoLocationBeaconRepository.findAll();
        assertThat(photoLocationBeacons).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPhotoLocationBeacons() throws Exception {
        // Initialize the database
        photoLocationBeaconRepository.saveAndFlush(photoLocationBeacon);

        // Get all the photoLocationBeacons
        restPhotoLocationBeaconMockMvc.perform(get("/api/photoLocationBeacons"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(photoLocationBeacon.getId().intValue())))
                .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X)))
                .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y)))
                .andExpect(jsonPath("$.[*].tolerance").value(hasItem(DEFAULT_TOLERANCE)));
    }

    @Test
    @Transactional
    public void getPhotoLocationBeacon() throws Exception {
        // Initialize the database
        photoLocationBeaconRepository.saveAndFlush(photoLocationBeacon);

        // Get the photoLocationBeacon
        restPhotoLocationBeaconMockMvc.perform(get("/api/photoLocationBeacons/{id}", photoLocationBeacon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(photoLocationBeacon.getId().intValue()))
            .andExpect(jsonPath("$.x").value(DEFAULT_X))
            .andExpect(jsonPath("$.y").value(DEFAULT_Y))
            .andExpect(jsonPath("$.tolerance").value(DEFAULT_TOLERANCE));
    }

    @Test
    @Transactional
    public void getNonExistingPhotoLocationBeacon() throws Exception {
        // Get the photoLocationBeacon
        restPhotoLocationBeaconMockMvc.perform(get("/api/photoLocationBeacons/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePhotoLocationBeacon() throws Exception {
        // Initialize the database
        photoLocationBeaconRepository.saveAndFlush(photoLocationBeacon);

		int databaseSizeBeforeUpdate = photoLocationBeaconRepository.findAll().size();

        // Update the photoLocationBeacon
        photoLocationBeacon.setX(UPDATED_X);
        photoLocationBeacon.setY(UPDATED_Y);
        photoLocationBeacon.setTolerance(UPDATED_TOLERANCE);

        restPhotoLocationBeaconMockMvc.perform(put("/api/photoLocationBeacons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationBeacon)))
                .andExpect(status().isOk());

        // Validate the PhotoLocationBeacon in the database
        List<PhotoLocationBeacon> photoLocationBeacons = photoLocationBeaconRepository.findAll();
        assertThat(photoLocationBeacons).hasSize(databaseSizeBeforeUpdate);
        PhotoLocationBeacon testPhotoLocationBeacon = photoLocationBeacons.get(photoLocationBeacons.size() - 1);
        assertThat(testPhotoLocationBeacon.getX()).isEqualTo(UPDATED_X);
        assertThat(testPhotoLocationBeacon.getY()).isEqualTo(UPDATED_Y);
        assertThat(testPhotoLocationBeacon.getTolerance()).isEqualTo(UPDATED_TOLERANCE);
    }

    @Test
    @Transactional
    public void deletePhotoLocationBeacon() throws Exception {
        // Initialize the database
        photoLocationBeaconRepository.saveAndFlush(photoLocationBeacon);

		int databaseSizeBeforeDelete = photoLocationBeaconRepository.findAll().size();

        // Get the photoLocationBeacon
        restPhotoLocationBeaconMockMvc.perform(delete("/api/photoLocationBeacons/{id}", photoLocationBeacon.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PhotoLocationBeacon> photoLocationBeacons = photoLocationBeaconRepository.findAll();
        assertThat(photoLocationBeacons).hasSize(databaseSizeBeforeDelete - 1);
    }
}
