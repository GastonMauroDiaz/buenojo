package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.LoadedExercise;
import com.ciis.buenojo.repository.LoadedExerciseRepository;

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
 * Test class for the LoadedExerciseResource REST controller.
 *
 * @see LoadedExerciseResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class LoadedExerciseResourceTest {


    private static final Long DEFAULT_EXERCISE_ID = 1L;
    private static final Long UPDATED_EXERCISE_ID = 2L;

    @Inject
    private LoadedExerciseRepository loadedExerciseRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLoadedExerciseMockMvc;

    private LoadedExercise loadedExercise;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LoadedExerciseResource loadedExerciseResource = new LoadedExerciseResource();
        ReflectionTestUtils.setField(loadedExerciseResource, "loadedExerciseRepository", loadedExerciseRepository);
        this.restLoadedExerciseMockMvc = MockMvcBuilders.standaloneSetup(loadedExerciseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        loadedExercise = new LoadedExercise();
        loadedExercise.setExerciseId(DEFAULT_EXERCISE_ID);
    }

    @Test
    @Transactional
    public void createLoadedExercise() throws Exception {
        int databaseSizeBeforeCreate = loadedExerciseRepository.findAll().size();

        // Create the LoadedExercise

        restLoadedExerciseMockMvc.perform(post("/api/loadedExercises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(loadedExercise)))
                .andExpect(status().isCreated());

        // Validate the LoadedExercise in the database
        List<LoadedExercise> loadedExercises = loadedExerciseRepository.findAll();
        assertThat(loadedExercises).hasSize(databaseSizeBeforeCreate + 1);
        LoadedExercise testLoadedExercise = loadedExercises.get(loadedExercises.size() - 1);
        assertThat(testLoadedExercise.getExerciseId()).isEqualTo(DEFAULT_EXERCISE_ID);
    }

    @Test
    @Transactional
    public void checkExerciseIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = loadedExerciseRepository.findAll().size();
        // set the field null
        loadedExercise.setExerciseId(null);

        // Create the LoadedExercise, which fails.

        restLoadedExerciseMockMvc.perform(post("/api/loadedExercises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(loadedExercise)))
                .andExpect(status().isBadRequest());

        List<LoadedExercise> loadedExercises = loadedExerciseRepository.findAll();
        assertThat(loadedExercises).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLoadedExercises() throws Exception {
        // Initialize the database
        loadedExerciseRepository.saveAndFlush(loadedExercise);

        // Get all the loadedExercises
        restLoadedExerciseMockMvc.perform(get("/api/loadedExercises"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(loadedExercise.getId().intValue())))
                .andExpect(jsonPath("$.[*].exerciseId").value(hasItem(DEFAULT_EXERCISE_ID.intValue())));
    }

    @Test
    @Transactional
    public void getLoadedExercise() throws Exception {
        // Initialize the database
        loadedExerciseRepository.saveAndFlush(loadedExercise);

        // Get the loadedExercise
        restLoadedExerciseMockMvc.perform(get("/api/loadedExercises/{id}", loadedExercise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(loadedExercise.getId().intValue()))
            .andExpect(jsonPath("$.exerciseId").value(DEFAULT_EXERCISE_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLoadedExercise() throws Exception {
        // Get the loadedExercise
        restLoadedExerciseMockMvc.perform(get("/api/loadedExercises/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLoadedExercise() throws Exception {
        // Initialize the database
        loadedExerciseRepository.saveAndFlush(loadedExercise);

		int databaseSizeBeforeUpdate = loadedExerciseRepository.findAll().size();

        // Update the loadedExercise
        loadedExercise.setExerciseId(UPDATED_EXERCISE_ID);

        restLoadedExerciseMockMvc.perform(put("/api/loadedExercises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(loadedExercise)))
                .andExpect(status().isOk());

        // Validate the LoadedExercise in the database
        List<LoadedExercise> loadedExercises = loadedExerciseRepository.findAll();
        assertThat(loadedExercises).hasSize(databaseSizeBeforeUpdate);
        LoadedExercise testLoadedExercise = loadedExercises.get(loadedExercises.size() - 1);
        assertThat(testLoadedExercise.getExerciseId()).isEqualTo(UPDATED_EXERCISE_ID);
    }

    @Test
    @Transactional
    public void deleteLoadedExercise() throws Exception {
        // Initialize the database
        loadedExerciseRepository.saveAndFlush(loadedExercise);

		int databaseSizeBeforeDelete = loadedExerciseRepository.findAll().size();

        // Get the loadedExercise
        restLoadedExerciseMockMvc.perform(delete("/api/loadedExercises/{id}", loadedExercise.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<LoadedExercise> loadedExercises = loadedExerciseRepository.findAll();
        assertThat(loadedExercises).hasSize(databaseSizeBeforeDelete - 1);
    }
}
