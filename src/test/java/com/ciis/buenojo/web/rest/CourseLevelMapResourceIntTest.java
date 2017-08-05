package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.CourseLevelMap;
import com.ciis.buenojo.repository.CourseLevelMapRepository;

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
 * Test class for the CourseLevelMapResource REST controller.
 *
 * @see CourseLevelMapResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CourseLevelMapResourceIntTest {


    @Inject
    private CourseLevelMapRepository courseLevelMapRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCourseLevelMapMockMvc;

    private CourseLevelMap courseLevelMap;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CourseLevelMapResource courseLevelMapResource = new CourseLevelMapResource();
        ReflectionTestUtils.setField(courseLevelMapResource, "courseLevelMapRepository", courseLevelMapRepository);
        this.restCourseLevelMapMockMvc = MockMvcBuilders.standaloneSetup(courseLevelMapResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        courseLevelMap = new CourseLevelMap();
    }

    @Test
    @Transactional
    public void createCourseLevelMap() throws Exception {
        int databaseSizeBeforeCreate = courseLevelMapRepository.findAll().size();

        // Create the CourseLevelMap

        restCourseLevelMapMockMvc.perform(post("/api/courseLevelMaps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(courseLevelMap)))
                .andExpect(status().isCreated());

        // Validate the CourseLevelMap in the database
        List<CourseLevelMap> courseLevelMaps = courseLevelMapRepository.findAll();
        assertThat(courseLevelMaps).hasSize(databaseSizeBeforeCreate + 1);
        CourseLevelMap testCourseLevelMap = courseLevelMaps.get(courseLevelMaps.size() - 1);
    }

    @Test
    @Transactional
    public void getAllCourseLevelMaps() throws Exception {
        // Initialize the database
        courseLevelMapRepository.saveAndFlush(courseLevelMap);

        // Get all the courseLevelMaps
        restCourseLevelMapMockMvc.perform(get("/api/courseLevelMaps"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(courseLevelMap.getId().intValue())));
    }

    @Test
    @Transactional
    public void getCourseLevelMap() throws Exception {
        // Initialize the database
        courseLevelMapRepository.saveAndFlush(courseLevelMap);

        // Get the courseLevelMap
        restCourseLevelMapMockMvc.perform(get("/api/courseLevelMaps/{id}", courseLevelMap.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(courseLevelMap.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCourseLevelMap() throws Exception {
        // Get the courseLevelMap
        restCourseLevelMapMockMvc.perform(get("/api/courseLevelMaps/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourseLevelMap() throws Exception {
        // Initialize the database
        courseLevelMapRepository.saveAndFlush(courseLevelMap);

		int databaseSizeBeforeUpdate = courseLevelMapRepository.findAll().size();

        // Update the courseLevelMap

        restCourseLevelMapMockMvc.perform(put("/api/courseLevelMaps")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(courseLevelMap)))
                .andExpect(status().isOk());

        // Validate the CourseLevelMap in the database
        List<CourseLevelMap> courseLevelMaps = courseLevelMapRepository.findAll();
        assertThat(courseLevelMaps).hasSize(databaseSizeBeforeUpdate);
        CourseLevelMap testCourseLevelMap = courseLevelMaps.get(courseLevelMaps.size() - 1);
    }

    @Test
    @Transactional
    public void deleteCourseLevelMap() throws Exception {
        // Initialize the database
        courseLevelMapRepository.saveAndFlush(courseLevelMap);

		int databaseSizeBeforeDelete = courseLevelMapRepository.findAll().size();

        // Get the courseLevelMap
        restCourseLevelMapMockMvc.perform(delete("/api/courseLevelMaps/{id}", courseLevelMap.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CourseLevelMap> courseLevelMaps = courseLevelMapRepository.findAll();
        assertThat(courseLevelMaps).hasSize(databaseSizeBeforeDelete - 1);
    }
}
