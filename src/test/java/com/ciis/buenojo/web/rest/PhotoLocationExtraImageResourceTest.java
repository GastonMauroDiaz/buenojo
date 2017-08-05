package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.PhotoLocationExtraImage;
import com.ciis.buenojo.repository.PhotoLocationExtraImageRepository;

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
 * Test class for the PhotoLocationExtraImageResource REST controller.
 *
 * @see PhotoLocationExtraImageResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PhotoLocationExtraImageResourceTest {


    @Inject
    private PhotoLocationExtraImageRepository photoLocationExtraImageRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPhotoLocationExtraImageMockMvc;

    private PhotoLocationExtraImage photoLocationExtraImage;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PhotoLocationExtraImageResource photoLocationExtraImageResource = new PhotoLocationExtraImageResource();
        ReflectionTestUtils.setField(photoLocationExtraImageResource, "photoLocationExtraImageRepository", photoLocationExtraImageRepository);
        this.restPhotoLocationExtraImageMockMvc = MockMvcBuilders.standaloneSetup(photoLocationExtraImageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        photoLocationExtraImage = new PhotoLocationExtraImage();
    }

    @Test
    @Transactional
    public void createPhotoLocationExtraImage() throws Exception {
        int databaseSizeBeforeCreate = photoLocationExtraImageRepository.findAll().size();

        // Create the PhotoLocationExtraImage

        restPhotoLocationExtraImageMockMvc.perform(post("/api/photoLocationExtraImages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationExtraImage)))
                .andExpect(status().isCreated());

        // Validate the PhotoLocationExtraImage in the database
        List<PhotoLocationExtraImage> photoLocationExtraImages = photoLocationExtraImageRepository.findAll();
        assertThat(photoLocationExtraImages).hasSize(databaseSizeBeforeCreate + 1);
        PhotoLocationExtraImage testPhotoLocationExtraImage = photoLocationExtraImages.get(photoLocationExtraImages.size() - 1);
    }

    @Test
    @Transactional
    public void getAllPhotoLocationExtraImages() throws Exception {
        // Initialize the database
        photoLocationExtraImageRepository.saveAndFlush(photoLocationExtraImage);

        // Get all the photoLocationExtraImages
        restPhotoLocationExtraImageMockMvc.perform(get("/api/photoLocationExtraImages"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(photoLocationExtraImage.getId().intValue())));
    }

    @Test
    @Transactional
    public void getPhotoLocationExtraImage() throws Exception {
        // Initialize the database
        photoLocationExtraImageRepository.saveAndFlush(photoLocationExtraImage);

        // Get the photoLocationExtraImage
        restPhotoLocationExtraImageMockMvc.perform(get("/api/photoLocationExtraImages/{id}", photoLocationExtraImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(photoLocationExtraImage.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPhotoLocationExtraImage() throws Exception {
        // Get the photoLocationExtraImage
        restPhotoLocationExtraImageMockMvc.perform(get("/api/photoLocationExtraImages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePhotoLocationExtraImage() throws Exception {
        // Initialize the database
        photoLocationExtraImageRepository.saveAndFlush(photoLocationExtraImage);

		int databaseSizeBeforeUpdate = photoLocationExtraImageRepository.findAll().size();

        // Update the photoLocationExtraImage

        restPhotoLocationExtraImageMockMvc.perform(put("/api/photoLocationExtraImages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationExtraImage)))
                .andExpect(status().isOk());

        // Validate the PhotoLocationExtraImage in the database
        List<PhotoLocationExtraImage> photoLocationExtraImages = photoLocationExtraImageRepository.findAll();
        assertThat(photoLocationExtraImages).hasSize(databaseSizeBeforeUpdate);
        PhotoLocationExtraImage testPhotoLocationExtraImage = photoLocationExtraImages.get(photoLocationExtraImages.size() - 1);
    }

    @Test
    @Transactional
    public void deletePhotoLocationExtraImage() throws Exception {
        // Initialize the database
        photoLocationExtraImageRepository.saveAndFlush(photoLocationExtraImage);

		int databaseSizeBeforeDelete = photoLocationExtraImageRepository.findAll().size();

        // Get the photoLocationExtraImage
        restPhotoLocationExtraImageMockMvc.perform(delete("/api/photoLocationExtraImages/{id}", photoLocationExtraImage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PhotoLocationExtraImage> photoLocationExtraImages = photoLocationExtraImageRepository.findAll();
        assertThat(photoLocationExtraImages).hasSize(databaseSizeBeforeDelete - 1);
    }
}
