package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.CurrentSession;
import com.ciis.buenojo.repository.CurrentSessionRepository;

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
 * Test class for the CurrentSessionResource REST controller.
 *
 * @see CurrentSessionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CurrentSessionResourceIntTest {


    @Inject
    private CurrentSessionRepository currentSessionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCurrentSessionMockMvc;

    private CurrentSession currentSession;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CurrentSessionResource currentSessionResource = new CurrentSessionResource();
        ReflectionTestUtils.setField(currentSessionResource, "currentSessionRepository", currentSessionRepository);
        this.restCurrentSessionMockMvc = MockMvcBuilders.standaloneSetup(currentSessionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        currentSession = new CurrentSession();
    }

    @Test
    @Transactional
    public void createCurrentSession() throws Exception {
        int databaseSizeBeforeCreate = currentSessionRepository.findAll().size();

        // Create the CurrentSession

        restCurrentSessionMockMvc.perform(post("/api/currentSessions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(currentSession)))
                .andExpect(status().isCreated());

        // Validate the CurrentSession in the database
        List<CurrentSession> currentSessions = currentSessionRepository.findAll();
        assertThat(currentSessions).hasSize(databaseSizeBeforeCreate + 1);
        CurrentSession testCurrentSession = currentSessions.get(currentSessions.size() - 1);
    }

    @Test
    @Transactional
    public void getAllCurrentSessions() throws Exception {
        // Initialize the database
        currentSessionRepository.saveAndFlush(currentSession);

        // Get all the currentSessions
        restCurrentSessionMockMvc.perform(get("/api/currentSessions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(currentSession.getId().intValue())));
    }

    @Test
    @Transactional
    public void getCurrentSession() throws Exception {
        // Initialize the database
        currentSessionRepository.saveAndFlush(currentSession);

        // Get the currentSession
        restCurrentSessionMockMvc.perform(get("/api/currentSessions/{id}", currentSession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(currentSession.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCurrentSession() throws Exception {
        // Get the currentSession
        restCurrentSessionMockMvc.perform(get("/api/currentSessions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCurrentSession() throws Exception {
        // Initialize the database
        currentSessionRepository.saveAndFlush(currentSession);

		int databaseSizeBeforeUpdate = currentSessionRepository.findAll().size();

        // Update the currentSession

        restCurrentSessionMockMvc.perform(put("/api/currentSessions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(currentSession)))
                .andExpect(status().isOk());

        // Validate the CurrentSession in the database
        List<CurrentSession> currentSessions = currentSessionRepository.findAll();
        assertThat(currentSessions).hasSize(databaseSizeBeforeUpdate);
        CurrentSession testCurrentSession = currentSessions.get(currentSessions.size() - 1);
    }

    @Test
    @Transactional
    public void deleteCurrentSession() throws Exception {
        // Initialize the database
        currentSessionRepository.saveAndFlush(currentSession);

		int databaseSizeBeforeDelete = currentSessionRepository.findAll().size();

        // Get the currentSession
        restCurrentSessionMockMvc.perform(delete("/api/currentSessions/{id}", currentSession.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CurrentSession> currentSessions = currentSessionRepository.findAll();
        assertThat(currentSessions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
