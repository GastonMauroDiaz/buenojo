package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.MultipleChoiceExerciseContainer;
import com.ciis.buenojo.repository.MultipleChoiceExerciseContainerRepository;

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
 * Test class for the MultipleChoiceExerciseContainerResource REST controller.
 *
 * @see MultipleChoiceExerciseContainerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MultipleChoiceExerciseContainerResourceIntTest {

    private static final String DEFAULT_NAME = "";
    private static final String UPDATED_NAME = "";

    @Inject
    private MultipleChoiceExerciseContainerRepository multipleChoiceExerciseContainerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMultipleChoiceExerciseContainerMockMvc;

    private MultipleChoiceExerciseContainer multipleChoiceExerciseContainer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MultipleChoiceExerciseContainerResource multipleChoiceExerciseContainerResource = new MultipleChoiceExerciseContainerResource();
        ReflectionTestUtils.setField(multipleChoiceExerciseContainerResource, "multipleChoiceExerciseContainerRepository", multipleChoiceExerciseContainerRepository);
        this.restMultipleChoiceExerciseContainerMockMvc = MockMvcBuilders.standaloneSetup(multipleChoiceExerciseContainerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        multipleChoiceExerciseContainer = new MultipleChoiceExerciseContainer();
        multipleChoiceExerciseContainer.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createMultipleChoiceExerciseContainer() throws Exception {
        int databaseSizeBeforeCreate = multipleChoiceExerciseContainerRepository.findAll().size();

        // Create the MultipleChoiceExerciseContainer

        restMultipleChoiceExerciseContainerMockMvc.perform(post("/api/multipleChoiceExerciseContainers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(multipleChoiceExerciseContainer)))
                .andExpect(status().isCreated());

        // Validate the MultipleChoiceExerciseContainer in the database
        List<MultipleChoiceExerciseContainer> multipleChoiceExerciseContainers = multipleChoiceExerciseContainerRepository.findAll();
        assertThat(multipleChoiceExerciseContainers).hasSize(databaseSizeBeforeCreate + 1);
        MultipleChoiceExerciseContainer testMultipleChoiceExerciseContainer = multipleChoiceExerciseContainers.get(multipleChoiceExerciseContainers.size() - 1);
        assertThat(testMultipleChoiceExerciseContainer.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = multipleChoiceExerciseContainerRepository.findAll().size();
        // set the field null
        multipleChoiceExerciseContainer.setName(null);

        // Create the MultipleChoiceExerciseContainer, which fails.

        restMultipleChoiceExerciseContainerMockMvc.perform(post("/api/multipleChoiceExerciseContainers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(multipleChoiceExerciseContainer)))
                .andExpect(status().isBadRequest());

        List<MultipleChoiceExerciseContainer> multipleChoiceExerciseContainers = multipleChoiceExerciseContainerRepository.findAll();
        assertThat(multipleChoiceExerciseContainers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMultipleChoiceExerciseContainers() throws Exception {
        // Initialize the database
        multipleChoiceExerciseContainerRepository.saveAndFlush(multipleChoiceExerciseContainer);

        // Get all the multipleChoiceExerciseContainers
        restMultipleChoiceExerciseContainerMockMvc.perform(get("/api/multipleChoiceExerciseContainers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(multipleChoiceExerciseContainer.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getMultipleChoiceExerciseContainer() throws Exception {
        // Initialize the database
        multipleChoiceExerciseContainerRepository.saveAndFlush(multipleChoiceExerciseContainer);

        // Get the multipleChoiceExerciseContainer
        restMultipleChoiceExerciseContainerMockMvc.perform(get("/api/multipleChoiceExerciseContainers/{id}", multipleChoiceExerciseContainer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(multipleChoiceExerciseContainer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMultipleChoiceExerciseContainer() throws Exception {
        // Get the multipleChoiceExerciseContainer
        restMultipleChoiceExerciseContainerMockMvc.perform(get("/api/multipleChoiceExerciseContainers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMultipleChoiceExerciseContainer() throws Exception {
        // Initialize the database
        multipleChoiceExerciseContainerRepository.saveAndFlush(multipleChoiceExerciseContainer);

		int databaseSizeBeforeUpdate = multipleChoiceExerciseContainerRepository.findAll().size();

        // Update the multipleChoiceExerciseContainer
        multipleChoiceExerciseContainer.setName(UPDATED_NAME);

        restMultipleChoiceExerciseContainerMockMvc.perform(put("/api/multipleChoiceExerciseContainers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(multipleChoiceExerciseContainer)))
                .andExpect(status().isOk());

        // Validate the MultipleChoiceExerciseContainer in the database
        List<MultipleChoiceExerciseContainer> multipleChoiceExerciseContainers = multipleChoiceExerciseContainerRepository.findAll();
        assertThat(multipleChoiceExerciseContainers).hasSize(databaseSizeBeforeUpdate);
        MultipleChoiceExerciseContainer testMultipleChoiceExerciseContainer = multipleChoiceExerciseContainers.get(multipleChoiceExerciseContainers.size() - 1);
        assertThat(testMultipleChoiceExerciseContainer.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteMultipleChoiceExerciseContainer() throws Exception {
        // Initialize the database
        multipleChoiceExerciseContainerRepository.saveAndFlush(multipleChoiceExerciseContainer);

		int databaseSizeBeforeDelete = multipleChoiceExerciseContainerRepository.findAll().size();

        // Get the multipleChoiceExerciseContainer
        restMultipleChoiceExerciseContainerMockMvc.perform(delete("/api/multipleChoiceExerciseContainers/{id}", multipleChoiceExerciseContainer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<MultipleChoiceExerciseContainer> multipleChoiceExerciseContainers = multipleChoiceExerciseContainerRepository.findAll();
        assertThat(multipleChoiceExerciseContainers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
