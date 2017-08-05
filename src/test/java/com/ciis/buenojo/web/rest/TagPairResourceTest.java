package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.TagPair;
import com.ciis.buenojo.repository.TagPairRepository;

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
 * Test class for the TagPairResource REST controller.
 *
 * @see TagPairResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TagPairResourceTest {


    private static final Integer DEFAULT_TAG_SLOT_ID = 1;
    private static final Integer UPDATED_TAG_SLOT_ID = 2;

    @Inject
    private TagPairRepository tagPairRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTagPairMockMvc;

    private TagPair tagPair;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TagPairResource tagPairResource = new TagPairResource();
        ReflectionTestUtils.setField(tagPairResource, "tagPairRepository", tagPairRepository);
        this.restTagPairMockMvc = MockMvcBuilders.standaloneSetup(tagPairResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        tagPair = new TagPair();
        tagPair.setTagSlotId(DEFAULT_TAG_SLOT_ID);
    }

    @Test
    @Transactional
    public void createTagPair() throws Exception {
        int databaseSizeBeforeCreate = tagPairRepository.findAll().size();

        // Create the TagPair

        restTagPairMockMvc.perform(post("/api/tagPairs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tagPair)))
                .andExpect(status().isCreated());

        // Validate the TagPair in the database
        List<TagPair> tagPairs = tagPairRepository.findAll();
        assertThat(tagPairs).hasSize(databaseSizeBeforeCreate + 1);
        TagPair testTagPair = tagPairs.get(tagPairs.size() - 1);
        assertThat(testTagPair.getTagSlotId()).isEqualTo(DEFAULT_TAG_SLOT_ID);
    }

    @Test
    @Transactional
    public void checkTagSlotIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = tagPairRepository.findAll().size();
        // set the field null
        tagPair.setTagSlotId(null);

        // Create the TagPair, which fails.

        restTagPairMockMvc.perform(post("/api/tagPairs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tagPair)))
                .andExpect(status().isBadRequest());

        List<TagPair> tagPairs = tagPairRepository.findAll();
        assertThat(tagPairs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTagPairs() throws Exception {
        // Initialize the database
        tagPairRepository.saveAndFlush(tagPair);

        // Get all the tagPairs
        restTagPairMockMvc.perform(get("/api/tagPairs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tagPair.getId().intValue())))
                .andExpect(jsonPath("$.[*].tagSlotId").value(hasItem(DEFAULT_TAG_SLOT_ID)));
    }

    @Test
    @Transactional
    public void getTagPair() throws Exception {
        // Initialize the database
        tagPairRepository.saveAndFlush(tagPair);

        // Get the tagPair
        restTagPairMockMvc.perform(get("/api/tagPairs/{id}", tagPair.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tagPair.getId().intValue()))
            .andExpect(jsonPath("$.tagSlotId").value(DEFAULT_TAG_SLOT_ID));
    }

    @Test
    @Transactional
    public void getNonExistingTagPair() throws Exception {
        // Get the tagPair
        restTagPairMockMvc.perform(get("/api/tagPairs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTagPair() throws Exception {
        // Initialize the database
        tagPairRepository.saveAndFlush(tagPair);

		int databaseSizeBeforeUpdate = tagPairRepository.findAll().size();

        // Update the tagPair
        tagPair.setTagSlotId(UPDATED_TAG_SLOT_ID);

        restTagPairMockMvc.perform(put("/api/tagPairs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tagPair)))
                .andExpect(status().isOk());

        // Validate the TagPair in the database
        List<TagPair> tagPairs = tagPairRepository.findAll();
        assertThat(tagPairs).hasSize(databaseSizeBeforeUpdate);
        TagPair testTagPair = tagPairs.get(tagPairs.size() - 1);
        assertThat(testTagPair.getTagSlotId()).isEqualTo(UPDATED_TAG_SLOT_ID);
    }

    @Test
    @Transactional
    public void deleteTagPair() throws Exception {
        // Initialize the database
        tagPairRepository.saveAndFlush(tagPair);

		int databaseSizeBeforeDelete = tagPairRepository.findAll().size();

        // Get the tagPair
        restTagPairMockMvc.perform(delete("/api/tagPairs/{id}", tagPair.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TagPair> tagPairs = tagPairRepository.findAll();
        assertThat(tagPairs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
