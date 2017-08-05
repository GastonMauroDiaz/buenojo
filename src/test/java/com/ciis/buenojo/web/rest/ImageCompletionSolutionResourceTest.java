package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.ImageCompletionSolution;
import com.ciis.buenojo.repository.ImageCompletionSolutionRepository;

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
 * Test class for the ImageCompletionSolutionResource REST controller.
 *
 * @see ImageCompletionSolutionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ImageCompletionSolutionResourceTest {


    @Inject
    private ImageCompletionSolutionRepository imageCompletionSolutionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restImageCompletionSolutionMockMvc;

    private ImageCompletionSolution imageCompletionSolution;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ImageCompletionSolutionResource imageCompletionSolutionResource = new ImageCompletionSolutionResource();
        ReflectionTestUtils.setField(imageCompletionSolutionResource, "imageCompletionSolutionRepository", imageCompletionSolutionRepository);
        this.restImageCompletionSolutionMockMvc = MockMvcBuilders.standaloneSetup(imageCompletionSolutionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        imageCompletionSolution = new ImageCompletionSolution();
    }

    @Test
    @Transactional
    public void createImageCompletionSolution() throws Exception {
        int databaseSizeBeforeCreate = imageCompletionSolutionRepository.findAll().size();

        // Create the ImageCompletionSolution

        restImageCompletionSolutionMockMvc.perform(post("/api/imageCompletionSolutions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(imageCompletionSolution)))
                .andExpect(status().isCreated());

        // Validate the ImageCompletionSolution in the database
        List<ImageCompletionSolution> imageCompletionSolutions = imageCompletionSolutionRepository.findAll();
        assertThat(imageCompletionSolutions).hasSize(databaseSizeBeforeCreate + 1);
        ImageCompletionSolution testImageCompletionSolution = imageCompletionSolutions.get(imageCompletionSolutions.size() - 1);
    }

    @Test
    @Transactional
    public void getAllImageCompletionSolutions() throws Exception {
        // Initialize the database
        imageCompletionSolutionRepository.saveAndFlush(imageCompletionSolution);

        // Get all the imageCompletionSolutions
        restImageCompletionSolutionMockMvc.perform(get("/api/imageCompletionSolutions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(imageCompletionSolution.getId().intValue())));
    }

    @Test
    @Transactional
    public void getImageCompletionSolution() throws Exception {
        // Initialize the database
        imageCompletionSolutionRepository.saveAndFlush(imageCompletionSolution);

        // Get the imageCompletionSolution
        restImageCompletionSolutionMockMvc.perform(get("/api/imageCompletionSolutions/{id}", imageCompletionSolution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(imageCompletionSolution.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingImageCompletionSolution() throws Exception {
        // Get the imageCompletionSolution
        restImageCompletionSolutionMockMvc.perform(get("/api/imageCompletionSolutions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateImageCompletionSolution() throws Exception {
        // Initialize the database
        imageCompletionSolutionRepository.saveAndFlush(imageCompletionSolution);

		int databaseSizeBeforeUpdate = imageCompletionSolutionRepository.findAll().size();

        // Update the imageCompletionSolution

        restImageCompletionSolutionMockMvc.perform(put("/api/imageCompletionSolutions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(imageCompletionSolution)))
                .andExpect(status().isOk());

        // Validate the ImageCompletionSolution in the database
        List<ImageCompletionSolution> imageCompletionSolutions = imageCompletionSolutionRepository.findAll();
        assertThat(imageCompletionSolutions).hasSize(databaseSizeBeforeUpdate);
        ImageCompletionSolution testImageCompletionSolution = imageCompletionSolutions.get(imageCompletionSolutions.size() - 1);
    }

    @Test
    @Transactional
    public void deleteImageCompletionSolution() throws Exception {
        // Initialize the database
        imageCompletionSolutionRepository.saveAndFlush(imageCompletionSolution);

		int databaseSizeBeforeDelete = imageCompletionSolutionRepository.findAll().size();

        // Get the imageCompletionSolution
        restImageCompletionSolutionMockMvc.perform(delete("/api/imageCompletionSolutions/{id}", imageCompletionSolution.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ImageCompletionSolution> imageCompletionSolutions = imageCompletionSolutionRepository.findAll();
        assertThat(imageCompletionSolutions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
