package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.ImageResource;
import com.ciis.buenojo.repository.ImageResourceRepository;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ImageResourceResource REST controller.
 *
 * @see ImageResourceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ImageResourceResourceTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final byte[] DEFAULT_LO_RES_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LO_RES_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_LO_RES_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LO_RES_IMAGE_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_HI_RES_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_HI_RES_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_HI_RES_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_HI_RES_IMAGE_CONTENT_TYPE = "image/png";

    @Inject
    private ImageResourceRepository imageResourceRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restImageResourceMockMvc;

    private ImageResource imageResource;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ImageResourceResource imageResourceResource = new ImageResourceResource();
        ReflectionTestUtils.setField(imageResourceResource, "imageResourceRepository", imageResourceRepository);
        this.restImageResourceMockMvc = MockMvcBuilders.standaloneSetup(imageResourceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        imageResource = new ImageResource();
        imageResource.setName(DEFAULT_NAME);
        imageResource.setLoResImage(DEFAULT_LO_RES_IMAGE);
        imageResource.setLoResImageContentType(DEFAULT_LO_RES_IMAGE_CONTENT_TYPE);
        imageResource.setHiResImage(DEFAULT_HI_RES_IMAGE);
        imageResource.setHiResImageContentType(DEFAULT_HI_RES_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createImageResource() throws Exception {
        int databaseSizeBeforeCreate = imageResourceRepository.findAll().size();

        // Create the ImageResource

        restImageResourceMockMvc.perform(post("/api/imageResources")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(imageResource)))
                .andExpect(status().isCreated());

        // Validate the ImageResource in the database
        List<ImageResource> imageResources = imageResourceRepository.findAll();
        assertThat(imageResources).hasSize(databaseSizeBeforeCreate + 1);
        ImageResource testImageResource = imageResources.get(imageResources.size() - 1);
        assertThat(testImageResource.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testImageResource.getLoResImage()).isEqualTo(DEFAULT_LO_RES_IMAGE);
        assertThat(testImageResource.getLoResImageContentType()).isEqualTo(DEFAULT_LO_RES_IMAGE_CONTENT_TYPE);
        assertThat(testImageResource.getHiResImage()).isEqualTo(DEFAULT_HI_RES_IMAGE);
        assertThat(testImageResource.getHiResImageContentType()).isEqualTo(DEFAULT_HI_RES_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = imageResourceRepository.findAll().size();
        // set the field null
        imageResource.setName(null);

        // Create the ImageResource, which fails.

        restImageResourceMockMvc.perform(post("/api/imageResources")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(imageResource)))
                .andExpect(status().isBadRequest());

        List<ImageResource> imageResources = imageResourceRepository.findAll();
        assertThat(imageResources).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLoResImageIsRequired() throws Exception {
        int databaseSizeBeforeTest = imageResourceRepository.findAll().size();
        // set the field null
        imageResource.setLoResImage(null);

        // Create the ImageResource, which fails.

        restImageResourceMockMvc.perform(post("/api/imageResources")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(imageResource)))
                .andExpect(status().isBadRequest());

        List<ImageResource> imageResources = imageResourceRepository.findAll();
        assertThat(imageResources).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHiResImageIsRequired() throws Exception {
        int databaseSizeBeforeTest = imageResourceRepository.findAll().size();
        // set the field null
        imageResource.setHiResImage(null);

        // Create the ImageResource, which fails.

        restImageResourceMockMvc.perform(post("/api/imageResources")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(imageResource)))
                .andExpect(status().isBadRequest());

        List<ImageResource> imageResources = imageResourceRepository.findAll();
        assertThat(imageResources).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllImageResources() throws Exception {
        // Initialize the database
        imageResourceRepository.saveAndFlush(imageResource);

        // Get all the imageResources
        restImageResourceMockMvc.perform(get("/api/imageResources"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(imageResource.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].loResImageContentType").value(hasItem(DEFAULT_LO_RES_IMAGE_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].loResImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_LO_RES_IMAGE))))
                .andExpect(jsonPath("$.[*].hiResImageContentType").value(hasItem(DEFAULT_HI_RES_IMAGE_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].hiResImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_HI_RES_IMAGE))));
    }

    @Test
    @Transactional
    public void getImageResource() throws Exception {
        // Initialize the database
        imageResourceRepository.saveAndFlush(imageResource);

        // Get the imageResource
        restImageResourceMockMvc.perform(get("/api/imageResources/{id}", imageResource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(imageResource.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.loResImageContentType").value(DEFAULT_LO_RES_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.loResImage").value(Base64Utils.encodeToString(DEFAULT_LO_RES_IMAGE)))
            .andExpect(jsonPath("$.hiResImageContentType").value(DEFAULT_HI_RES_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.hiResImage").value(Base64Utils.encodeToString(DEFAULT_HI_RES_IMAGE)));
    }

    @Test
    @Transactional
    public void getNonExistingImageResource() throws Exception {
        // Get the imageResource
        restImageResourceMockMvc.perform(get("/api/imageResources/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateImageResource() throws Exception {
        // Initialize the database
        imageResourceRepository.saveAndFlush(imageResource);

		int databaseSizeBeforeUpdate = imageResourceRepository.findAll().size();

        // Update the imageResource
        imageResource.setName(UPDATED_NAME);
        imageResource.setLoResImage(UPDATED_LO_RES_IMAGE);
        imageResource.setLoResImageContentType(UPDATED_LO_RES_IMAGE_CONTENT_TYPE);
        imageResource.setHiResImage(UPDATED_HI_RES_IMAGE);
        imageResource.setHiResImageContentType(UPDATED_HI_RES_IMAGE_CONTENT_TYPE);

        restImageResourceMockMvc.perform(put("/api/imageResources")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(imageResource)))
                .andExpect(status().isOk());

        // Validate the ImageResource in the database
        List<ImageResource> imageResources = imageResourceRepository.findAll();
        assertThat(imageResources).hasSize(databaseSizeBeforeUpdate);
        ImageResource testImageResource = imageResources.get(imageResources.size() - 1);
        assertThat(testImageResource.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testImageResource.getLoResImage()).isEqualTo(UPDATED_LO_RES_IMAGE);
        assertThat(testImageResource.getLoResImageContentType()).isEqualTo(UPDATED_LO_RES_IMAGE_CONTENT_TYPE);
        assertThat(testImageResource.getHiResImage()).isEqualTo(UPDATED_HI_RES_IMAGE);
        assertThat(testImageResource.getHiResImageContentType()).isEqualTo(UPDATED_HI_RES_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void deleteImageResource() throws Exception {
        // Initialize the database
        imageResourceRepository.saveAndFlush(imageResource);

		int databaseSizeBeforeDelete = imageResourceRepository.findAll().size();

        // Get the imageResource
        restImageResourceMockMvc.perform(delete("/api/imageResources/{id}", imageResource.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ImageResource> imageResources = imageResourceRepository.findAll();
        assertThat(imageResources).hasSize(databaseSizeBeforeDelete - 1);
    }
}
