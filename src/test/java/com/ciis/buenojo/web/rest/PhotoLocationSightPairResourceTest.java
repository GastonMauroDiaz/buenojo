package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.PhotoLocationSightPair;
import com.ciis.buenojo.repository.PhotoLocationSightPairRepository;

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
 * Test class for the PhotoLocationSightPairResource REST controller.
 *
 * @see PhotoLocationSightPairResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PhotoLocationSightPairResourceTest {


    private static final Integer DEFAULT_NUMBER = 0;
    private static final Integer UPDATED_NUMBER = 1;

    private static final Integer DEFAULT_SATELLITE_X = 0;
    private static final Integer UPDATED_SATELLITE_X = 1;

    private static final Integer DEFAULT_SATELLITE_Y = 0;
    private static final Integer UPDATED_SATELLITE_Y = 1;

    private static final Integer DEFAULT_SATELLITE_TOLERANCE = 0;
    private static final Integer UPDATED_SATELLITE_TOLERANCE = 1;

    private static final Integer DEFAULT_TERRAIN_X = 0;
    private static final Integer UPDATED_TERRAIN_X = 1;

    private static final Integer DEFAULT_TERRAIN_Y = 0;
    private static final Integer UPDATED_TERRAIN_Y = 1;

    private static final Integer DEFAULT_TERRAIN_TOLERANCE = 0;
    private static final Integer UPDATED_TERRAIN_TOLERANCE = 1;

    @Inject
    private PhotoLocationSightPairRepository photoLocationSightPairRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPhotoLocationSightPairMockMvc;

    private PhotoLocationSightPair photoLocationSightPair;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PhotoLocationSightPairResource photoLocationSightPairResource = new PhotoLocationSightPairResource();
        ReflectionTestUtils.setField(photoLocationSightPairResource, "photoLocationSightPairRepository", photoLocationSightPairRepository);
        this.restPhotoLocationSightPairMockMvc = MockMvcBuilders.standaloneSetup(photoLocationSightPairResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        photoLocationSightPair = new PhotoLocationSightPair();
        photoLocationSightPair.setNumber(DEFAULT_NUMBER);
        photoLocationSightPair.setSatelliteX(DEFAULT_SATELLITE_X);
        photoLocationSightPair.setSatelliteY(DEFAULT_SATELLITE_Y);
        photoLocationSightPair.setSatelliteTolerance(DEFAULT_SATELLITE_TOLERANCE);
        photoLocationSightPair.setTerrainX(DEFAULT_TERRAIN_X);
        photoLocationSightPair.setTerrainY(DEFAULT_TERRAIN_Y);
        photoLocationSightPair.setTerrainTolerance(DEFAULT_TERRAIN_TOLERANCE);
    }

    @Test
    @Transactional
    public void createPhotoLocationSightPair() throws Exception {
        int databaseSizeBeforeCreate = photoLocationSightPairRepository.findAll().size();

        // Create the PhotoLocationSightPair

        restPhotoLocationSightPairMockMvc.perform(post("/api/photoLocationSightPairs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationSightPair)))
                .andExpect(status().isCreated());

        // Validate the PhotoLocationSightPair in the database
        List<PhotoLocationSightPair> photoLocationSightPairs = photoLocationSightPairRepository.findAll();
        assertThat(photoLocationSightPairs).hasSize(databaseSizeBeforeCreate + 1);
        PhotoLocationSightPair testPhotoLocationSightPair = photoLocationSightPairs.get(photoLocationSightPairs.size() - 1);
        assertThat(testPhotoLocationSightPair.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testPhotoLocationSightPair.getSatelliteX()).isEqualTo(DEFAULT_SATELLITE_X);
        assertThat(testPhotoLocationSightPair.getSatelliteY()).isEqualTo(DEFAULT_SATELLITE_Y);
        assertThat(testPhotoLocationSightPair.getSatelliteTolerance()).isEqualTo(DEFAULT_SATELLITE_TOLERANCE);
        assertThat(testPhotoLocationSightPair.getTerrainX()).isEqualTo(DEFAULT_TERRAIN_X);
        assertThat(testPhotoLocationSightPair.getTerrainY()).isEqualTo(DEFAULT_TERRAIN_Y);
        assertThat(testPhotoLocationSightPair.getTerrainTolerance()).isEqualTo(DEFAULT_TERRAIN_TOLERANCE);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoLocationSightPairRepository.findAll().size();
        // set the field null
        photoLocationSightPair.setNumber(null);

        // Create the PhotoLocationSightPair, which fails.

        restPhotoLocationSightPairMockMvc.perform(post("/api/photoLocationSightPairs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationSightPair)))
                .andExpect(status().isBadRequest());

        List<PhotoLocationSightPair> photoLocationSightPairs = photoLocationSightPairRepository.findAll();
        assertThat(photoLocationSightPairs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSatelliteXIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoLocationSightPairRepository.findAll().size();
        // set the field null
        photoLocationSightPair.setSatelliteX(null);

        // Create the PhotoLocationSightPair, which fails.

        restPhotoLocationSightPairMockMvc.perform(post("/api/photoLocationSightPairs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationSightPair)))
                .andExpect(status().isBadRequest());

        List<PhotoLocationSightPair> photoLocationSightPairs = photoLocationSightPairRepository.findAll();
        assertThat(photoLocationSightPairs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSatelliteYIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoLocationSightPairRepository.findAll().size();
        // set the field null
        photoLocationSightPair.setSatelliteY(null);

        // Create the PhotoLocationSightPair, which fails.

        restPhotoLocationSightPairMockMvc.perform(post("/api/photoLocationSightPairs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationSightPair)))
                .andExpect(status().isBadRequest());

        List<PhotoLocationSightPair> photoLocationSightPairs = photoLocationSightPairRepository.findAll();
        assertThat(photoLocationSightPairs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSatelliteToleranceIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoLocationSightPairRepository.findAll().size();
        // set the field null
        photoLocationSightPair.setSatelliteTolerance(null);

        // Create the PhotoLocationSightPair, which fails.

        restPhotoLocationSightPairMockMvc.perform(post("/api/photoLocationSightPairs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationSightPair)))
                .andExpect(status().isBadRequest());

        List<PhotoLocationSightPair> photoLocationSightPairs = photoLocationSightPairRepository.findAll();
        assertThat(photoLocationSightPairs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTerrainXIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoLocationSightPairRepository.findAll().size();
        // set the field null
        photoLocationSightPair.setTerrainX(null);

        // Create the PhotoLocationSightPair, which fails.

        restPhotoLocationSightPairMockMvc.perform(post("/api/photoLocationSightPairs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationSightPair)))
                .andExpect(status().isBadRequest());

        List<PhotoLocationSightPair> photoLocationSightPairs = photoLocationSightPairRepository.findAll();
        assertThat(photoLocationSightPairs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTerrainYIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoLocationSightPairRepository.findAll().size();
        // set the field null
        photoLocationSightPair.setTerrainY(null);

        // Create the PhotoLocationSightPair, which fails.

        restPhotoLocationSightPairMockMvc.perform(post("/api/photoLocationSightPairs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationSightPair)))
                .andExpect(status().isBadRequest());

        List<PhotoLocationSightPair> photoLocationSightPairs = photoLocationSightPairRepository.findAll();
        assertThat(photoLocationSightPairs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTerrainToleranceIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoLocationSightPairRepository.findAll().size();
        // set the field null
        photoLocationSightPair.setTerrainTolerance(null);

        // Create the PhotoLocationSightPair, which fails.

        restPhotoLocationSightPairMockMvc.perform(post("/api/photoLocationSightPairs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationSightPair)))
                .andExpect(status().isBadRequest());

        List<PhotoLocationSightPair> photoLocationSightPairs = photoLocationSightPairRepository.findAll();
        assertThat(photoLocationSightPairs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPhotoLocationSightPairs() throws Exception {
        // Initialize the database
        photoLocationSightPairRepository.saveAndFlush(photoLocationSightPair);

        // Get all the photoLocationSightPairs
        restPhotoLocationSightPairMockMvc.perform(get("/api/photoLocationSightPairs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(photoLocationSightPair.getId().intValue())))
                .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
                .andExpect(jsonPath("$.[*].satelliteX").value(hasItem(DEFAULT_SATELLITE_X)))
                .andExpect(jsonPath("$.[*].satelliteY").value(hasItem(DEFAULT_SATELLITE_Y)))
                .andExpect(jsonPath("$.[*].satelliteTolerance").value(hasItem(DEFAULT_SATELLITE_TOLERANCE)))
                .andExpect(jsonPath("$.[*].terrainX").value(hasItem(DEFAULT_TERRAIN_X)))
                .andExpect(jsonPath("$.[*].terrainY").value(hasItem(DEFAULT_TERRAIN_Y)))
                .andExpect(jsonPath("$.[*].terrainTolerance").value(hasItem(DEFAULT_TERRAIN_TOLERANCE)));
    }

    @Test
    @Transactional
    public void getPhotoLocationSightPair() throws Exception {
        // Initialize the database
        photoLocationSightPairRepository.saveAndFlush(photoLocationSightPair);

        // Get the photoLocationSightPair
        restPhotoLocationSightPairMockMvc.perform(get("/api/photoLocationSightPairs/{id}", photoLocationSightPair.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(photoLocationSightPair.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.satelliteX").value(DEFAULT_SATELLITE_X))
            .andExpect(jsonPath("$.satelliteY").value(DEFAULT_SATELLITE_Y))
            .andExpect(jsonPath("$.satelliteTolerance").value(DEFAULT_SATELLITE_TOLERANCE))
            .andExpect(jsonPath("$.terrainX").value(DEFAULT_TERRAIN_X))
            .andExpect(jsonPath("$.terrainY").value(DEFAULT_TERRAIN_Y))
            .andExpect(jsonPath("$.terrainTolerance").value(DEFAULT_TERRAIN_TOLERANCE));
    }

    @Test
    @Transactional
    public void getNonExistingPhotoLocationSightPair() throws Exception {
        // Get the photoLocationSightPair
        restPhotoLocationSightPairMockMvc.perform(get("/api/photoLocationSightPairs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePhotoLocationSightPair() throws Exception {
        // Initialize the database
        photoLocationSightPairRepository.saveAndFlush(photoLocationSightPair);

		int databaseSizeBeforeUpdate = photoLocationSightPairRepository.findAll().size();

        // Update the photoLocationSightPair
        photoLocationSightPair.setNumber(UPDATED_NUMBER);
        photoLocationSightPair.setSatelliteX(UPDATED_SATELLITE_X);
        photoLocationSightPair.setSatelliteY(UPDATED_SATELLITE_Y);
        photoLocationSightPair.setSatelliteTolerance(UPDATED_SATELLITE_TOLERANCE);
        photoLocationSightPair.setTerrainX(UPDATED_TERRAIN_X);
        photoLocationSightPair.setTerrainY(UPDATED_TERRAIN_Y);
        photoLocationSightPair.setTerrainTolerance(UPDATED_TERRAIN_TOLERANCE);

        restPhotoLocationSightPairMockMvc.perform(put("/api/photoLocationSightPairs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationSightPair)))
                .andExpect(status().isOk());

        // Validate the PhotoLocationSightPair in the database
        List<PhotoLocationSightPair> photoLocationSightPairs = photoLocationSightPairRepository.findAll();
        assertThat(photoLocationSightPairs).hasSize(databaseSizeBeforeUpdate);
        PhotoLocationSightPair testPhotoLocationSightPair = photoLocationSightPairs.get(photoLocationSightPairs.size() - 1);
        assertThat(testPhotoLocationSightPair.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testPhotoLocationSightPair.getSatelliteX()).isEqualTo(UPDATED_SATELLITE_X);
        assertThat(testPhotoLocationSightPair.getSatelliteY()).isEqualTo(UPDATED_SATELLITE_Y);
        assertThat(testPhotoLocationSightPair.getSatelliteTolerance()).isEqualTo(UPDATED_SATELLITE_TOLERANCE);
        assertThat(testPhotoLocationSightPair.getTerrainX()).isEqualTo(UPDATED_TERRAIN_X);
        assertThat(testPhotoLocationSightPair.getTerrainY()).isEqualTo(UPDATED_TERRAIN_Y);
        assertThat(testPhotoLocationSightPair.getTerrainTolerance()).isEqualTo(UPDATED_TERRAIN_TOLERANCE);
    }

    @Test
    @Transactional
    public void deletePhotoLocationSightPair() throws Exception {
        // Initialize the database
        photoLocationSightPairRepository.saveAndFlush(photoLocationSightPair);

		int databaseSizeBeforeDelete = photoLocationSightPairRepository.findAll().size();

        // Get the photoLocationSightPair
        restPhotoLocationSightPairMockMvc.perform(delete("/api/photoLocationSightPairs/{id}", photoLocationSightPair.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PhotoLocationSightPair> photoLocationSightPairs = photoLocationSightPairRepository.findAll();
        assertThat(photoLocationSightPairs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
