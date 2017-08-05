package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.PhotoLocationImage;
import com.ciis.buenojo.repository.PhotoLocationImageRepository;

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
 * Test class for the PhotoLocationImageResource REST controller.
 *
 * @see PhotoLocationImageResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PhotoLocationImageResourceTest {


    @Inject
    private PhotoLocationImageRepository photoLocationImageRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPhotoLocationImageMockMvc;

    private PhotoLocationImage photoLocationImage;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PhotoLocationImageResource photoLocationImageResource = new PhotoLocationImageResource();
        ReflectionTestUtils.setField(photoLocationImageResource, "photoLocationImageRepository", photoLocationImageRepository);
        this.restPhotoLocationImageMockMvc = MockMvcBuilders.standaloneSetup(photoLocationImageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        photoLocationImage = new PhotoLocationImage();
    }

    @Test
    @Transactional
    public void createPhotoLocationImage() throws Exception {
        int databaseSizeBeforeCreate = photoLocationImageRepository.findAll().size();

        // Create the PhotoLocationImage

        restPhotoLocationImageMockMvc.perform(post("/api/photoLocationImages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationImage)))
                .andExpect(status().isCreated());

        // Validate the PhotoLocationImage in the database
        List<PhotoLocationImage> photoLocationImages = photoLocationImageRepository.findAll();
        assertThat(photoLocationImages).hasSize(databaseSizeBeforeCreate + 1);
        PhotoLocationImage testPhotoLocationImage = photoLocationImages.get(photoLocationImages.size() - 1);
    }

    @Test
    @Transactional
    public void getAllPhotoLocationImages() throws Exception {
        // Initialize the database
        photoLocationImageRepository.saveAndFlush(photoLocationImage);

        // Get all the photoLocationImages
        restPhotoLocationImageMockMvc.perform(get("/api/photoLocationImages"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(photoLocationImage.getId().intValue())));
    }

    @Test
    @Transactional
    public void getPhotoLocationImage() throws Exception {
        // Initialize the database
        photoLocationImageRepository.saveAndFlush(photoLocationImage);

        // Get the photoLocationImage
        restPhotoLocationImageMockMvc.perform(get("/api/photoLocationImages/{id}", photoLocationImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(photoLocationImage.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPhotoLocationImage() throws Exception {
        // Get the photoLocationImage
        restPhotoLocationImageMockMvc.perform(get("/api/photoLocationImages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePhotoLocationImage() throws Exception {
        // Initialize the database
        photoLocationImageRepository.saveAndFlush(photoLocationImage);

		int databaseSizeBeforeUpdate = photoLocationImageRepository.findAll().size();

        // Update the photoLocationImage

        restPhotoLocationImageMockMvc.perform(put("/api/photoLocationImages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationImage)))
                .andExpect(status().isOk());

        // Validate the PhotoLocationImage in the database
        List<PhotoLocationImage> photoLocationImages = photoLocationImageRepository.findAll();
        assertThat(photoLocationImages).hasSize(databaseSizeBeforeUpdate);
        PhotoLocationImage testPhotoLocationImage = photoLocationImages.get(photoLocationImages.size() - 1);
    }

    @Test
    @Transactional
    public void deletePhotoLocationImage() throws Exception {
        // Initialize the database
        photoLocationImageRepository.saveAndFlush(photoLocationImage);

		int databaseSizeBeforeDelete = photoLocationImageRepository.findAll().size();

        // Get the photoLocationImage
        restPhotoLocationImageMockMvc.perform(delete("/api/photoLocationImages/{id}", photoLocationImage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PhotoLocationImage> photoLocationImages = photoLocationImageRepository.findAll();
        assertThat(photoLocationImages).hasSize(databaseSizeBeforeDelete - 1);
    }
}
