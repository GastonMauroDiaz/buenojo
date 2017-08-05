package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.Level;
import com.ciis.buenojo.repository.LevelRepository;

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
 * Test class for the LevelResource REST controller.
 *
 * @see LevelResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class LevelResourceIntTest {


    
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private LevelRepository levelRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLevelMockMvc;

    private Level level;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LevelResource levelResource = new LevelResource();
        ReflectionTestUtils.setField(levelResource, "levelRepository", levelRepository);
        this.restLevelMockMvc = MockMvcBuilders.standaloneSetup(levelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        level = new Level();
        level.setName(DEFAULT_NAME);
        level.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createLevel() throws Exception {
        int databaseSizeBeforeCreate = levelRepository.findAll().size();

        // Create the Level

        restLevelMockMvc.perform(post("/api/levels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(level)))
                .andExpect(status().isCreated());

        // Validate the Level in the database
        List<Level> levels = levelRepository.findAll();
        assertThat(levels).hasSize(databaseSizeBeforeCreate + 1);
        Level testLevel = levels.get(levels.size() - 1);
        assertThat(testLevel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLevel.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = levelRepository.findAll().size();
        // set the field null
        level.setName(null);

        // Create the Level, which fails.

        restLevelMockMvc.perform(post("/api/levels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(level)))
                .andExpect(status().isBadRequest());

        List<Level> levels = levelRepository.findAll();
        assertThat(levels).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLevels() throws Exception {
        // Initialize the database
        levelRepository.saveAndFlush(level);

        // Get all the levels
        restLevelMockMvc.perform(get("/api/levels"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(level.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getLevel() throws Exception {
        // Initialize the database
        levelRepository.saveAndFlush(level);

        // Get the level
        restLevelMockMvc.perform(get("/api/levels/{id}", level.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(level.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLevel() throws Exception {
        // Get the level
        restLevelMockMvc.perform(get("/api/levels/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLevel() throws Exception {
        // Initialize the database
        levelRepository.saveAndFlush(level);

		int databaseSizeBeforeUpdate = levelRepository.findAll().size();

        // Update the level
        level.setName(UPDATED_NAME);
        level.setDescription(UPDATED_DESCRIPTION);

        restLevelMockMvc.perform(put("/api/levels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(level)))
                .andExpect(status().isOk());

        // Validate the Level in the database
        List<Level> levels = levelRepository.findAll();
        assertThat(levels).hasSize(databaseSizeBeforeUpdate);
        Level testLevel = levels.get(levels.size() - 1);
        assertThat(testLevel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLevel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteLevel() throws Exception {
        // Initialize the database
        levelRepository.saveAndFlush(level);

		int databaseSizeBeforeDelete = levelRepository.findAll().size();

        // Get the level
        restLevelMockMvc.perform(delete("/api/levels/{id}", level.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Level> levels = levelRepository.findAll();
        assertThat(levels).hasSize(databaseSizeBeforeDelete - 1);
    }
}
