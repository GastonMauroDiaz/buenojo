package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.PhotoLocationKeyword;
import com.ciis.buenojo.repository.PhotoLocationKeywordRepository;

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
 * Test class for the PhotoLocationKeywordResource REST controller.
 *
 * @see PhotoLocationKeywordResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PhotoLocationKeywordResourceTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private PhotoLocationKeywordRepository photoLocationKeywordRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPhotoLocationKeywordMockMvc;

    private PhotoLocationKeyword photoLocationKeyword;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PhotoLocationKeywordResource photoLocationKeywordResource = new PhotoLocationKeywordResource();
        ReflectionTestUtils.setField(photoLocationKeywordResource, "photoLocationKeywordRepository", photoLocationKeywordRepository);
        this.restPhotoLocationKeywordMockMvc = MockMvcBuilders.standaloneSetup(photoLocationKeywordResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        photoLocationKeyword = new PhotoLocationKeyword();
        photoLocationKeyword.setName(DEFAULT_NAME);
        photoLocationKeyword.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createPhotoLocationKeyword() throws Exception {
        int databaseSizeBeforeCreate = photoLocationKeywordRepository.findAll().size();

        // Create the PhotoLocationKeyword

        restPhotoLocationKeywordMockMvc.perform(post("/api/photoLocationKeywords")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationKeyword)))
                .andExpect(status().isCreated());

        // Validate the PhotoLocationKeyword in the database
        List<PhotoLocationKeyword> photoLocationKeywords = photoLocationKeywordRepository.findAll();
        assertThat(photoLocationKeywords).hasSize(databaseSizeBeforeCreate + 1);
        PhotoLocationKeyword testPhotoLocationKeyword = photoLocationKeywords.get(photoLocationKeywords.size() - 1);
        assertThat(testPhotoLocationKeyword.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPhotoLocationKeyword.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = photoLocationKeywordRepository.findAll().size();
        // set the field null
        photoLocationKeyword.setName(null);

        // Create the PhotoLocationKeyword, which fails.

        restPhotoLocationKeywordMockMvc.perform(post("/api/photoLocationKeywords")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationKeyword)))
                .andExpect(status().isBadRequest());

        List<PhotoLocationKeyword> photoLocationKeywords = photoLocationKeywordRepository.findAll();
        assertThat(photoLocationKeywords).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPhotoLocationKeywords() throws Exception {
        // Initialize the database
        photoLocationKeywordRepository.saveAndFlush(photoLocationKeyword);

        // Get all the photoLocationKeywords
        restPhotoLocationKeywordMockMvc.perform(get("/api/photoLocationKeywords"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(photoLocationKeyword.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getPhotoLocationKeyword() throws Exception {
        // Initialize the database
        photoLocationKeywordRepository.saveAndFlush(photoLocationKeyword);

        // Get the photoLocationKeyword
        restPhotoLocationKeywordMockMvc.perform(get("/api/photoLocationKeywords/{id}", photoLocationKeyword.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(photoLocationKeyword.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPhotoLocationKeyword() throws Exception {
        // Get the photoLocationKeyword
        restPhotoLocationKeywordMockMvc.perform(get("/api/photoLocationKeywords/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePhotoLocationKeyword() throws Exception {
        // Initialize the database
        photoLocationKeywordRepository.saveAndFlush(photoLocationKeyword);

		int databaseSizeBeforeUpdate = photoLocationKeywordRepository.findAll().size();

        // Update the photoLocationKeyword
        photoLocationKeyword.setName(UPDATED_NAME);
        photoLocationKeyword.setDescription(UPDATED_DESCRIPTION);

        restPhotoLocationKeywordMockMvc.perform(put("/api/photoLocationKeywords")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(photoLocationKeyword)))
                .andExpect(status().isOk());

        // Validate the PhotoLocationKeyword in the database
        List<PhotoLocationKeyword> photoLocationKeywords = photoLocationKeywordRepository.findAll();
        assertThat(photoLocationKeywords).hasSize(databaseSizeBeforeUpdate);
        PhotoLocationKeyword testPhotoLocationKeyword = photoLocationKeywords.get(photoLocationKeywords.size() - 1);
        assertThat(testPhotoLocationKeyword.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPhotoLocationKeyword.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deletePhotoLocationKeyword() throws Exception {
        // Initialize the database
        photoLocationKeywordRepository.saveAndFlush(photoLocationKeyword);

		int databaseSizeBeforeDelete = photoLocationKeywordRepository.findAll().size();

        // Get the photoLocationKeyword
        restPhotoLocationKeywordMockMvc.perform(delete("/api/photoLocationKeywords/{id}", photoLocationKeyword.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PhotoLocationKeyword> photoLocationKeywords = photoLocationKeywordRepository.findAll();
        assertThat(photoLocationKeywords).hasSize(databaseSizeBeforeDelete - 1);
    }
}
