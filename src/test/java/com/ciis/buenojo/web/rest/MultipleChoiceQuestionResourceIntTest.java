package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.MultipleChoiceQuestion;
import com.ciis.buenojo.repository.MultipleChoiceQuestionRepository;

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

import com.ciis.buenojo.domain.enumeration.MultipleChoiceInteractionTypeEnum;

/**
 * Test class for the MultipleChoiceQuestionResource REST controller.
 *
 * @see MultipleChoiceQuestionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MultipleChoiceQuestionResourceIntTest {

    private static final String DEFAULT_QUESTION = "AAA";
    private static final String UPDATED_QUESTION = "BBB";


private static final MultipleChoiceInteractionTypeEnum DEFAULT_INTERACTION_TYPE = MultipleChoiceInteractionTypeEnum.Type1;
    private static final MultipleChoiceInteractionTypeEnum UPDATED_INTERACTION_TYPE = MultipleChoiceInteractionTypeEnum.Type2;

    private static final Integer DEFAULT_EXERCISE_ID = 1;
    private static final Integer UPDATED_EXERCISE_ID = 2;
    private static final String DEFAULT_SOURCE = "AAAAA";
    private static final String UPDATED_SOURCE = "BBBBB";

    @Inject
    private MultipleChoiceQuestionRepository multipleChoiceQuestionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMultipleChoiceQuestionMockMvc;

    private MultipleChoiceQuestion multipleChoiceQuestion;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MultipleChoiceQuestionResource multipleChoiceQuestionResource = new MultipleChoiceQuestionResource();
        ReflectionTestUtils.setField(multipleChoiceQuestionResource, "multipleChoiceQuestionRepository", multipleChoiceQuestionRepository);
        this.restMultipleChoiceQuestionMockMvc = MockMvcBuilders.standaloneSetup(multipleChoiceQuestionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        multipleChoiceQuestion = new MultipleChoiceQuestion();
        multipleChoiceQuestion.setQuestion(DEFAULT_QUESTION);
        multipleChoiceQuestion.setInteractionType(DEFAULT_INTERACTION_TYPE);
        multipleChoiceQuestion.setExerciseId(DEFAULT_EXERCISE_ID);
        multipleChoiceQuestion.setSource(DEFAULT_SOURCE);
    }

    @Test
    @Transactional
    public void createMultipleChoiceQuestion() throws Exception {
        int databaseSizeBeforeCreate = multipleChoiceQuestionRepository.findAll().size();

        // Create the MultipleChoiceQuestion

        restMultipleChoiceQuestionMockMvc.perform(post("/api/multipleChoiceQuestions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(multipleChoiceQuestion)))
                .andExpect(status().isCreated());

        // Validate the MultipleChoiceQuestion in the database
        List<MultipleChoiceQuestion> multipleChoiceQuestions = multipleChoiceQuestionRepository.findAll();
        assertThat(multipleChoiceQuestions).hasSize(databaseSizeBeforeCreate + 1);
        MultipleChoiceQuestion testMultipleChoiceQuestion = multipleChoiceQuestions.get(multipleChoiceQuestions.size() - 1);
        assertThat(testMultipleChoiceQuestion.getQuestion()).isEqualTo(DEFAULT_QUESTION);
        assertThat(testMultipleChoiceQuestion.getInteractionType()).isEqualTo(DEFAULT_INTERACTION_TYPE);
        assertThat(testMultipleChoiceQuestion.getExerciseId()).isEqualTo(DEFAULT_EXERCISE_ID);
        assertThat(testMultipleChoiceQuestion.getSource()).isEqualTo(DEFAULT_SOURCE);
    }

    @Test
    @Transactional
    public void checkQuestionIsRequired() throws Exception {
        int databaseSizeBeforeTest = multipleChoiceQuestionRepository.findAll().size();
        // set the field null
        multipleChoiceQuestion.setQuestion(null);

        // Create the MultipleChoiceQuestion, which fails.

        restMultipleChoiceQuestionMockMvc.perform(post("/api/multipleChoiceQuestions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(multipleChoiceQuestion)))
                .andExpect(status().isBadRequest());

        List<MultipleChoiceQuestion> multipleChoiceQuestions = multipleChoiceQuestionRepository.findAll();
        assertThat(multipleChoiceQuestions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInteractionTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = multipleChoiceQuestionRepository.findAll().size();
        // set the field null
        multipleChoiceQuestion.setInteractionType(null);

        // Create the MultipleChoiceQuestion, which fails.

        restMultipleChoiceQuestionMockMvc.perform(post("/api/multipleChoiceQuestions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(multipleChoiceQuestion)))
                .andExpect(status().isBadRequest());

        List<MultipleChoiceQuestion> multipleChoiceQuestions = multipleChoiceQuestionRepository.findAll();
        assertThat(multipleChoiceQuestions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMultipleChoiceQuestions() throws Exception {
        // Initialize the database
        multipleChoiceQuestionRepository.saveAndFlush(multipleChoiceQuestion);

        // Get all the multipleChoiceQuestions
        restMultipleChoiceQuestionMockMvc.perform(get("/api/multipleChoiceQuestions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(multipleChoiceQuestion.getId().intValue())))
                .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION.toString())))
                .andExpect(jsonPath("$.[*].interactionType").value(hasItem(DEFAULT_INTERACTION_TYPE.toString())))
                .andExpect(jsonPath("$.[*].exerciseId").value(hasItem(DEFAULT_EXERCISE_ID)))
                .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())));
    }

    @Test
    @Transactional
    public void getMultipleChoiceQuestion() throws Exception {
        // Initialize the database
        multipleChoiceQuestionRepository.saveAndFlush(multipleChoiceQuestion);

        // Get the multipleChoiceQuestion
        restMultipleChoiceQuestionMockMvc.perform(get("/api/multipleChoiceQuestions/{id}", multipleChoiceQuestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(multipleChoiceQuestion.getId().intValue()))
            .andExpect(jsonPath("$.question").value(DEFAULT_QUESTION.toString()))
            .andExpect(jsonPath("$.interactionType").value(DEFAULT_INTERACTION_TYPE.toString()))
            .andExpect(jsonPath("$.exerciseId").value(DEFAULT_EXERCISE_ID))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMultipleChoiceQuestion() throws Exception {
        // Get the multipleChoiceQuestion
        restMultipleChoiceQuestionMockMvc.perform(get("/api/multipleChoiceQuestions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMultipleChoiceQuestion() throws Exception {
        // Initialize the database
        multipleChoiceQuestionRepository.saveAndFlush(multipleChoiceQuestion);

		int databaseSizeBeforeUpdate = multipleChoiceQuestionRepository.findAll().size();

        // Update the multipleChoiceQuestion
        multipleChoiceQuestion.setQuestion(UPDATED_QUESTION);
        multipleChoiceQuestion.setInteractionType(UPDATED_INTERACTION_TYPE);
        multipleChoiceQuestion.setExerciseId(UPDATED_EXERCISE_ID);
        multipleChoiceQuestion.setSource(UPDATED_SOURCE);

        restMultipleChoiceQuestionMockMvc.perform(put("/api/multipleChoiceQuestions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(multipleChoiceQuestion)))
                .andExpect(status().isOk());

        // Validate the MultipleChoiceQuestion in the database
        List<MultipleChoiceQuestion> multipleChoiceQuestions = multipleChoiceQuestionRepository.findAll();
        assertThat(multipleChoiceQuestions).hasSize(databaseSizeBeforeUpdate);
        MultipleChoiceQuestion testMultipleChoiceQuestion = multipleChoiceQuestions.get(multipleChoiceQuestions.size() - 1);
        assertThat(testMultipleChoiceQuestion.getQuestion()).isEqualTo(UPDATED_QUESTION);
        assertThat(testMultipleChoiceQuestion.getInteractionType()).isEqualTo(UPDATED_INTERACTION_TYPE);
        assertThat(testMultipleChoiceQuestion.getExerciseId()).isEqualTo(UPDATED_EXERCISE_ID);
        assertThat(testMultipleChoiceQuestion.getSource()).isEqualTo(UPDATED_SOURCE);
    }

    @Test
    @Transactional
    public void deleteMultipleChoiceQuestion() throws Exception {
        // Initialize the database
        multipleChoiceQuestionRepository.saveAndFlush(multipleChoiceQuestion);

		int databaseSizeBeforeDelete = multipleChoiceQuestionRepository.findAll().size();

        // Get the multipleChoiceQuestion
        restMultipleChoiceQuestionMockMvc.perform(delete("/api/multipleChoiceQuestions/{id}", multipleChoiceQuestion.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<MultipleChoiceQuestion> multipleChoiceQuestions = multipleChoiceQuestionRepository.findAll();
        assertThat(multipleChoiceQuestions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
