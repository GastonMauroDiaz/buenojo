package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.MultipleChoiceSubjectSpecific;
import com.ciis.buenojo.repository.MultipleChoiceSubjectSpecificRepository;

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
 * Test class for the MultipleChoiceSubjectSpecificResource REST controller.
 *
 * @see MultipleChoiceSubjectSpecificResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MultipleChoiceSubjectSpecificResourceIntTest {

    private static final String DEFAULT_TEXT = "AAAAA";
    private static final String UPDATED_TEXT = "BBBBB";

    @Inject
    private MultipleChoiceSubjectSpecificRepository multipleChoiceSubjectSpecificRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMultipleChoiceSubjectSpecificMockMvc;

    private MultipleChoiceSubjectSpecific multipleChoiceSubjectSpecific;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MultipleChoiceSubjectSpecificResource multipleChoiceSubjectSpecificResource = new MultipleChoiceSubjectSpecificResource();
        ReflectionTestUtils.setField(multipleChoiceSubjectSpecificResource, "multipleChoiceSubjectSpecificRepository", multipleChoiceSubjectSpecificRepository);
        this.restMultipleChoiceSubjectSpecificMockMvc = MockMvcBuilders.standaloneSetup(multipleChoiceSubjectSpecificResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        multipleChoiceSubjectSpecific = new MultipleChoiceSubjectSpecific();
        multipleChoiceSubjectSpecific.setText(DEFAULT_TEXT);
    }

    @Test
    @Transactional
    public void createMultipleChoiceSubjectSpecific() throws Exception {
        int databaseSizeBeforeCreate = multipleChoiceSubjectSpecificRepository.findAll().size();

        // Create the MultipleChoiceSubjectSpecific

        restMultipleChoiceSubjectSpecificMockMvc.perform(post("/api/multipleChoiceSubjectSpecifics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(multipleChoiceSubjectSpecific)))
                .andExpect(status().isCreated());

        // Validate the MultipleChoiceSubjectSpecific in the database
        List<MultipleChoiceSubjectSpecific> multipleChoiceSubjectSpecifics = multipleChoiceSubjectSpecificRepository.findAll();
        assertThat(multipleChoiceSubjectSpecifics).hasSize(databaseSizeBeforeCreate + 1);
        MultipleChoiceSubjectSpecific testMultipleChoiceSubjectSpecific = multipleChoiceSubjectSpecifics.get(multipleChoiceSubjectSpecifics.size() - 1);
        assertThat(testMultipleChoiceSubjectSpecific.getText()).isEqualTo(DEFAULT_TEXT);
    }

    @Test
    @Transactional
    public void getAllMultipleChoiceSubjectSpecifics() throws Exception {
        // Initialize the database
        multipleChoiceSubjectSpecificRepository.saveAndFlush(multipleChoiceSubjectSpecific);

        // Get all the multipleChoiceSubjectSpecifics
        restMultipleChoiceSubjectSpecificMockMvc.perform(get("/api/multipleChoiceSubjectSpecifics"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(multipleChoiceSubjectSpecific.getId().intValue())))
                .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }

    @Test
    @Transactional
    public void getMultipleChoiceSubjectSpecific() throws Exception {
        // Initialize the database
        multipleChoiceSubjectSpecificRepository.saveAndFlush(multipleChoiceSubjectSpecific);

        // Get the multipleChoiceSubjectSpecific
        restMultipleChoiceSubjectSpecificMockMvc.perform(get("/api/multipleChoiceSubjectSpecifics/{id}", multipleChoiceSubjectSpecific.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(multipleChoiceSubjectSpecific.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMultipleChoiceSubjectSpecific() throws Exception {
        // Get the multipleChoiceSubjectSpecific
        restMultipleChoiceSubjectSpecificMockMvc.perform(get("/api/multipleChoiceSubjectSpecifics/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMultipleChoiceSubjectSpecific() throws Exception {
        // Initialize the database
        multipleChoiceSubjectSpecificRepository.saveAndFlush(multipleChoiceSubjectSpecific);

		int databaseSizeBeforeUpdate = multipleChoiceSubjectSpecificRepository.findAll().size();

        // Update the multipleChoiceSubjectSpecific
        multipleChoiceSubjectSpecific.setText(UPDATED_TEXT);

        restMultipleChoiceSubjectSpecificMockMvc.perform(put("/api/multipleChoiceSubjectSpecifics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(multipleChoiceSubjectSpecific)))
                .andExpect(status().isOk());

        // Validate the MultipleChoiceSubjectSpecific in the database
        List<MultipleChoiceSubjectSpecific> multipleChoiceSubjectSpecifics = multipleChoiceSubjectSpecificRepository.findAll();
        assertThat(multipleChoiceSubjectSpecifics).hasSize(databaseSizeBeforeUpdate);
        MultipleChoiceSubjectSpecific testMultipleChoiceSubjectSpecific = multipleChoiceSubjectSpecifics.get(multipleChoiceSubjectSpecifics.size() - 1);
        assertThat(testMultipleChoiceSubjectSpecific.getText()).isEqualTo(UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void deleteMultipleChoiceSubjectSpecific() throws Exception {
        // Initialize the database
        multipleChoiceSubjectSpecificRepository.saveAndFlush(multipleChoiceSubjectSpecific);

		int databaseSizeBeforeDelete = multipleChoiceSubjectSpecificRepository.findAll().size();

        // Get the multipleChoiceSubjectSpecific
        restMultipleChoiceSubjectSpecificMockMvc.perform(delete("/api/multipleChoiceSubjectSpecifics/{id}", multipleChoiceSubjectSpecific.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<MultipleChoiceSubjectSpecific> multipleChoiceSubjectSpecifics = multipleChoiceSubjectSpecificRepository.findAll();
        assertThat(multipleChoiceSubjectSpecifics).hasSize(databaseSizeBeforeDelete - 1);
    }
}
