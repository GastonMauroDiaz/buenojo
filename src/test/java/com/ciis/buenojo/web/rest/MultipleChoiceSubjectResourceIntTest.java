package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.MultipleChoiceSubject;
import com.ciis.buenojo.repository.MultipleChoiceSubjectRepository;

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
 * Test class for the MultipleChoiceSubjectResource REST controller.
 *
 * @see MultipleChoiceSubjectResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MultipleChoiceSubjectResourceIntTest {

    private static final String DEFAULT_TEXT = "AAAAA";
    private static final String UPDATED_TEXT = "BBBBB";

    @Inject
    private MultipleChoiceSubjectRepository multipleChoiceSubjectRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMultipleChoiceSubjectMockMvc;

    private MultipleChoiceSubject multipleChoiceSubject;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MultipleChoiceSubjectResource multipleChoiceSubjectResource = new MultipleChoiceSubjectResource();
        ReflectionTestUtils.setField(multipleChoiceSubjectResource, "multipleChoiceSubjectRepository", multipleChoiceSubjectRepository);
        this.restMultipleChoiceSubjectMockMvc = MockMvcBuilders.standaloneSetup(multipleChoiceSubjectResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        multipleChoiceSubject = new MultipleChoiceSubject();
        multipleChoiceSubject.setText(DEFAULT_TEXT);
    }

    @Test
    @Transactional
    public void createMultipleChoiceSubject() throws Exception {
        int databaseSizeBeforeCreate = multipleChoiceSubjectRepository.findAll().size();

        // Create the MultipleChoiceSubject

        restMultipleChoiceSubjectMockMvc.perform(post("/api/multipleChoiceSubjects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(multipleChoiceSubject)))
                .andExpect(status().isCreated());

        // Validate the MultipleChoiceSubject in the database
        List<MultipleChoiceSubject> multipleChoiceSubjects = multipleChoiceSubjectRepository.findAll();
        assertThat(multipleChoiceSubjects).hasSize(databaseSizeBeforeCreate + 1);
        MultipleChoiceSubject testMultipleChoiceSubject = multipleChoiceSubjects.get(multipleChoiceSubjects.size() - 1);
        assertThat(testMultipleChoiceSubject.getText()).isEqualTo(DEFAULT_TEXT);
    }

    @Test
    @Transactional
    public void checkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = multipleChoiceSubjectRepository.findAll().size();
        // set the field null
        multipleChoiceSubject.setText(null);

        // Create the MultipleChoiceSubject, which fails.

        restMultipleChoiceSubjectMockMvc.perform(post("/api/multipleChoiceSubjects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(multipleChoiceSubject)))
                .andExpect(status().isBadRequest());

        List<MultipleChoiceSubject> multipleChoiceSubjects = multipleChoiceSubjectRepository.findAll();
        assertThat(multipleChoiceSubjects).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMultipleChoiceSubjects() throws Exception {
        // Initialize the database
        multipleChoiceSubjectRepository.saveAndFlush(multipleChoiceSubject);

        // Get all the multipleChoiceSubjects
        restMultipleChoiceSubjectMockMvc.perform(get("/api/multipleChoiceSubjects"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(multipleChoiceSubject.getId().intValue())))
                .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }

    @Test
    @Transactional
    public void getMultipleChoiceSubject() throws Exception {
        // Initialize the database
        multipleChoiceSubjectRepository.saveAndFlush(multipleChoiceSubject);

        // Get the multipleChoiceSubject
        restMultipleChoiceSubjectMockMvc.perform(get("/api/multipleChoiceSubjects/{id}", multipleChoiceSubject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(multipleChoiceSubject.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMultipleChoiceSubject() throws Exception {
        // Get the multipleChoiceSubject
        restMultipleChoiceSubjectMockMvc.perform(get("/api/multipleChoiceSubjects/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMultipleChoiceSubject() throws Exception {
        // Initialize the database
        multipleChoiceSubjectRepository.saveAndFlush(multipleChoiceSubject);

		int databaseSizeBeforeUpdate = multipleChoiceSubjectRepository.findAll().size();

        // Update the multipleChoiceSubject
        multipleChoiceSubject.setText(UPDATED_TEXT);

        restMultipleChoiceSubjectMockMvc.perform(put("/api/multipleChoiceSubjects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(multipleChoiceSubject)))
                .andExpect(status().isOk());

        // Validate the MultipleChoiceSubject in the database
        List<MultipleChoiceSubject> multipleChoiceSubjects = multipleChoiceSubjectRepository.findAll();
        assertThat(multipleChoiceSubjects).hasSize(databaseSizeBeforeUpdate);
        MultipleChoiceSubject testMultipleChoiceSubject = multipleChoiceSubjects.get(multipleChoiceSubjects.size() - 1);
        assertThat(testMultipleChoiceSubject.getText()).isEqualTo(UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void deleteMultipleChoiceSubject() throws Exception {
        // Initialize the database
        multipleChoiceSubjectRepository.saveAndFlush(multipleChoiceSubject);

		int databaseSizeBeforeDelete = multipleChoiceSubjectRepository.findAll().size();

        // Get the multipleChoiceSubject
        restMultipleChoiceSubjectMockMvc.perform(delete("/api/multipleChoiceSubjects/{id}", multipleChoiceSubject.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<MultipleChoiceSubject> multipleChoiceSubjects = multipleChoiceSubjectRepository.findAll();
        assertThat(multipleChoiceSubjects).hasSize(databaseSizeBeforeDelete - 1);
    }
}
