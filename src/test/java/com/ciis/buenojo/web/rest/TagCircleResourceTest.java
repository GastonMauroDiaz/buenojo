package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.TagCircle;
import com.ciis.buenojo.repository.TagCircleRepository;

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
 * Test class for the TagCircleResource REST controller.
 *
 * @see TagCircleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TagCircleResourceTest {


    private static final Integer DEFAULT_NUMBER = 0;
    private static final Integer UPDATED_NUMBER = 1;

    private static final Integer DEFAULT_Y = 0;
    private static final Integer UPDATED_Y = 1;

    private static final Integer DEFAULT_X = 0;
    private static final Integer UPDATED_X = 1;

    private static final Float DEFAULT_RADIO_PX = 0F;
    private static final Float UPDATED_RADIO_PX = 1F;

    @Inject
    private TagCircleRepository tagCircleRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTagCircleMockMvc;

    private TagCircle tagCircle;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TagCircleResource tagCircleResource = new TagCircleResource();
        ReflectionTestUtils.setField(tagCircleResource, "tagCircleRepository", tagCircleRepository);
        this.restTagCircleMockMvc = MockMvcBuilders.standaloneSetup(tagCircleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        tagCircle = new TagCircle();
        tagCircle.setNumber(DEFAULT_NUMBER);
        tagCircle.setY(DEFAULT_Y);
        tagCircle.setX(DEFAULT_X);
        tagCircle.setRadioPx(DEFAULT_RADIO_PX);
    }

    @Test
    @Transactional
    public void createTagCircle() throws Exception {
        int databaseSizeBeforeCreate = tagCircleRepository.findAll().size();

        // Create the TagCircle

        restTagCircleMockMvc.perform(post("/api/tagCircles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tagCircle)))
                .andExpect(status().isCreated());

        // Validate the TagCircle in the database
        List<TagCircle> tagCircles = tagCircleRepository.findAll();
        assertThat(tagCircles).hasSize(databaseSizeBeforeCreate + 1);
        TagCircle testTagCircle = tagCircles.get(tagCircles.size() - 1);
        assertThat(testTagCircle.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testTagCircle.getY()).isEqualTo(DEFAULT_Y);
        assertThat(testTagCircle.getX()).isEqualTo(DEFAULT_X);
        assertThat(testTagCircle.getRadioPx()).isEqualTo(DEFAULT_RADIO_PX);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = tagCircleRepository.findAll().size();
        // set the field null
        tagCircle.setNumber(null);

        // Create the TagCircle, which fails.

        restTagCircleMockMvc.perform(post("/api/tagCircles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tagCircle)))
                .andExpect(status().isBadRequest());

        List<TagCircle> tagCircles = tagCircleRepository.findAll();
        assertThat(tagCircles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYIsRequired() throws Exception {
        int databaseSizeBeforeTest = tagCircleRepository.findAll().size();
        // set the field null
        tagCircle.setY(null);

        // Create the TagCircle, which fails.

        restTagCircleMockMvc.perform(post("/api/tagCircles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tagCircle)))
                .andExpect(status().isBadRequest());

        List<TagCircle> tagCircles = tagCircleRepository.findAll();
        assertThat(tagCircles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkXIsRequired() throws Exception {
        int databaseSizeBeforeTest = tagCircleRepository.findAll().size();
        // set the field null
        tagCircle.setX(null);

        // Create the TagCircle, which fails.

        restTagCircleMockMvc.perform(post("/api/tagCircles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tagCircle)))
                .andExpect(status().isBadRequest());

        List<TagCircle> tagCircles = tagCircleRepository.findAll();
        assertThat(tagCircles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRadioPxIsRequired() throws Exception {
        int databaseSizeBeforeTest = tagCircleRepository.findAll().size();
        // set the field null
        tagCircle.setRadioPx(null);

        // Create the TagCircle, which fails.

        restTagCircleMockMvc.perform(post("/api/tagCircles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tagCircle)))
                .andExpect(status().isBadRequest());

        List<TagCircle> tagCircles = tagCircleRepository.findAll();
        assertThat(tagCircles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTagCircles() throws Exception {
        // Initialize the database
        tagCircleRepository.saveAndFlush(tagCircle);

        // Get all the tagCircles
        restTagCircleMockMvc.perform(get("/api/tagCircles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tagCircle.getId().intValue())))
                .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
                .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y)))
                .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X)))
                .andExpect(jsonPath("$.[*].radioPx").value(hasItem(DEFAULT_RADIO_PX.doubleValue())));
    }

    @Test
    @Transactional
    public void getTagCircle() throws Exception {
        // Initialize the database
        tagCircleRepository.saveAndFlush(tagCircle);

        // Get the tagCircle
        restTagCircleMockMvc.perform(get("/api/tagCircles/{id}", tagCircle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tagCircle.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.y").value(DEFAULT_Y))
            .andExpect(jsonPath("$.x").value(DEFAULT_X))
            .andExpect(jsonPath("$.radioPx").value(DEFAULT_RADIO_PX.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTagCircle() throws Exception {
        // Get the tagCircle
        restTagCircleMockMvc.perform(get("/api/tagCircles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTagCircle() throws Exception {
        // Initialize the database
        tagCircleRepository.saveAndFlush(tagCircle);

		int databaseSizeBeforeUpdate = tagCircleRepository.findAll().size();

        // Update the tagCircle
        tagCircle.setNumber(UPDATED_NUMBER);
        tagCircle.setY(UPDATED_Y);
        tagCircle.setX(UPDATED_X);
        tagCircle.setRadioPx(UPDATED_RADIO_PX);

        restTagCircleMockMvc.perform(put("/api/tagCircles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tagCircle)))
                .andExpect(status().isOk());

        // Validate the TagCircle in the database
        List<TagCircle> tagCircles = tagCircleRepository.findAll();
        assertThat(tagCircles).hasSize(databaseSizeBeforeUpdate);
        TagCircle testTagCircle = tagCircles.get(tagCircles.size() - 1);
        assertThat(testTagCircle.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testTagCircle.getY()).isEqualTo(UPDATED_Y);
        assertThat(testTagCircle.getX()).isEqualTo(UPDATED_X);
        assertThat(testTagCircle.getRadioPx()).isEqualTo(UPDATED_RADIO_PX);
    }

    @Test
    @Transactional
    public void deleteTagCircle() throws Exception {
        // Initialize the database
        tagCircleRepository.saveAndFlush(tagCircle);

		int databaseSizeBeforeDelete = tagCircleRepository.findAll().size();

        // Get the tagCircle
        restTagCircleMockMvc.perform(delete("/api/tagCircles/{id}", tagCircle.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TagCircle> tagCircles = tagCircleRepository.findAll();
        assertThat(tagCircles).hasSize(databaseSizeBeforeDelete - 1);
    }
}
