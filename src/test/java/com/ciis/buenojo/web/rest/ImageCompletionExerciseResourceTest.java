package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.ImageCompletionExercise;
import com.ciis.buenojo.repository.ImageCompletionExerciseRepository;

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
 * Test class for the ImageCompletionExerciseResource REST controller.
 *
 * @see ImageCompletionExerciseResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ImageCompletionExerciseResourceTest {


    @Inject
    private ImageCompletionExerciseRepository imageCompletionExerciseRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restImageCompletionExerciseMockMvc;

    private ImageCompletionExercise imageCompletionExercise;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ImageCompletionExerciseResource imageCompletionExerciseResource = new ImageCompletionExerciseResource();
        ReflectionTestUtils.setField(imageCompletionExerciseResource, "imageCompletionExerciseRepository", imageCompletionExerciseRepository);
        this.restImageCompletionExerciseMockMvc = MockMvcBuilders.standaloneSetup(imageCompletionExerciseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        imageCompletionExercise = new ImageCompletionExercise();
    }

    @Test
    @Transactional
    public void createImageCompletionExercise() throws Exception {
        int databaseSizeBeforeCreate = imageCompletionExerciseRepository.findAll().size();

        // Create the ImageCompletionExercise

        restImageCompletionExerciseMockMvc.perform(post("/api/imageCompletionExercises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(imageCompletionExercise)))
                .andExpect(status().isCreated());

        // Validate the ImageCompletionExercise in the database
        List<ImageCompletionExercise> imageCompletionExercises = imageCompletionExerciseRepository.findAll();
        assertThat(imageCompletionExercises).hasSize(databaseSizeBeforeCreate + 1);
        ImageCompletionExercise testImageCompletionExercise = imageCompletionExercises.get(imageCompletionExercises.size() - 1);
    }

    @Test
    @Transactional
    public void getAllImageCompletionExercises() throws Exception {
        // Initialize the database
        imageCompletionExerciseRepository.saveAndFlush(imageCompletionExercise);

        // Get all the imageCompletionExercises
        restImageCompletionExerciseMockMvc.perform(get("/api/imageCompletionExercises"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(imageCompletionExercise.getId().intValue())));
    }

    @Test
    @Transactional
    public void getImageCompletionExercise() throws Exception {
        // Initialize the database
        imageCompletionExerciseRepository.saveAndFlush(imageCompletionExercise);

        // Get the imageCompletionExercise
        restImageCompletionExerciseMockMvc.perform(get("/api/imageCompletionExercises/{id}", imageCompletionExercise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(imageCompletionExercise.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingImageCompletionExercise() throws Exception {
        // Get the imageCompletionExercise
        restImageCompletionExerciseMockMvc.perform(get("/api/imageCompletionExercises/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateImageCompletionExercise() throws Exception {
        // Initialize the database
        imageCompletionExerciseRepository.saveAndFlush(imageCompletionExercise);

		int databaseSizeBeforeUpdate = imageCompletionExerciseRepository.findAll().size();

        // Update the imageCompletionExercise

        restImageCompletionExerciseMockMvc.perform(put("/api/imageCompletionExercises")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(imageCompletionExercise)))
                .andExpect(status().isOk());

        // Validate the ImageCompletionExercise in the database
        List<ImageCompletionExercise> imageCompletionExercises = imageCompletionExerciseRepository.findAll();
        assertThat(imageCompletionExercises).hasSize(databaseSizeBeforeUpdate);
        ImageCompletionExercise testImageCompletionExercise = imageCompletionExercises.get(imageCompletionExercises.size() - 1);
    }

    @Test
    @Transactional
    public void deleteImageCompletionExercise() throws Exception {
        // Initialize the database
        imageCompletionExerciseRepository.saveAndFlush(imageCompletionExercise);

		int databaseSizeBeforeDelete = imageCompletionExerciseRepository.findAll().size();

        // Get the imageCompletionExercise
        restImageCompletionExerciseMockMvc.perform(delete("/api/imageCompletionExercises/{id}", imageCompletionExercise.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ImageCompletionExercise> imageCompletionExercises = imageCompletionExerciseRepository.findAll();
        assertThat(imageCompletionExercises).hasSize(databaseSizeBeforeDelete - 1);
    }
}
