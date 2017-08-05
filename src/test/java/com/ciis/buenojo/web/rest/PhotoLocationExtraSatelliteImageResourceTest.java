package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.PhotoLocationExtraSatelliteImage;
import com.ciis.buenojo.repository.PhotoLocationExtraSatelliteImageRepository;

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
 * Test class for the PhotoLocationExtraSatelliteImageResource REST controller.
 *
 * @see PhotoLocationExtraSatelliteImageResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PhotoLocationExtraSatelliteImageResourceTest {


    @Inject
    private PhotoLocationExtraSatelliteImageRepository photoLocationExtraSatelliteImageRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPhotoLocationExtraSatelliteImageMockMvc;

    private PhotoLocationExtraSatelliteImage photoLocationExtraSatelliteImage;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PhotoLocationExtraSatelliteImageResource photoLocationExtraSatelliteImageResource = new PhotoLocationExtraSatelliteImageResource();
        ReflectionTestUtils.setField(photoLocationExtraSatelliteImageResource, "photoLocationExtraSatelliteImageRepository", photoLocationExtraSatelliteImageRepository);
        this.restPhotoLocationExtraSatelliteImageMockMvc = MockMvcBuilders.standaloneSetup(photoLocationExtraSatelliteImageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        photoLocationExtraSatelliteImage = new PhotoLocationExtraSatelliteImage();
    }

    @Test
    @Transactional
    public void createPhotoLocationExtraSatelliteImage() throws Exception {
        int databaseSizeBeforeCreate = photoLocationExtraSatelliteImageRepository.findAll().size();

        // Create the PhotoLocationExtraSatelliteImage

        restPhotoLocationExtraSatelliteImageMockMvc.perform(post("/api/photoLocationExtraSatelliteImages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationExtraSatelliteImage)))
                .andExpect(status().isCreated());

        // Validate the PhotoLocationExtraSatelliteImage in the database
        List<PhotoLocationExtraSatelliteImage> photoLocationExtraSatelliteImages = photoLocationExtraSatelliteImageRepository.findAll();
        assertThat(photoLocationExtraSatelliteImages).hasSize(databaseSizeBeforeCreate + 1);
        PhotoLocationExtraSatelliteImage testPhotoLocationExtraSatelliteImage = photoLocationExtraSatelliteImages.get(photoLocationExtraSatelliteImages.size() - 1);
    }

    @Test
    @Transactional
    public void getAllPhotoLocationExtraSatelliteImages() throws Exception {
        // Initialize the database
        photoLocationExtraSatelliteImageRepository.saveAndFlush(photoLocationExtraSatelliteImage);

        // Get all the photoLocationExtraSatelliteImages
        restPhotoLocationExtraSatelliteImageMockMvc.perform(get("/api/photoLocationExtraSatelliteImages"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(photoLocationExtraSatelliteImage.getId().intValue())));
    }

    @Test
    @Transactional
    public void getPhotoLocationExtraSatelliteImage() throws Exception {
        // Initialize the database
        photoLocationExtraSatelliteImageRepository.saveAndFlush(photoLocationExtraSatelliteImage);

        // Get the photoLocationExtraSatelliteImage
        restPhotoLocationExtraSatelliteImageMockMvc.perform(get("/api/photoLocationExtraSatelliteImages/{id}", photoLocationExtraSatelliteImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(photoLocationExtraSatelliteImage.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPhotoLocationExtraSatelliteImage() throws Exception {
        // Get the photoLocationExtraSatelliteImage
        restPhotoLocationExtraSatelliteImageMockMvc.perform(get("/api/photoLocationExtraSatelliteImages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePhotoLocationExtraSatelliteImage() throws Exception {
        // Initialize the database
        photoLocationExtraSatelliteImageRepository.saveAndFlush(photoLocationExtraSatelliteImage);

		int databaseSizeBeforeUpdate = photoLocationExtraSatelliteImageRepository.findAll().size();

        // Update the photoLocationExtraSatelliteImage

        restPhotoLocationExtraSatelliteImageMockMvc.perform(put("/api/photoLocationExtraSatelliteImages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationExtraSatelliteImage)))
                .andExpect(status().isOk());

        // Validate the PhotoLocationExtraSatelliteImage in the database
        List<PhotoLocationExtraSatelliteImage> photoLocationExtraSatelliteImages = photoLocationExtraSatelliteImageRepository.findAll();
        assertThat(photoLocationExtraSatelliteImages).hasSize(databaseSizeBeforeUpdate);
        PhotoLocationExtraSatelliteImage testPhotoLocationExtraSatelliteImage = photoLocationExtraSatelliteImages.get(photoLocationExtraSatelliteImages.size() - 1);
    }

    @Test
    @Transactional
    public void deletePhotoLocationExtraSatelliteImage() throws Exception {
        // Initialize the database
        photoLocationExtraSatelliteImageRepository.saveAndFlush(photoLocationExtraSatelliteImage);

		int databaseSizeBeforeDelete = photoLocationExtraSatelliteImageRepository.findAll().size();

        // Get the photoLocationExtraSatelliteImage
        restPhotoLocationExtraSatelliteImageMockMvc.perform(delete("/api/photoLocationExtraSatelliteImages/{id}", photoLocationExtraSatelliteImage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PhotoLocationExtraSatelliteImage> photoLocationExtraSatelliteImages = photoLocationExtraSatelliteImageRepository.findAll();
        assertThat(photoLocationExtraSatelliteImages).hasSize(databaseSizeBeforeDelete - 1);
    }
}
