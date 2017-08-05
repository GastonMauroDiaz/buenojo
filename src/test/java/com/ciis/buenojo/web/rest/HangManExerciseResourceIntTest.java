package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.HangManExercise;
import com.ciis.buenojo.repository.HangManExerciseRepository;

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
 * Test class for the HangManExerciseResource REST controller.
 *
 * @see HangManExerciseResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HangManExerciseResourceIntTest {

    private static final String DEFAULT_TASK = "AAAAAAAAAAAAAAA";
    private static final String UPDATED_TASK = "BBBBBBBBBBBBBBB";

    private static final Integer DEFAULT_EXERCISE_ORDER = 1;
    private static final Integer UPDATED_EXERCISE_ORDER = 2;

    @Inject
    private HangManExerciseRepository hangManExerciseRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restHangManExerciseMockMvc;

    private HangManExercise hangManExercise;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HangManExerciseResource hangManExerciseResource = new HangManExerciseResource();
        ReflectionTestUtils.setField(hangManExerciseResource, "hangManExerciseRepository", hangManExerciseRepository);
        this.restHangManExerciseMockMvc = MockMvcBuilders.standaloneSetup(hangManExerciseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        hangManExercise = new HangManExercise();
        hangManExercise.setTask(DEFAULT_TASK);
        hangManExercise.setExerciseOrder(DEFAULT_EXERCISE_ORDER);
    }

    @Test
    @Transactional
    public void createHangManExercise() throws Exception {
        int databaseSizeBeforeCreate = hangManExerciseRepository.findAll().size();

        // Create the HangManExercise

        restHangManExerciseMockMvc.perform(post("/api/hangManExercises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hangManExercise)))
                .andExpect(status().isCreated());

        // Validate the HangManExercise in the database
        List<HangManExercise> hangManExercises = hangManExerciseRepository.findAll();
        assertThat(hangManExercises).hasSize(databaseSizeBeforeCreate + 1);
        HangManExercise testHangManExercise = hangManExercises.get(hangManExercises.size() - 1);
        assertThat(testHangManExercise.getTask()).isEqualTo(DEFAULT_TASK);
        assertThat(testHangManExercise.getExerciseOrder()).isEqualTo(DEFAULT_EXERCISE_ORDER);
    }

    @Test
    @Transactional
    public void checkTaskIsRequired() throws Exception {
        int databaseSizeBeforeTest = hangManExerciseRepository.findAll().size();
        // set the field null
        hangManExercise.setTask(null);

        // Create the HangManExercise, which fails.

        restHangManExerciseMockMvc.perform(post("/api/hangManExercises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hangManExercise)))
                .andExpect(status().isBadRequest());

        List<HangManExercise> hangManExercises = hangManExerciseRepository.findAll();
        assertThat(hangManExercises).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHangManExercises() throws Exception {
        // Initialize the database
        hangManExerciseRepository.saveAndFlush(hangManExercise);

        // Get all the hangManExercises
        restHangManExerciseMockMvc.perform(get("/api/hangManExercises"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(hangManExercise.getId().intValue())))
                .andExpect(jsonPath("$.[*].task").value(hasItem(DEFAULT_TASK.toString())))
                .andExpect(jsonPath("$.[*].exerciseOrder").value(hasItem(DEFAULT_EXERCISE_ORDER)));
    }

    @Test
    @Transactional
    public void getHangManExercise() throws Exception {
        // Initialize the database
        hangManExerciseRepository.saveAndFlush(hangManExercise);

        // Get the hangManExercise
        restHangManExerciseMockMvc.perform(get("/api/hangManExercises/{id}", hangManExercise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(hangManExercise.getId().intValue()))
            .andExpect(jsonPath("$.task").value(DEFAULT_TASK.toString()))
            .andExpect(jsonPath("$.exerciseOrder").value(DEFAULT_EXERCISE_ORDER));
    }

    @Test
    @Transactional
    public void getNonExistingHangManExercise() throws Exception {
        // Get the hangManExercise
        restHangManExerciseMockMvc.perform(get("/api/hangManExercises/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHangManExercise() throws Exception {
        // Initialize the database
        hangManExerciseRepository.saveAndFlush(hangManExercise);

		int databaseSizeBeforeUpdate = hangManExerciseRepository.findAll().size();

        // Update the hangManExercise
        hangManExercise.setTask(UPDATED_TASK);
        hangManExercise.setExerciseOrder(UPDATED_EXERCISE_ORDER);

        restHangManExerciseMockMvc.perform(put("/api/hangManExercises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hangManExercise)))
                .andExpect(status().isOk());

        // Validate the HangManExercise in the database
        List<HangManExercise> hangManExercises = hangManExerciseRepository.findAll();
        assertThat(hangManExercises).hasSize(databaseSizeBeforeUpdate);
        HangManExercise testHangManExercise = hangManExercises.get(hangManExercises.size() - 1);
        assertThat(testHangManExercise.getTask()).isEqualTo(UPDATED_TASK);
        assertThat(testHangManExercise.getExerciseOrder()).isEqualTo(UPDATED_EXERCISE_ORDER);
    }

    @Test
    @Transactional
    public void deleteHangManExercise() throws Exception {
        // Initialize the database
        hangManExerciseRepository.saveAndFlush(hangManExercise);

		int databaseSizeBeforeDelete = hangManExerciseRepository.findAll().size();

        // Get the hangManExercise
        restHangManExerciseMockMvc.perform(delete("/api/hangManExercises/{id}", hangManExercise.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<HangManExercise> hangManExercises = hangManExerciseRepository.findAll();
        assertThat(hangManExercises).hasSize(databaseSizeBeforeDelete - 1);
    }
}
