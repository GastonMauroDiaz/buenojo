package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.CourseLevelSession;
import com.ciis.buenojo.repository.CourseLevelSessionRepository;

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

import com.ciis.buenojo.domain.enumeration.CourseLevelStatus;

/**
 * Test class for the CourseLevelSessionResource REST controller.
 *
 * @see CourseLevelSessionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CourseLevelSessionResourceIntTest {



private static final CourseLevelStatus DEFAULT_STATUS = CourseLevelStatus.NotStarted;
    private static final CourseLevelStatus UPDATED_STATUS = CourseLevelStatus.InProgress;

    private static final Double DEFAULT_PERCENTAGE = 0D;
    private static final Double UPDATED_PERCENTAGE = 1D;

    private static final Double DEFAULT_EXPERIENCE_POINTS = 0D;
    private static final Double UPDATED_EXPERIENCE_POINTS = 1D;

    private static final Integer DEFAULT_EXERCISE_COMPLETED_COUNT = 0;
    private static final Integer UPDATED_EXERCISE_COMPLETED_COUNT = 1;

    private static final Long DEFAULT_ID = 0L;
    @Inject
    private CourseLevelSessionRepository courseLevelSessionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCourseLevelSessionMockMvc;

    private CourseLevelSession courseLevelSession;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CourseLevelSessionResource courseLevelSessionResource = new CourseLevelSessionResource();
        ReflectionTestUtils.setField(courseLevelSessionResource, "courseLevelSessionRepository", courseLevelSessionRepository);
        this.restCourseLevelSessionMockMvc = MockMvcBuilders.standaloneSetup(courseLevelSessionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        courseLevelSession = new CourseLevelSession();
        courseLevelSession.setStatus(DEFAULT_STATUS);
        courseLevelSession.setPercentage(DEFAULT_PERCENTAGE);
        courseLevelSession.setExperiencePoints(DEFAULT_EXPERIENCE_POINTS);
        courseLevelSession.setExerciseCompletedCount(DEFAULT_EXERCISE_COMPLETED_COUNT);
        courseLevelSession.setApprovedExercises(0);
    }

    @Test
    @Transactional
    public void createCourseLevelSession() throws Exception {
        int databaseSizeBeforeCreate = courseLevelSessionRepository.findAll().size();

        // Create the CourseLevelSession

        restCourseLevelSessionMockMvc.perform(post("/api/courseLevelSessions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(courseLevelSession)))
                .andExpect(status().isCreated());

        // Validate the CourseLevelSession in the database
        List<CourseLevelSession> courseLevelSessions = courseLevelSessionRepository.findAll();
        assertThat(courseLevelSessions).hasSize(databaseSizeBeforeCreate + 1);
        CourseLevelSession testCourseLevelSession = courseLevelSessions.get(courseLevelSessions.size() - 1);
        assertThat(testCourseLevelSession.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testCourseLevelSession.getPercentage()).isEqualTo(DEFAULT_PERCENTAGE);
        assertThat(testCourseLevelSession.getExperiencePoints()).isEqualTo(DEFAULT_EXPERIENCE_POINTS);
        assertThat(testCourseLevelSession.getExerciseCompletedCount()).isEqualTo(DEFAULT_EXERCISE_COMPLETED_COUNT);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseLevelSessionRepository.findAll().size();
        // set the field null
        courseLevelSession.setStatus(null);

        // Create the CourseLevelSession, which fails.

        restCourseLevelSessionMockMvc.perform(post("/api/courseLevelSessions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(courseLevelSession)))
                .andExpect(status().isBadRequest());

        List<CourseLevelSession> courseLevelSessions = courseLevelSessionRepository.findAll();
        assertThat(courseLevelSessions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPercentageIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseLevelSessionRepository.findAll().size();
        // set the field null
        courseLevelSession.setPercentage(null);

        // Create the CourseLevelSession, which fails.

        restCourseLevelSessionMockMvc.perform(post("/api/courseLevelSessions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(courseLevelSession)))
                .andExpect(status().isBadRequest());

        List<CourseLevelSession> courseLevelSessions = courseLevelSessionRepository.findAll();
        assertThat(courseLevelSessions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExperiencePointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseLevelSessionRepository.findAll().size();
        // set the field null
        courseLevelSession.setExperiencePoints(null);

        // Create the CourseLevelSession, which fails.

        restCourseLevelSessionMockMvc.perform(post("/api/courseLevelSessions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(courseLevelSession)))
                .andExpect(status().isBadRequest());

        List<CourseLevelSession> courseLevelSessions = courseLevelSessionRepository.findAll();
        assertThat(courseLevelSessions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExerciseCompletedCountIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseLevelSessionRepository.findAll().size();
        // set the field null
        courseLevelSession.setExerciseCompletedCount(null);

        // Create the CourseLevelSession, which fails.

        restCourseLevelSessionMockMvc.perform(post("/api/courseLevelSessions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(courseLevelSession)))
                .andExpect(status().isBadRequest());

        List<CourseLevelSession> courseLevelSessions = courseLevelSessionRepository.findAll();
        assertThat(courseLevelSessions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCourseLevelSessions() throws Exception {
        // Initialize the database
        courseLevelSessionRepository.saveAndFlush(courseLevelSession);

        // Get all the courseLevelSessions
        restCourseLevelSessionMockMvc.perform(get("/api/courseLevelSessions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(courseLevelSession.getId().intValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].percentage").value(hasItem(DEFAULT_PERCENTAGE.doubleValue())))
                .andExpect(jsonPath("$.[*].experiencePoints").value(hasItem(DEFAULT_EXPERIENCE_POINTS.doubleValue())))
                .andExpect(jsonPath("$.[*].exerciseCompletedCount").value(hasItem(DEFAULT_EXERCISE_COMPLETED_COUNT)));
    }

    @Test
    @Transactional
    public void getCourseLevelSession() throws Exception {
        // Initialize the database
        courseLevelSessionRepository.saveAndFlush(courseLevelSession);

        // Get the courseLevelSession
        restCourseLevelSessionMockMvc.perform(get("/api/courseLevelSessions/{id}", courseLevelSession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(courseLevelSession.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.percentage").value(DEFAULT_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.experiencePoints").value(DEFAULT_EXPERIENCE_POINTS.doubleValue()))
            .andExpect(jsonPath("$.exerciseCompletedCount").value(DEFAULT_EXERCISE_COMPLETED_COUNT));
    }

    @Test
    @Transactional
    public void getNonExistingCourseLevelSession() throws Exception {
        // Get the courseLevelSession
        restCourseLevelSessionMockMvc.perform(get("/api/courseLevelSessions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourseLevelSession() throws Exception {
        // Initialize the database
        courseLevelSessionRepository.saveAndFlush(courseLevelSession);

		int databaseSizeBeforeUpdate = courseLevelSessionRepository.findAll().size();

        // Update the courseLevelSession
        courseLevelSession.setStatus(UPDATED_STATUS);
        courseLevelSession.setPercentage(UPDATED_PERCENTAGE);
        courseLevelSession.setExperiencePoints(UPDATED_EXPERIENCE_POINTS);
        courseLevelSession.setExerciseCompletedCount(UPDATED_EXERCISE_COMPLETED_COUNT);

        restCourseLevelSessionMockMvc.perform(put("/api/courseLevelSessions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(courseLevelSession)))
                .andExpect(status().isOk());

        // Validate the CourseLevelSession in the database
        List<CourseLevelSession> courseLevelSessions = courseLevelSessionRepository.findAll();
        assertThat(courseLevelSessions).hasSize(databaseSizeBeforeUpdate);
        CourseLevelSession testCourseLevelSession = courseLevelSessions.get(courseLevelSessions.size() - 1);
        assertThat(testCourseLevelSession.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testCourseLevelSession.getPercentage()).isEqualTo(UPDATED_PERCENTAGE);
        assertThat(testCourseLevelSession.getExperiencePoints()).isEqualTo(UPDATED_EXPERIENCE_POINTS);
        assertThat(testCourseLevelSession.getExerciseCompletedCount()).isEqualTo(UPDATED_EXERCISE_COMPLETED_COUNT);
    }

    @Test
    @Transactional
    public void deleteCourseLevelSession() throws Exception {
        // Initialize the database
        courseLevelSessionRepository.saveAndFlush(courseLevelSession);

		int databaseSizeBeforeDelete = courseLevelSessionRepository.findAll().size();

        // Get the courseLevelSession
        restCourseLevelSessionMockMvc.perform(delete("/api/courseLevelSessions/{id}", courseLevelSession.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CourseLevelSession> courseLevelSessions = courseLevelSessionRepository.findAll();
        assertThat(courseLevelSessions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
