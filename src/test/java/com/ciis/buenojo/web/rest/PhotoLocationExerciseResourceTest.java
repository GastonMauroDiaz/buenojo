package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.PhotoLocationExercise;
import com.ciis.buenojo.repository.PhotoLocationExerciseRepository;

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
 * Test class for the PhotoLocationExerciseResource REST controller.
 *
 * @see PhotoLocationExerciseResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PhotoLocationExerciseResourceTest {


    private static final Float DEFAULT_TOTAL_SCORE = 0F;
    private static final Float UPDATED_TOTAL_SCORE = 1F;

    private static final Integer DEFAULT_TOTAL_TIME_IN_SECONDS = 0;
    private static final Integer UPDATED_TOTAL_TIME_IN_SECONDS = 1;

    @Inject
    private PhotoLocationExerciseRepository photoLocationExerciseRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPhotoLocationExerciseMockMvc;

    private PhotoLocationExercise photoLocationExercise;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PhotoLocationExerciseResource photoLocationExerciseResource = new PhotoLocationExerciseResource();
        ReflectionTestUtils.setField(photoLocationExerciseResource, "photoLocationExerciseRepository", photoLocationExerciseRepository);
        this.restPhotoLocationExerciseMockMvc = MockMvcBuilders.standaloneSetup(photoLocationExerciseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        photoLocationExercise = new PhotoLocationExercise();
        photoLocationExercise.setTotalScore(DEFAULT_TOTAL_SCORE);
        photoLocationExercise.setTotalTimeInSeconds(DEFAULT_TOTAL_TIME_IN_SECONDS);
    }

    @Test
    @Transactional
    public void createPhotoLocationExercise() throws Exception {
        int databaseSizeBeforeCreate = photoLocationExerciseRepository.findAll().size();

        // Create the PhotoLocationExercise

        restPhotoLocationExerciseMockMvc.perform(post("/api/photoLocationExercises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationExercise)))
                .andExpect(status().isCreated());

        // Validate the PhotoLocationExercise in the database
        List<PhotoLocationExercise> photoLocationExercises = photoLocationExerciseRepository.findAll();
        assertThat(photoLocationExercises).hasSize(databaseSizeBeforeCreate + 1);
        PhotoLocationExercise testPhotoLocationExercise = photoLocationExercises.get(photoLocationExercises.size() - 1);
        assertThat(testPhotoLocationExercise.getTotalScore()).isEqualTo(DEFAULT_TOTAL_SCORE);
        assertThat(testPhotoLocationExercise.getTotalTimeInSeconds()).isEqualTo(DEFAULT_TOTAL_TIME_IN_SECONDS);
    }

    @Test
    @Transactional
    public void checkTotalScoreIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoLocationExerciseRepository.findAll().size();
        // set the field null
        photoLocationExercise.setTotalScore(null);

        // Create the PhotoLocationExercise, which fails.

        restPhotoLocationExerciseMockMvc.perform(post("/api/photoLocationExercises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationExercise)))
                .andExpect(status().isBadRequest());

        List<PhotoLocationExercise> photoLocationExercises = photoLocationExerciseRepository.findAll();
        assertThat(photoLocationExercises).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalTimeInSecondsIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoLocationExerciseRepository.findAll().size();
        // set the field null
        photoLocationExercise.setTotalTimeInSeconds(null);

        // Create the PhotoLocationExercise, which fails.

        restPhotoLocationExerciseMockMvc.perform(post("/api/photoLocationExercises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationExercise)))
                .andExpect(status().isBadRequest());

        List<PhotoLocationExercise> photoLocationExercises = photoLocationExerciseRepository.findAll();
        assertThat(photoLocationExercises).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPhotoLocationExercises() throws Exception {
        // Initialize the database
        photoLocationExerciseRepository.saveAndFlush(photoLocationExercise);

        // Get all the photoLocationExercises
        restPhotoLocationExerciseMockMvc.perform(get("/api/photoLocationExercises"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(photoLocationExercise.getId().intValue())))
                .andExpect(jsonPath("$.[*].totalScore").value(hasItem(DEFAULT_TOTAL_SCORE.doubleValue())))
                .andExpect(jsonPath("$.[*].totalTimeInSeconds").value(hasItem(DEFAULT_TOTAL_TIME_IN_SECONDS)));
    }

    @Test
    @Transactional
    public void getPhotoLocationExercise() throws Exception {
        // Initialize the database
        photoLocationExerciseRepository.saveAndFlush(photoLocationExercise);

        // Get the photoLocationExercise
        restPhotoLocationExerciseMockMvc.perform(get("/api/photoLocationExercises/{id}", photoLocationExercise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(photoLocationExercise.getId().intValue()))
            .andExpect(jsonPath("$.totalScore").value(DEFAULT_TOTAL_SCORE.doubleValue()))
            .andExpect(jsonPath("$.totalTimeInSeconds").value(DEFAULT_TOTAL_TIME_IN_SECONDS));
    }

    @Test
    @Transactional
    public void getNonExistingPhotoLocationExercise() throws Exception {
        // Get the photoLocationExercise
        restPhotoLocationExerciseMockMvc.perform(get("/api/photoLocationExercises/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePhotoLocationExercise() throws Exception {
        // Initialize the database
        photoLocationExerciseRepository.saveAndFlush(photoLocationExercise);

		int databaseSizeBeforeUpdate = photoLocationExerciseRepository.findAll().size();

        // Update the photoLocationExercise
        photoLocationExercise.setTotalScore(UPDATED_TOTAL_SCORE);
        photoLocationExercise.setTotalTimeInSeconds(UPDATED_TOTAL_TIME_IN_SECONDS);

        restPhotoLocationExerciseMockMvc.perform(put("/api/photoLocationExercises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationExercise)))
                .andExpect(status().isOk());

        // Validate the PhotoLocationExercise in the database
        List<PhotoLocationExercise> photoLocationExercises = photoLocationExerciseRepository.findAll();
        assertThat(photoLocationExercises).hasSize(databaseSizeBeforeUpdate);
        PhotoLocationExercise testPhotoLocationExercise = photoLocationExercises.get(photoLocationExercises.size() - 1);
        assertThat(testPhotoLocationExercise.getTotalScore()).isEqualTo(UPDATED_TOTAL_SCORE);
        assertThat(testPhotoLocationExercise.getTotalTimeInSeconds()).isEqualTo(UPDATED_TOTAL_TIME_IN_SECONDS);
    }

    @Test
    @Transactional
    public void deletePhotoLocationExercise() throws Exception {
        // Initialize the database
        photoLocationExerciseRepository.saveAndFlush(photoLocationExercise);

		int databaseSizeBeforeDelete = photoLocationExerciseRepository.findAll().size();

        // Get the photoLocationExercise
        restPhotoLocationExerciseMockMvc.perform(delete("/api/photoLocationExercises/{id}", photoLocationExercise.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PhotoLocationExercise> photoLocationExercises = photoLocationExerciseRepository.findAll();
        assertThat(photoLocationExercises).hasSize(databaseSizeBeforeDelete - 1);
    }
}
