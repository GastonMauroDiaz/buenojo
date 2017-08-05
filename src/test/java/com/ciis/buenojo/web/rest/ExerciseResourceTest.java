package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.Exercise;
import com.ciis.buenojo.repository.ExerciseRepository;

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
 * Test class for the ExerciseResource REST controller.
 *
 * @see ExerciseResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ExerciseResourceTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Float DEFAULT_TOTAL_SCORE = 0.0f;
    private static final Float UPDATED_TOTAL_SCORE = 1.0f;

    @Inject
    private ExerciseRepository exerciseRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restExerciseMockMvc;

    private Exercise exercise;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExerciseResource exerciseResource = new ExerciseResource();
        ReflectionTestUtils.setField(exerciseResource, "exerciseRepository", exerciseRepository);
        this.restExerciseMockMvc = MockMvcBuilders.standaloneSetup(exerciseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        exercise = new Exercise();
        exercise.setName(DEFAULT_NAME);
        exercise.setDescription(DEFAULT_DESCRIPTION);
        exercise.setTotalScore(DEFAULT_TOTAL_SCORE);
    }

    @Test
    @Transactional
    public void createExercise() throws Exception {
        int databaseSizeBeforeCreate = exerciseRepository.findAll().size();

        // Create the Exercise

        restExerciseMockMvc.perform(post("/api/exercises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(exercise)))
                .andExpect(status().isCreated());

        // Validate the Exercise in the database
        List<Exercise> exercises = exerciseRepository.findAll();
        assertThat(exercises).hasSize(databaseSizeBeforeCreate + 1);
        Exercise testExercise = exercises.get(exercises.size() - 1);
        assertThat(testExercise.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExercise.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testExercise.getTotalScore()).isEqualTo(DEFAULT_TOTAL_SCORE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseRepository.findAll().size();
        // set the field null
        exercise.setName(null);

        // Create the Exercise, which fails.

        restExerciseMockMvc.perform(post("/api/exercises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(exercise)))
                .andExpect(status().isBadRequest());

        List<Exercise> exercises = exerciseRepository.findAll();
        assertThat(exercises).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseRepository.findAll().size();
        // set the field null
        exercise.setDescription(null);

        // Create the Exercise, which fails.

        restExerciseMockMvc.perform(post("/api/exercises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(exercise)))
                .andExpect(status().isBadRequest());

        List<Exercise> exercises = exerciseRepository.findAll();
        assertThat(exercises).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalScoreIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseRepository.findAll().size();
        // set the field null
        exercise.setTotalScore(null);

        // Create the Exercise, which fails.

        restExerciseMockMvc.perform(post("/api/exercises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(exercise)))
                .andExpect(status().isBadRequest());

        List<Exercise> exercises = exerciseRepository.findAll();
        assertThat(exercises).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExercises() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exercises
        restExerciseMockMvc.perform(get("/api/exercises"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(exercise.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].totalScore").value(hasItem(DEFAULT_TOTAL_SCORE)));
    }

    @Test
    @Transactional
    public void getExercise() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get the exercise
        restExerciseMockMvc.perform(get("/api/exercises/{id}", exercise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(exercise.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.totalScore").value(DEFAULT_TOTAL_SCORE));
    }

    @Test
    @Transactional
    public void getNonExistingExercise() throws Exception {
        // Get the exercise
        restExerciseMockMvc.perform(get("/api/exercises/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExercise() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

		int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();

        // Update the exercise
        exercise.setName(UPDATED_NAME);
        exercise.setDescription(UPDATED_DESCRIPTION);
        exercise.setTotalScore(UPDATED_TOTAL_SCORE);

        restExerciseMockMvc.perform(put("/api/exercises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(exercise)))
                .andExpect(status().isOk());

        // Validate the Exercise in the database
        List<Exercise> exercises = exerciseRepository.findAll();
        assertThat(exercises).hasSize(databaseSizeBeforeUpdate);
        Exercise testExercise = exercises.get(exercises.size() - 1);
        assertThat(testExercise.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExercise.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testExercise.getTotalScore()).isEqualTo(UPDATED_TOTAL_SCORE);
    }

    @Test
    @Transactional
    public void deleteExercise() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

		int databaseSizeBeforeDelete = exerciseRepository.findAll().size();

        // Get the exercise
        restExerciseMockMvc.perform(delete("/api/exercises/{id}", exercise.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Exercise> exercises = exerciseRepository.findAll();
        assertThat(exercises).hasSize(databaseSizeBeforeDelete - 1);
    }
}
