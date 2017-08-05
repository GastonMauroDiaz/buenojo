package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.HangManExerciseOption;
import com.ciis.buenojo.repository.HangManExerciseOptionRepository;

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
 * Test class for the HangManExerciseOptionResource REST controller.
 *
 * @see HangManExerciseOptionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HangManExerciseOptionResourceIntTest {

    private static final String DEFAULT_TEXT = "AA";
    private static final String UPDATED_TEXT = "BB";

    private static final Boolean DEFAULT_IS_CORRECT = false;
    private static final Boolean UPDATED_IS_CORRECT = true;

    @Inject
    private HangManExerciseOptionRepository hangManExerciseOptionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restHangManExerciseOptionMockMvc;

    private HangManExerciseOption hangManExerciseOption;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HangManExerciseOptionResource hangManExerciseOptionResource = new HangManExerciseOptionResource();
        ReflectionTestUtils.setField(hangManExerciseOptionResource, "hangManExerciseOptionRepository", hangManExerciseOptionRepository);
        this.restHangManExerciseOptionMockMvc = MockMvcBuilders.standaloneSetup(hangManExerciseOptionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        hangManExerciseOption = new HangManExerciseOption();
        hangManExerciseOption.setText(DEFAULT_TEXT);
        hangManExerciseOption.setIsCorrect(DEFAULT_IS_CORRECT);
    }

    @Test
    @Transactional
    public void createHangManExerciseOption() throws Exception {
        int databaseSizeBeforeCreate = hangManExerciseOptionRepository.findAll().size();

        // Create the HangManExerciseOption

        restHangManExerciseOptionMockMvc.perform(post("/api/hangManExerciseOptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hangManExerciseOption)))
                .andExpect(status().isCreated());

        // Validate the HangManExerciseOption in the database
        List<HangManExerciseOption> hangManExerciseOptions = hangManExerciseOptionRepository.findAll();
        assertThat(hangManExerciseOptions).hasSize(databaseSizeBeforeCreate + 1);
        HangManExerciseOption testHangManExerciseOption = hangManExerciseOptions.get(hangManExerciseOptions.size() - 1);
        assertThat(testHangManExerciseOption.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testHangManExerciseOption.getIsCorrect()).isEqualTo(DEFAULT_IS_CORRECT);
    }

    @Test
    @Transactional
    public void checkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = hangManExerciseOptionRepository.findAll().size();
        // set the field null
        hangManExerciseOption.setText(null);

        // Create the HangManExerciseOption, which fails.

        restHangManExerciseOptionMockMvc.perform(post("/api/hangManExerciseOptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hangManExerciseOption)))
                .andExpect(status().isBadRequest());

        List<HangManExerciseOption> hangManExerciseOptions = hangManExerciseOptionRepository.findAll();
        assertThat(hangManExerciseOptions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsCorrectIsRequired() throws Exception {
        int databaseSizeBeforeTest = hangManExerciseOptionRepository.findAll().size();
        // set the field null
        hangManExerciseOption.setIsCorrect(null);

        // Create the HangManExerciseOption, which fails.

        restHangManExerciseOptionMockMvc.perform(post("/api/hangManExerciseOptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hangManExerciseOption)))
                .andExpect(status().isBadRequest());

        List<HangManExerciseOption> hangManExerciseOptions = hangManExerciseOptionRepository.findAll();
        assertThat(hangManExerciseOptions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHangManExerciseOptions() throws Exception {
        // Initialize the database
        hangManExerciseOptionRepository.saveAndFlush(hangManExerciseOption);

        // Get all the hangManExerciseOptions
        restHangManExerciseOptionMockMvc.perform(get("/api/hangManExerciseOptions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(hangManExerciseOption.getId().intValue())))
                .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
                .andExpect(jsonPath("$.[*].isCorrect").value(hasItem(DEFAULT_IS_CORRECT.booleanValue())));
    }

    @Test
    @Transactional
    public void getHangManExerciseOption() throws Exception {
        // Initialize the database
        hangManExerciseOptionRepository.saveAndFlush(hangManExerciseOption);

        // Get the hangManExerciseOption
        restHangManExerciseOptionMockMvc.perform(get("/api/hangManExerciseOptions/{id}", hangManExerciseOption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(hangManExerciseOption.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.isCorrect").value(DEFAULT_IS_CORRECT.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingHangManExerciseOption() throws Exception {
        // Get the hangManExerciseOption
        restHangManExerciseOptionMockMvc.perform(get("/api/hangManExerciseOptions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHangManExerciseOption() throws Exception {
        // Initialize the database
        hangManExerciseOptionRepository.saveAndFlush(hangManExerciseOption);

		int databaseSizeBeforeUpdate = hangManExerciseOptionRepository.findAll().size();

        // Update the hangManExerciseOption
        hangManExerciseOption.setText(UPDATED_TEXT);
        hangManExerciseOption.setIsCorrect(UPDATED_IS_CORRECT);

        restHangManExerciseOptionMockMvc.perform(put("/api/hangManExerciseOptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hangManExerciseOption)))
                .andExpect(status().isOk());

        // Validate the HangManExerciseOption in the database
        List<HangManExerciseOption> hangManExerciseOptions = hangManExerciseOptionRepository.findAll();
        assertThat(hangManExerciseOptions).hasSize(databaseSizeBeforeUpdate);
        HangManExerciseOption testHangManExerciseOption = hangManExerciseOptions.get(hangManExerciseOptions.size() - 1);
        assertThat(testHangManExerciseOption.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testHangManExerciseOption.getIsCorrect()).isEqualTo(UPDATED_IS_CORRECT);
    }

    @Test
    @Transactional
    public void deleteHangManExerciseOption() throws Exception {
        // Initialize the database
        hangManExerciseOptionRepository.saveAndFlush(hangManExerciseOption);

		int databaseSizeBeforeDelete = hangManExerciseOptionRepository.findAll().size();

        // Get the hangManExerciseOption
        restHangManExerciseOptionMockMvc.perform(delete("/api/hangManExerciseOptions/{id}", hangManExerciseOption.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<HangManExerciseOption> hangManExerciseOptions = hangManExerciseOptionRepository.findAll();
        assertThat(hangManExerciseOptions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
