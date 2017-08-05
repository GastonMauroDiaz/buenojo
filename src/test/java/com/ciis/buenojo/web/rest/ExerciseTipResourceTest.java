package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.ExerciseTip;
import com.ciis.buenojo.repository.ExerciseTipRepository;

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
 * Test class for the ExerciseTipResource REST controller.
 *
 * @see ExerciseTipResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ExerciseTipResourceTest {

   
    private static final String DEFAULT_TIP_DETAIL = "AAAAA";
    private static final String UPDATED_TIP_DETAIL = "BBBBB";

    @Inject
    private ExerciseTipRepository exerciseTipRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restExerciseTipMockMvc;

    private ExerciseTip exerciseTip;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExerciseTipResource exerciseTipResource = new ExerciseTipResource();
        ReflectionTestUtils.setField(exerciseTipResource, "exerciseTipRepository", exerciseTipRepository);
        this.restExerciseTipMockMvc = MockMvcBuilders.standaloneSetup(exerciseTipResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        exerciseTip = new ExerciseTip();
        
        exerciseTip.setDetail(DEFAULT_TIP_DETAIL);
    }

    @Test
    @Transactional
    public void createExerciseTip() throws Exception {
        int databaseSizeBeforeCreate = exerciseTipRepository.findAll().size();

        // Create the ExerciseTip

        restExerciseTipMockMvc.perform(post("/api/exerciseTips")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(exerciseTip)))
                .andExpect(status().isCreated());

        // Validate the ExerciseTip in the database
        List<ExerciseTip> exerciseTips = exerciseTipRepository.findAll();
        assertThat(exerciseTips).hasSize(databaseSizeBeforeCreate + 1);
        ExerciseTip testExerciseTip = exerciseTips.get(exerciseTips.size() - 1);
        
        assertThat(testExerciseTip.getDetail()).isEqualTo(DEFAULT_TIP_DETAIL);
    }

    @Test
    @Transactional
    public void checkHintIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseTipRepository.findAll().size();
        // Create the ExerciseTip, which fails.

        restExerciseTipMockMvc.perform(post("/api/exerciseTips")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(exerciseTip)))
                .andExpect(status().isBadRequest());

        List<ExerciseTip> exerciseTips = exerciseTipRepository.findAll();
        assertThat(exerciseTips).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExerciseTips() throws Exception {
        // Initialize the database
        exerciseTipRepository.saveAndFlush(exerciseTip);

        // Get all the exerciseTips
        restExerciseTipMockMvc.perform(get("/api/exerciseTips"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(exerciseTip.getId().intValue())))
                .andExpect(jsonPath("$.[*].tipDetail").value(hasItem(DEFAULT_TIP_DETAIL.toString())));
    }

    @Test
    @Transactional
    public void getExerciseTip() throws Exception {
        // Initialize the database
        exerciseTipRepository.saveAndFlush(exerciseTip);

        // Get the exerciseTip
        restExerciseTipMockMvc.perform(get("/api/exerciseTips/{id}", exerciseTip.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(exerciseTip.getId().intValue()))
            
            .andExpect(jsonPath("$.detail").value(DEFAULT_TIP_DETAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExerciseTip() throws Exception {
        // Get the exerciseTip
        restExerciseTipMockMvc.perform(get("/api/exerciseTips/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExerciseTip() throws Exception {
        // Initialize the database
        exerciseTipRepository.saveAndFlush(exerciseTip);

		int databaseSizeBeforeUpdate = exerciseTipRepository.findAll().size();

        // Update the exerciseTip
        
        exerciseTip.setDetail(UPDATED_TIP_DETAIL);

        restExerciseTipMockMvc.perform(put("/api/exerciseTips")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(exerciseTip)))
                .andExpect(status().isOk());

        // Validate the ExerciseTip in the database
        List<ExerciseTip> exerciseTips = exerciseTipRepository.findAll();
        assertThat(exerciseTips).hasSize(databaseSizeBeforeUpdate);
        ExerciseTip testExerciseTip = exerciseTips.get(exerciseTips.size() - 1);
        
        assertThat(testExerciseTip.getDetail()).isEqualTo(UPDATED_TIP_DETAIL);
    }

    @Test
    @Transactional
    public void deleteExerciseTip() throws Exception {
        // Initialize the database
        exerciseTipRepository.saveAndFlush(exerciseTip);

		int databaseSizeBeforeDelete = exerciseTipRepository.findAll().size();

        // Get the exerciseTip
        restExerciseTipMockMvc.perform(delete("/api/exerciseTips/{id}", exerciseTip.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ExerciseTip> exerciseTips = exerciseTipRepository.findAll();
        assertThat(exerciseTips).hasSize(databaseSizeBeforeDelete - 1);
    }
}
