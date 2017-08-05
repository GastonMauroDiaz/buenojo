package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.ImageResource;
import com.ciis.buenojo.domain.SatelliteImage;
import com.ciis.buenojo.repository.SatelliteImageRepository;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ciis.buenojo.domain.enumeration.SatelliteImageType;

/**
 * Test class for the SatelliteImageResource REST controller.
 *
 * @see SatelliteImageResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SatelliteImageResourceTest {


    private static final Double DEFAULT_METERS = 1D;
    private static final Double UPDATED_METERS = 2D;

    private static final Double DEFAULT_LON = 1D;
    private static final Double UPDATED_LON = 2D;

    private static final Double DEFAULT_LAT = 1D;
    private static final Double UPDATED_LAT = 2D;

    private static final Double DEFAULT_RESOLUTION = 1D;
    private static final Double UPDATED_RESOLUTION = 2D;
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final Long	DEFAULT_ID = 1000L;
   
    
    private static final String DEFAULT_COPYRIGHT = "AAAAA";
    private static final String UPDATED_COPYRIGHT = "BBBBB";


private static final SatelliteImageType DEFAULT_IMAGE_TYPE = SatelliteImageType.LandsatFC;
    private static final SatelliteImageType UPDATED_IMAGE_TYPE = SatelliteImageType.LandsatFC;

    @Inject
    private SatelliteImageRepository satelliteImageRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSatelliteImageMockMvc;

    private SatelliteImage satelliteImage;
    private ImageResource img;
    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SatelliteImageResource satelliteImageResource = new SatelliteImageResource();
        ReflectionTestUtils.setField(satelliteImageResource, "satelliteImageRepository", satelliteImageRepository);
        this.restSatelliteImageMockMvc = MockMvcBuilders.standaloneSetup(satelliteImageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        satelliteImage = new SatelliteImage();
        satelliteImage.setMeters(DEFAULT_METERS);
        satelliteImage.setLon(DEFAULT_LON);
        satelliteImage.setLat(DEFAULT_LAT);
        satelliteImage.setResolution(DEFAULT_RESOLUTION);
        satelliteImage.setName(DEFAULT_NAME);
        img = new ImageResource();
        img.setId(DEFAULT_ID);
        satelliteImage.setImage(img);
        
        satelliteImage.setCopyright(DEFAULT_COPYRIGHT);
        satelliteImage.setImageType(DEFAULT_IMAGE_TYPE);
    }

    @Test
    @Transactional
    public void createSatelliteImage() throws Exception {
        int databaseSizeBeforeCreate = satelliteImageRepository.findAll().size();

        // Create the SatelliteImage

        restSatelliteImageMockMvc.perform(post("/api/satelliteImages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(satelliteImage)))
                .andExpect(status().isCreated());

        // Validate the SatelliteImage in the database
        List<SatelliteImage> satelliteImages = satelliteImageRepository.findAll();
        assertThat(satelliteImages).hasSize(databaseSizeBeforeCreate + 1);
        SatelliteImage testSatelliteImage = satelliteImages.get(satelliteImages.size() - 1);
        assertThat(testSatelliteImage.getMeters()).isEqualTo(DEFAULT_METERS);
        assertThat(testSatelliteImage.getLon()).isEqualTo(DEFAULT_LON);
        assertThat(testSatelliteImage.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testSatelliteImage.getResolution()).isEqualTo(DEFAULT_RESOLUTION);
        assertThat(testSatelliteImage.getName()).isEqualTo(DEFAULT_NAME);
        
        assertThat(testSatelliteImage.getCopyright()).isEqualTo(DEFAULT_COPYRIGHT);
        assertThat(testSatelliteImage.getImageType()).isEqualTo(DEFAULT_IMAGE_TYPE);
    }

    @Test
    @Transactional
    public void checkMetersIsRequired() throws Exception {
        int databaseSizeBeforeTest = satelliteImageRepository.findAll().size();
        // set the field null
        satelliteImage.setMeters(null);

        // Create the SatelliteImage, which fails.

        restSatelliteImageMockMvc.perform(post("/api/satelliteImages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(satelliteImage)))
                .andExpect(status().isBadRequest());

        List<SatelliteImage> satelliteImages = satelliteImageRepository.findAll();
        assertThat(satelliteImages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLonIsRequired() throws Exception {
        int databaseSizeBeforeTest = satelliteImageRepository.findAll().size();
        // set the field null
        satelliteImage.setLon(null);

        // Create the SatelliteImage, which fails.

        restSatelliteImageMockMvc.perform(post("/api/satelliteImages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(satelliteImage)))
                .andExpect(status().isBadRequest());

        List<SatelliteImage> satelliteImages = satelliteImageRepository.findAll();
        assertThat(satelliteImages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLatIsRequired() throws Exception {
        int databaseSizeBeforeTest = satelliteImageRepository.findAll().size();
        // set the field null
        satelliteImage.setLat(null);

        // Create the SatelliteImage, which fails.

        restSatelliteImageMockMvc.perform(post("/api/satelliteImages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(satelliteImage)))
                .andExpect(status().isBadRequest());

        List<SatelliteImage> satelliteImages = satelliteImageRepository.findAll();
        assertThat(satelliteImages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkResolutionIsRequired() throws Exception {
        int databaseSizeBeforeTest = satelliteImageRepository.findAll().size();
        // set the field null
        satelliteImage.setResolution(null);

        // Create the SatelliteImage, which fails.

        restSatelliteImageMockMvc.perform(post("/api/satelliteImages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(satelliteImage)))
                .andExpect(status().isBadRequest());

        List<SatelliteImage> satelliteImages = satelliteImageRepository.findAll();
        assertThat(satelliteImages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = satelliteImageRepository.findAll().size();
        // set the field null
        satelliteImage.setName(null);

        // Create the SatelliteImage, which fails.

        restSatelliteImageMockMvc.perform(post("/api/satelliteImages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(satelliteImage)))
                .andExpect(status().isBadRequest());

        List<SatelliteImage> satelliteImages = satelliteImageRepository.findAll();
        assertThat(satelliteImages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkImageIsRequired() throws Exception {
        int databaseSizeBeforeTest = satelliteImageRepository.findAll().size();
        // set the field null
        satelliteImage.setImage(null);

        // Create the SatelliteImage, which fails.

        restSatelliteImageMockMvc.perform(post("/api/satelliteImages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(satelliteImage)))
                .andExpect(status().isBadRequest());

        List<SatelliteImage> satelliteImages = satelliteImageRepository.findAll();
        assertThat(satelliteImages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCopyrightIsRequired() throws Exception {
        int databaseSizeBeforeTest = satelliteImageRepository.findAll().size();
        // set the field null
        satelliteImage.setCopyright(null);

        // Create the SatelliteImage, which fails.

        restSatelliteImageMockMvc.perform(post("/api/satelliteImages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(satelliteImage)))
                .andExpect(status().isBadRequest());

        List<SatelliteImage> satelliteImages = satelliteImageRepository.findAll();
        assertThat(satelliteImages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkImageTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = satelliteImageRepository.findAll().size();
        // set the field null
        satelliteImage.setImageType(null);

        // Create the SatelliteImage, which fails.

        restSatelliteImageMockMvc.perform(post("/api/satelliteImages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(satelliteImage)))
                .andExpect(status().isBadRequest());

        List<SatelliteImage> satelliteImages = satelliteImageRepository.findAll();
        assertThat(satelliteImages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSatelliteImages() throws Exception {
        // Initialize the database
        satelliteImageRepository.saveAndFlush(satelliteImage);

        // Get all the satelliteImages
        restSatelliteImageMockMvc.perform(get("/api/satelliteImages"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(satelliteImage.getId().intValue())))
                .andExpect(jsonPath("$.[*].meters").value(hasItem(DEFAULT_METERS.doubleValue())))
                .andExpect(jsonPath("$.[*].lon").value(hasItem(DEFAULT_LON.doubleValue())))
                .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
                .andExpect(jsonPath("$.[*].resolution").value(hasItem(DEFAULT_RESOLUTION.doubleValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
//                .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
//                .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
                .andExpect(jsonPath("$.[*].copyright").value(hasItem(DEFAULT_COPYRIGHT.toString())))
                .andExpect(jsonPath("$.[*].imageType").value(hasItem(DEFAULT_IMAGE_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getSatelliteImage() throws Exception {
        // Initialize the database
        satelliteImageRepository.saveAndFlush(satelliteImage);

        // Get the satelliteImage
        restSatelliteImageMockMvc.perform(get("/api/satelliteImages/{id}", satelliteImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(satelliteImage.getId().intValue()))
            .andExpect(jsonPath("$.meters").value(DEFAULT_METERS.doubleValue()))
            .andExpect(jsonPath("$.lon").value(DEFAULT_LON.doubleValue()))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.doubleValue()))
            .andExpect(jsonPath("$.resolution").value(DEFAULT_RESOLUTION.doubleValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
//            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
//            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.copyright").value(DEFAULT_COPYRIGHT.toString()))
            .andExpect(jsonPath("$.imageType").value(DEFAULT_IMAGE_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSatelliteImage() throws Exception {
        // Get the satelliteImage
        restSatelliteImageMockMvc.perform(get("/api/satelliteImages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSatelliteImage() throws Exception {
        // Initialize the database
        satelliteImageRepository.saveAndFlush(satelliteImage);

		int databaseSizeBeforeUpdate = satelliteImageRepository.findAll().size();

        // Update the satelliteImage
        satelliteImage.setMeters(UPDATED_METERS);
        satelliteImage.setLon(UPDATED_LON);
        satelliteImage.setLat(UPDATED_LAT);
        satelliteImage.setResolution(UPDATED_RESOLUTION);
        satelliteImage.setName(UPDATED_NAME);
        satelliteImage.setImage(img);
//        satelliteImage.setImageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        satelliteImage.setCopyright(UPDATED_COPYRIGHT);
        satelliteImage.setImageType(UPDATED_IMAGE_TYPE);

        restSatelliteImageMockMvc.perform(put("/api/satelliteImages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(satelliteImage)))
                .andExpect(status().isOk());

        // Validate the SatelliteImage in the database
        List<SatelliteImage> satelliteImages = satelliteImageRepository.findAll();
        assertThat(satelliteImages).hasSize(databaseSizeBeforeUpdate);
        SatelliteImage testSatelliteImage = satelliteImages.get(satelliteImages.size() - 1);
        assertThat(testSatelliteImage.getMeters()).isEqualTo(UPDATED_METERS);
        assertThat(testSatelliteImage.getLon()).isEqualTo(UPDATED_LON);
        assertThat(testSatelliteImage.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testSatelliteImage.getResolution()).isEqualTo(UPDATED_RESOLUTION);
        assertThat(testSatelliteImage.getName()).isEqualTo(UPDATED_NAME);
//        assertThat(testSatelliteImage.getImage()).isEqualTo(UPDATED_IMAGE);
//        assertThat(testSatelliteImage.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testSatelliteImage.getCopyright()).isEqualTo(UPDATED_COPYRIGHT);
        assertThat(testSatelliteImage.getImageType()).isEqualTo(UPDATED_IMAGE_TYPE);
    }

    @Test
    @Transactional
    public void deleteSatelliteImage() throws Exception {
        // Initialize the database
        satelliteImageRepository.saveAndFlush(satelliteImage);

		int databaseSizeBeforeDelete = satelliteImageRepository.findAll().size();

        // Get the satelliteImage
        restSatelliteImageMockMvc.perform(delete("/api/satelliteImages/{id}", satelliteImage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SatelliteImage> satelliteImages = satelliteImageRepository.findAll();
        assertThat(satelliteImages).hasSize(databaseSizeBeforeDelete - 1);
    }
}
