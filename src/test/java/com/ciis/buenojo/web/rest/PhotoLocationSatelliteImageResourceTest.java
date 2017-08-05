package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.PhotoLocationSatelliteImage;
import com.ciis.buenojo.repository.PhotoLocationSatelliteImageRepository;

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
 * Test class for the PhotoLocationSatelliteImageResource REST controller.
 *
 * @see PhotoLocationSatelliteImageResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PhotoLocationSatelliteImageResourceTest {


    @Inject
    private PhotoLocationSatelliteImageRepository photoLocationSatelliteImageRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPhotoLocationSatelliteImageMockMvc;

    private PhotoLocationSatelliteImage photoLocationSatelliteImage;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PhotoLocationSatelliteImageResource photoLocationSatelliteImageResource = new PhotoLocationSatelliteImageResource();
        ReflectionTestUtils.setField(photoLocationSatelliteImageResource, "photoLocationSatelliteImageRepository", photoLocationSatelliteImageRepository);
        this.restPhotoLocationSatelliteImageMockMvc = MockMvcBuilders.standaloneSetup(photoLocationSatelliteImageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        photoLocationSatelliteImage = new PhotoLocationSatelliteImage();
    }

    @Test
    @Transactional
    public void createPhotoLocationSatelliteImage() throws Exception {
        int databaseSizeBeforeCreate = photoLocationSatelliteImageRepository.findAll().size();

        // Create the PhotoLocationSatelliteImage

        restPhotoLocationSatelliteImageMockMvc.perform(post("/api/photoLocationSatelliteImages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationSatelliteImage)))
                .andExpect(status().isCreated());

        // Validate the PhotoLocationSatelliteImage in the database
        List<PhotoLocationSatelliteImage> photoLocationSatelliteImages = photoLocationSatelliteImageRepository.findAll();
        assertThat(photoLocationSatelliteImages).hasSize(databaseSizeBeforeCreate + 1);
        PhotoLocationSatelliteImage testPhotoLocationSatelliteImage = photoLocationSatelliteImages.get(photoLocationSatelliteImages.size() - 1);
    }

    @Test
    @Transactional
    public void getAllPhotoLocationSatelliteImages() throws Exception {
        // Initialize the database
        photoLocationSatelliteImageRepository.saveAndFlush(photoLocationSatelliteImage);

        // Get all the photoLocationSatelliteImages
        restPhotoLocationSatelliteImageMockMvc.perform(get("/api/photoLocationSatelliteImages"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(photoLocationSatelliteImage.getId().intValue())));
    }

    @Test
    @Transactional
    public void getPhotoLocationSatelliteImage() throws Exception {
        // Initialize the database
        photoLocationSatelliteImageRepository.saveAndFlush(photoLocationSatelliteImage);

        // Get the photoLocationSatelliteImage
        restPhotoLocationSatelliteImageMockMvc.perform(get("/api/photoLocationSatelliteImages/{id}", photoLocationSatelliteImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(photoLocationSatelliteImage.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPhotoLocationSatelliteImage() throws Exception {
        // Get the photoLocationSatelliteImage
        restPhotoLocationSatelliteImageMockMvc.perform(get("/api/photoLocationSatelliteImages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePhotoLocationSatelliteImage() throws Exception {
        // Initialize the database
        photoLocationSatelliteImageRepository.saveAndFlush(photoLocationSatelliteImage);

		int databaseSizeBeforeUpdate = photoLocationSatelliteImageRepository.findAll().size();

        // Update the photoLocationSatelliteImage

        restPhotoLocationSatelliteImageMockMvc.perform(put("/api/photoLocationSatelliteImages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationSatelliteImage)))
                .andExpect(status().isOk());

        // Validate the PhotoLocationSatelliteImage in the database
        List<PhotoLocationSatelliteImage> photoLocationSatelliteImages = photoLocationSatelliteImageRepository.findAll();
        assertThat(photoLocationSatelliteImages).hasSize(databaseSizeBeforeUpdate);
        PhotoLocationSatelliteImage testPhotoLocationSatelliteImage = photoLocationSatelliteImages.get(photoLocationSatelliteImages.size() - 1);
    }

    @Test
    @Transactional
    public void deletePhotoLocationSatelliteImage() throws Exception {
        // Initialize the database
        photoLocationSatelliteImageRepository.saveAndFlush(photoLocationSatelliteImage);

		int databaseSizeBeforeDelete = photoLocationSatelliteImageRepository.findAll().size();

        // Get the photoLocationSatelliteImage
        restPhotoLocationSatelliteImageMockMvc.perform(delete("/api/photoLocationSatelliteImages/{id}", photoLocationSatelliteImage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PhotoLocationSatelliteImage> photoLocationSatelliteImages = photoLocationSatelliteImageRepository.findAll();
        assertThat(photoLocationSatelliteImages).hasSize(databaseSizeBeforeDelete - 1);
    }
}
