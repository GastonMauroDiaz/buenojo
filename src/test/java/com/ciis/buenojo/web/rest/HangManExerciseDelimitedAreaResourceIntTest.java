package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.HangManExerciseDelimitedArea;
import com.ciis.buenojo.repository.HangManExerciseDelimitedAreaRepository;

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
 * Test class for the HangManExerciseDelimitedAreaResource REST controller.
 *
 * @see HangManExerciseDelimitedAreaResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HangManExerciseDelimitedAreaResourceIntTest {


    private static final Integer DEFAULT_X = 0;
    private static final Integer UPDATED_X = 1;

    private static final Integer DEFAULT_Y = 0;
    private static final Integer UPDATED_Y = 1;

    private static final Integer DEFAULT_RADIUS = 10;
    private static final Integer UPDATED_RADIUS = 11;

    @Inject
    private HangManExerciseDelimitedAreaRepository hangManExerciseDelimitedAreaRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restHangManExerciseDelimitedAreaMockMvc;

    private HangManExerciseDelimitedArea hangManExerciseDelimitedArea;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HangManExerciseDelimitedAreaResource hangManExerciseDelimitedAreaResource = new HangManExerciseDelimitedAreaResource();
        ReflectionTestUtils.setField(hangManExerciseDelimitedAreaResource, "hangManExerciseDelimitedAreaRepository", hangManExerciseDelimitedAreaRepository);
        this.restHangManExerciseDelimitedAreaMockMvc = MockMvcBuilders.standaloneSetup(hangManExerciseDelimitedAreaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        hangManExerciseDelimitedArea = new HangManExerciseDelimitedArea();
        hangManExerciseDelimitedArea.setX(DEFAULT_X);
        hangManExerciseDelimitedArea.setY(DEFAULT_Y);
        hangManExerciseDelimitedArea.setRadius(DEFAULT_RADIUS);
    }

    @Test
    @Transactional
    public void createHangManExerciseDelimitedArea() throws Exception {
        int databaseSizeBeforeCreate = hangManExerciseDelimitedAreaRepository.findAll().size();

        // Create the HangManExerciseDelimitedArea

        restHangManExerciseDelimitedAreaMockMvc.perform(post("/api/hangManExerciseDelimitedAreas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hangManExerciseDelimitedArea)))
                .andExpect(status().isCreated());

        // Validate the HangManExerciseDelimitedArea in the database
        List<HangManExerciseDelimitedArea> hangManExerciseDelimitedAreas = hangManExerciseDelimitedAreaRepository.findAll();
        assertThat(hangManExerciseDelimitedAreas).hasSize(databaseSizeBeforeCreate + 1);
        HangManExerciseDelimitedArea testHangManExerciseDelimitedArea = hangManExerciseDelimitedAreas.get(hangManExerciseDelimitedAreas.size() - 1);
        assertThat(testHangManExerciseDelimitedArea.getX()).isEqualTo(DEFAULT_X);
        assertThat(testHangManExerciseDelimitedArea.getY()).isEqualTo(DEFAULT_Y);
        assertThat(testHangManExerciseDelimitedArea.getRadius()).isEqualTo(DEFAULT_RADIUS);
    }

    @Test
    @Transactional
    public void checkXIsRequired() throws Exception {
        int databaseSizeBeforeTest = hangManExerciseDelimitedAreaRepository.findAll().size();
        // set the field null
        hangManExerciseDelimitedArea.setX(null);

        // Create the HangManExerciseDelimitedArea, which fails.

        restHangManExerciseDelimitedAreaMockMvc.perform(post("/api/hangManExerciseDelimitedAreas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hangManExerciseDelimitedArea)))
                .andExpect(status().isBadRequest());

        List<HangManExerciseDelimitedArea> hangManExerciseDelimitedAreas = hangManExerciseDelimitedAreaRepository.findAll();
        assertThat(hangManExerciseDelimitedAreas).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYIsRequired() throws Exception {
        int databaseSizeBeforeTest = hangManExerciseDelimitedAreaRepository.findAll().size();
        // set the field null
        hangManExerciseDelimitedArea.setY(null);

        // Create the HangManExerciseDelimitedArea, which fails.

        restHangManExerciseDelimitedAreaMockMvc.perform(post("/api/hangManExerciseDelimitedAreas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hangManExerciseDelimitedArea)))
                .andExpect(status().isBadRequest());

        List<HangManExerciseDelimitedArea> hangManExerciseDelimitedAreas = hangManExerciseDelimitedAreaRepository.findAll();
        assertThat(hangManExerciseDelimitedAreas).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRadiusIsRequired() throws Exception {
        int databaseSizeBeforeTest = hangManExerciseDelimitedAreaRepository.findAll().size();
        // set the field null
        hangManExerciseDelimitedArea.setRadius(null);

        // Create the HangManExerciseDelimitedArea, which fails.

        restHangManExerciseDelimitedAreaMockMvc.perform(post("/api/hangManExerciseDelimitedAreas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hangManExerciseDelimitedArea)))
                .andExpect(status().isBadRequest());

        List<HangManExerciseDelimitedArea> hangManExerciseDelimitedAreas = hangManExerciseDelimitedAreaRepository.findAll();
        assertThat(hangManExerciseDelimitedAreas).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHangManExerciseDelimitedAreas() throws Exception {
        // Initialize the database
        hangManExerciseDelimitedAreaRepository.saveAndFlush(hangManExerciseDelimitedArea);

        // Get all the hangManExerciseDelimitedAreas
        restHangManExerciseDelimitedAreaMockMvc.perform(get("/api/hangManExerciseDelimitedAreas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(hangManExerciseDelimitedArea.getId().intValue())))
                .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X)))
                .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y)))
                .andExpect(jsonPath("$.[*].radius").value(hasItem(DEFAULT_RADIUS)));
    }

    @Test
    @Transactional
    public void getHangManExerciseDelimitedArea() throws Exception {
        // Initialize the database
        hangManExerciseDelimitedAreaRepository.saveAndFlush(hangManExerciseDelimitedArea);

        // Get the hangManExerciseDelimitedArea
        restHangManExerciseDelimitedAreaMockMvc.perform(get("/api/hangManExerciseDelimitedAreas/{id}", hangManExerciseDelimitedArea.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(hangManExerciseDelimitedArea.getId().intValue()))
            .andExpect(jsonPath("$.x").value(DEFAULT_X))
            .andExpect(jsonPath("$.y").value(DEFAULT_Y))
            .andExpect(jsonPath("$.radius").value(DEFAULT_RADIUS));
    }

    @Test
    @Transactional
    public void getNonExistingHangManExerciseDelimitedArea() throws Exception {
        // Get the hangManExerciseDelimitedArea
        restHangManExerciseDelimitedAreaMockMvc.perform(get("/api/hangManExerciseDelimitedAreas/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHangManExerciseDelimitedArea() throws Exception {
        // Initialize the database
        hangManExerciseDelimitedAreaRepository.saveAndFlush(hangManExerciseDelimitedArea);

		int databaseSizeBeforeUpdate = hangManExerciseDelimitedAreaRepository.findAll().size();

        // Update the hangManExerciseDelimitedArea
        hangManExerciseDelimitedArea.setX(UPDATED_X);
        hangManExerciseDelimitedArea.setY(UPDATED_Y);
        hangManExerciseDelimitedArea.setRadius(UPDATED_RADIUS);

        restHangManExerciseDelimitedAreaMockMvc.perform(put("/api/hangManExerciseDelimitedAreas")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hangManExerciseDelimitedArea)))
                .andExpect(status().isOk());

        // Validate the HangManExerciseDelimitedArea in the database
        List<HangManExerciseDelimitedArea> hangManExerciseDelimitedAreas = hangManExerciseDelimitedAreaRepository.findAll();
        assertThat(hangManExerciseDelimitedAreas).hasSize(databaseSizeBeforeUpdate);
        HangManExerciseDelimitedArea testHangManExerciseDelimitedArea = hangManExerciseDelimitedAreas.get(hangManExerciseDelimitedAreas.size() - 1);
        assertThat(testHangManExerciseDelimitedArea.getX()).isEqualTo(UPDATED_X);
        assertThat(testHangManExerciseDelimitedArea.getY()).isEqualTo(UPDATED_Y);
        assertThat(testHangManExerciseDelimitedArea.getRadius()).isEqualTo(UPDATED_RADIUS);
    }

    @Test
    @Transactional
    public void deleteHangManExerciseDelimitedArea() throws Exception {
        // Initialize the database
        hangManExerciseDelimitedAreaRepository.saveAndFlush(hangManExerciseDelimitedArea);

		int databaseSizeBeforeDelete = hangManExerciseDelimitedAreaRepository.findAll().size();

        // Get the hangManExerciseDelimitedArea
        restHangManExerciseDelimitedAreaMockMvc.perform(delete("/api/hangManExerciseDelimitedAreas/{id}", hangManExerciseDelimitedArea.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<HangManExerciseDelimitedArea> hangManExerciseDelimitedAreas = hangManExerciseDelimitedAreaRepository.findAll();
        assertThat(hangManExerciseDelimitedAreas).hasSize(databaseSizeBeforeDelete - 1);
    }
}
