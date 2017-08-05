package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.Tag;
import com.ciis.buenojo.domain.TagPool;
import com.ciis.buenojo.repository.TagPoolRepository;

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
 * Test class for the TagPoolResource REST controller.
 *
 * @see TagPoolResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TagPoolResourceTest {


    private static final Integer DEFAULT_SIMILARITY = 1;
    private static final Integer UPDATED_SIMILARITY = 2;

    @Inject
    private TagPoolRepository tagPoolRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTagPoolMockMvc;

    private TagPool tagPool;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TagPoolResource tagPoolResource = new TagPoolResource();
        ReflectionTestUtils.setField(tagPoolResource, "tagPoolRepository", tagPoolRepository);
        this.restTagPoolMockMvc = MockMvcBuilders.standaloneSetup(tagPoolResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        tagPool = new TagPool();
        Tag tag = new Tag();
        tag.setName("test");
        tag.setNumber(1);
        Tag similarTag = new Tag();
        tag.setName("similartest");
        tag.setNumber(1);
        tagPool.setSimilarTag(similarTag);
        tagPool.setTag(tag);
        tagPool.setSimilarity(DEFAULT_SIMILARITY);
    }

    @Test
    @Transactional
    public void createTagPool() throws Exception {
        int databaseSizeBeforeCreate = tagPoolRepository.findAll().size();

        // Create the TagPool

        restTagPoolMockMvc.perform(post("/api/tagPools")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tagPool)))
                .andExpect(status().isCreated());

        // Validate the TagPool in the database
        List<TagPool> tagPools = tagPoolRepository.findAll();
        assertThat(tagPools).hasSize(databaseSizeBeforeCreate + 1);
        TagPool testTagPool = tagPools.get(tagPools.size() - 1);
        assertThat(testTagPool.getSimilarity()).isEqualTo(DEFAULT_SIMILARITY);
    }

    @Test
    @Transactional
    public void checkSimilarityIsRequired() throws Exception {
        int databaseSizeBeforeTest = tagPoolRepository.findAll().size();
        // set the field null
        tagPool.setSimilarity(null);

        // Create the TagPool, which fails.

        restTagPoolMockMvc.perform(post("/api/tagPools")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tagPool)))
                .andExpect(status().isBadRequest());

        List<TagPool> tagPools = tagPoolRepository.findAll();
        assertThat(tagPools).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTagPools() throws Exception {
        // Initialize the database
        tagPoolRepository.saveAndFlush(tagPool);

        // Get all the tagPools
        restTagPoolMockMvc.perform(get("/api/tagPools"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tagPool.getId().intValue())))
                .andExpect(jsonPath("$.[*].similarity").value(hasItem(DEFAULT_SIMILARITY)));
    }

    @Test
    @Transactional
    public void getTagPool() throws Exception {
        // Initialize the database
        tagPoolRepository.saveAndFlush(tagPool);

        // Get the tagPool
        restTagPoolMockMvc.perform(get("/api/tagPools/{id}", tagPool.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tagPool.getId().intValue()))
            .andExpect(jsonPath("$.similarity").value(DEFAULT_SIMILARITY));
    }

    @Test
    @Transactional
    public void getNonExistingTagPool() throws Exception {
        // Get the tagPool
        restTagPoolMockMvc.perform(get("/api/tagPools/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTagPool() throws Exception {
        // Initialize the database
        tagPoolRepository.saveAndFlush(tagPool);

		int databaseSizeBeforeUpdate = tagPoolRepository.findAll().size();

        // Update the tagPool
        tagPool.setSimilarity(UPDATED_SIMILARITY);

        restTagPoolMockMvc.perform(put("/api/tagPools")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tagPool)))
                .andExpect(status().isOk());

        // Validate the TagPool in the database
        List<TagPool> tagPools = tagPoolRepository.findAll();
        assertThat(tagPools).hasSize(databaseSizeBeforeUpdate);
        TagPool testTagPool = tagPools.get(tagPools.size() - 1);
        assertThat(testTagPool.getSimilarity()).isEqualTo(UPDATED_SIMILARITY);
    }

    @Test
    @Transactional
    public void deleteTagPool() throws Exception {
        // Initialize the database
        tagPoolRepository.saveAndFlush(tagPool);

		int databaseSizeBeforeDelete = tagPoolRepository.findAll().size();

        // Get the tagPool
        restTagPoolMockMvc.perform(delete("/api/tagPools/{id}", tagPool.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TagPool> tagPools = tagPoolRepository.findAll();
        assertThat(tagPools).hasSize(databaseSizeBeforeDelete - 1);
    }
}
