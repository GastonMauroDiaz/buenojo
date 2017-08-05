package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.MultipleChoiceAnswer;
import com.ciis.buenojo.repository.MultipleChoiceAnswerRepository;

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
 * Test class for the MultipleChoiceAnswerResource REST controller.
 *
 * @see MultipleChoiceAnswerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MultipleChoiceAnswerResourceIntTest {

    private static final String DEFAULT_ANSWER = "AAAAA";
    private static final String UPDATED_ANSWER = "BBBBB";

    private static final Boolean DEFAULT_IS_RIGHT = false;
    private static final Boolean UPDATED_IS_RIGHT = true;
    private static final String DEFAULT_SOURCE = "AAAAA";
    private static final String UPDATED_SOURCE = "BBBBB";

    @Inject
    private MultipleChoiceAnswerRepository multipleChoiceAnswerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMultipleChoiceAnswerMockMvc;

    private MultipleChoiceAnswer multipleChoiceAnswer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MultipleChoiceAnswerResource multipleChoiceAnswerResource = new MultipleChoiceAnswerResource();
        ReflectionTestUtils.setField(multipleChoiceAnswerResource, "multipleChoiceAnswerRepository", multipleChoiceAnswerRepository);
        this.restMultipleChoiceAnswerMockMvc = MockMvcBuilders.standaloneSetup(multipleChoiceAnswerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        multipleChoiceAnswer = new MultipleChoiceAnswer();
        multipleChoiceAnswer.setAnswer(DEFAULT_ANSWER);
        multipleChoiceAnswer.setIsRight(DEFAULT_IS_RIGHT);
        multipleChoiceAnswer.setSource(DEFAULT_SOURCE);
    }

    @Test
    @Transactional
    public void createMultipleChoiceAnswer() throws Exception {
        int databaseSizeBeforeCreate = multipleChoiceAnswerRepository.findAll().size();

        // Create the MultipleChoiceAnswer

        restMultipleChoiceAnswerMockMvc.perform(post("/api/multipleChoiceAnswers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(multipleChoiceAnswer)))
                .andExpect(status().isCreated());

        // Validate the MultipleChoiceAnswer in the database
        List<MultipleChoiceAnswer> multipleChoiceAnswers = multipleChoiceAnswerRepository.findAll();
        assertThat(multipleChoiceAnswers).hasSize(databaseSizeBeforeCreate + 1);
        MultipleChoiceAnswer testMultipleChoiceAnswer = multipleChoiceAnswers.get(multipleChoiceAnswers.size() - 1);
        assertThat(testMultipleChoiceAnswer.getAnswer()).isEqualTo(DEFAULT_ANSWER);
        assertThat(testMultipleChoiceAnswer.getIsRight()).isEqualTo(DEFAULT_IS_RIGHT);
        assertThat(testMultipleChoiceAnswer.getSource()).isEqualTo(DEFAULT_SOURCE);
    }

    @Test
    @Transactional
    public void checkAnswerIsRequired() throws Exception {
        int databaseSizeBeforeTest = multipleChoiceAnswerRepository.findAll().size();
        // set the field null
        multipleChoiceAnswer.setAnswer(null);

        // Create the MultipleChoiceAnswer, which fails.

        restMultipleChoiceAnswerMockMvc.perform(post("/api/multipleChoiceAnswers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(multipleChoiceAnswer)))
                .andExpect(status().isBadRequest());

        List<MultipleChoiceAnswer> multipleChoiceAnswers = multipleChoiceAnswerRepository.findAll();
        assertThat(multipleChoiceAnswers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsRightIsRequired() throws Exception {
        int databaseSizeBeforeTest = multipleChoiceAnswerRepository.findAll().size();
        // set the field null
        multipleChoiceAnswer.setIsRight(null);

        // Create the MultipleChoiceAnswer, which fails.

        restMultipleChoiceAnswerMockMvc.perform(post("/api/multipleChoiceAnswers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(multipleChoiceAnswer)))
                .andExpect(status().isBadRequest());

        List<MultipleChoiceAnswer> multipleChoiceAnswers = multipleChoiceAnswerRepository.findAll();
        assertThat(multipleChoiceAnswers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMultipleChoiceAnswers() throws Exception {
        // Initialize the database
        multipleChoiceAnswerRepository.saveAndFlush(multipleChoiceAnswer);

        // Get all the multipleChoiceAnswers
        restMultipleChoiceAnswerMockMvc.perform(get("/api/multipleChoiceAnswers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(multipleChoiceAnswer.getId().intValue())))
                .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER.toString())))
                .andExpect(jsonPath("$.[*].isRight").value(hasItem(DEFAULT_IS_RIGHT.booleanValue())))
                .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())));
    }

    @Test
    @Transactional
    public void getMultipleChoiceAnswer() throws Exception {
        // Initialize the database
        multipleChoiceAnswerRepository.saveAndFlush(multipleChoiceAnswer);

        // Get the multipleChoiceAnswer
        restMultipleChoiceAnswerMockMvc.perform(get("/api/multipleChoiceAnswers/{id}", multipleChoiceAnswer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(multipleChoiceAnswer.getId().intValue()))
            .andExpect(jsonPath("$.answer").value(DEFAULT_ANSWER.toString()))
            .andExpect(jsonPath("$.isRight").value(DEFAULT_IS_RIGHT.booleanValue()))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMultipleChoiceAnswer() throws Exception {
        // Get the multipleChoiceAnswer
        restMultipleChoiceAnswerMockMvc.perform(get("/api/multipleChoiceAnswers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMultipleChoiceAnswer() throws Exception {
        // Initialize the database
        multipleChoiceAnswerRepository.saveAndFlush(multipleChoiceAnswer);

		int databaseSizeBeforeUpdate = multipleChoiceAnswerRepository.findAll().size();

        // Update the multipleChoiceAnswer
        multipleChoiceAnswer.setAnswer(UPDATED_ANSWER);
        multipleChoiceAnswer.setIsRight(UPDATED_IS_RIGHT);
        multipleChoiceAnswer.setSource(UPDATED_SOURCE);

        restMultipleChoiceAnswerMockMvc.perform(put("/api/multipleChoiceAnswers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(multipleChoiceAnswer)))
                .andExpect(status().isOk());

        // Validate the MultipleChoiceAnswer in the database
        List<MultipleChoiceAnswer> multipleChoiceAnswers = multipleChoiceAnswerRepository.findAll();
        assertThat(multipleChoiceAnswers).hasSize(databaseSizeBeforeUpdate);
        MultipleChoiceAnswer testMultipleChoiceAnswer = multipleChoiceAnswers.get(multipleChoiceAnswers.size() - 1);
        assertThat(testMultipleChoiceAnswer.getAnswer()).isEqualTo(UPDATED_ANSWER);
        assertThat(testMultipleChoiceAnswer.getIsRight()).isEqualTo(UPDATED_IS_RIGHT);
        assertThat(testMultipleChoiceAnswer.getSource()).isEqualTo(UPDATED_SOURCE);
    }

    @Test
    @Transactional
    public void deleteMultipleChoiceAnswer() throws Exception {
        // Initialize the database
        multipleChoiceAnswerRepository.saveAndFlush(multipleChoiceAnswer);

		int databaseSizeBeforeDelete = multipleChoiceAnswerRepository.findAll().size();

        // Get the multipleChoiceAnswer
        restMultipleChoiceAnswerMockMvc.perform(delete("/api/multipleChoiceAnswers/{id}", multipleChoiceAnswer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<MultipleChoiceAnswer> multipleChoiceAnswers = multipleChoiceAnswerRepository.findAll();
        assertThat(multipleChoiceAnswers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
