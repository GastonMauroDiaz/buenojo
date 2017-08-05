package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.HangManExerciseHint;
import com.ciis.buenojo.repository.HangManExerciseHintRepository;

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
 * Test class for the HangManExerciseHintResource REST controller.
 *
 * @see HangManExerciseHintResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HangManExerciseHintResourceIntTest {

    private static final String DEFAULT_TEXT = "AAAAA";
    private static final String UPDATED_TEXT = "BBBBB";

    private static final Integer DEFAULT_X = 0;
    private static final Integer UPDATED_X = 1;

    private static final Integer DEFAULT_Y = 0;
    private static final Integer UPDATED_Y = 1;

    @Inject
    private HangManExerciseHintRepository hangManExerciseHintRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restHangManExerciseHintMockMvc;

    private HangManExerciseHint hangManExerciseHint;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HangManExerciseHintResource hangManExerciseHintResource = new HangManExerciseHintResource();
        ReflectionTestUtils.setField(hangManExerciseHintResource, "hangManExerciseHintRepository", hangManExerciseHintRepository);
        this.restHangManExerciseHintMockMvc = MockMvcBuilders.standaloneSetup(hangManExerciseHintResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        hangManExerciseHint = new HangManExerciseHint();
        hangManExerciseHint.setText(DEFAULT_TEXT);
        hangManExerciseHint.setX(DEFAULT_X);
        hangManExerciseHint.setY(DEFAULT_Y);
    }

    @Test
    @Transactional
    public void createHangManExerciseHint() throws Exception {
        int databaseSizeBeforeCreate = hangManExerciseHintRepository.findAll().size();

        // Create the HangManExerciseHint

        restHangManExerciseHintMockMvc.perform(post("/api/hangManExerciseHints")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hangManExerciseHint)))
                .andExpect(status().isCreated());

        // Validate the HangManExerciseHint in the database
        List<HangManExerciseHint> hangManExerciseHints = hangManExerciseHintRepository.findAll();
        assertThat(hangManExerciseHints).hasSize(databaseSizeBeforeCreate + 1);
        HangManExerciseHint testHangManExerciseHint = hangManExerciseHints.get(hangManExerciseHints.size() - 1);
        assertThat(testHangManExerciseHint.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testHangManExerciseHint.getX()).isEqualTo(DEFAULT_X);
        assertThat(testHangManExerciseHint.getY()).isEqualTo(DEFAULT_Y);
    }

    @Test
    @Transactional
    public void checkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = hangManExerciseHintRepository.findAll().size();
        // set the field null
        hangManExerciseHint.setText(null);

        // Create the HangManExerciseHint, which fails.

        restHangManExerciseHintMockMvc.perform(post("/api/hangManExerciseHints")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hangManExerciseHint)))
                .andExpect(status().isBadRequest());

        List<HangManExerciseHint> hangManExerciseHints = hangManExerciseHintRepository.findAll();
        assertThat(hangManExerciseHints).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkXIsRequired() throws Exception {
        int databaseSizeBeforeTest = hangManExerciseHintRepository.findAll().size();
        // set the field null
        hangManExerciseHint.setX(null);

        // Create the HangManExerciseHint, which fails.

        restHangManExerciseHintMockMvc.perform(post("/api/hangManExerciseHints")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hangManExerciseHint)))
                .andExpect(status().isBadRequest());

        List<HangManExerciseHint> hangManExerciseHints = hangManExerciseHintRepository.findAll();
        assertThat(hangManExerciseHints).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYIsRequired() throws Exception {
        int databaseSizeBeforeTest = hangManExerciseHintRepository.findAll().size();
        // set the field null
        hangManExerciseHint.setY(null);

        // Create the HangManExerciseHint, which fails.

        restHangManExerciseHintMockMvc.perform(post("/api/hangManExerciseHints")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hangManExerciseHint)))
                .andExpect(status().isBadRequest());

        List<HangManExerciseHint> hangManExerciseHints = hangManExerciseHintRepository.findAll();
        assertThat(hangManExerciseHints).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHangManExerciseHints() throws Exception {
        // Initialize the database
        hangManExerciseHintRepository.saveAndFlush(hangManExerciseHint);

        // Get all the hangManExerciseHints
        restHangManExerciseHintMockMvc.perform(get("/api/hangManExerciseHints"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(hangManExerciseHint.getId().intValue())))
                .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
                .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X)))
                .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y)));
    }

    @Test
    @Transactional
    public void getHangManExerciseHint() throws Exception {
        // Initialize the database
        hangManExerciseHintRepository.saveAndFlush(hangManExerciseHint);

        // Get the hangManExerciseHint
        restHangManExerciseHintMockMvc.perform(get("/api/hangManExerciseHints/{id}", hangManExerciseHint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(hangManExerciseHint.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.x").value(DEFAULT_X))
            .andExpect(jsonPath("$.y").value(DEFAULT_Y));
    }

    @Test
    @Transactional
    public void getNonExistingHangManExerciseHint() throws Exception {
        // Get the hangManExerciseHint
        restHangManExerciseHintMockMvc.perform(get("/api/hangManExerciseHints/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHangManExerciseHint() throws Exception {
        // Initialize the database
        hangManExerciseHintRepository.saveAndFlush(hangManExerciseHint);

		int databaseSizeBeforeUpdate = hangManExerciseHintRepository.findAll().size();

        // Update the hangManExerciseHint
        hangManExerciseHint.setText(UPDATED_TEXT);
        hangManExerciseHint.setX(UPDATED_X);
        hangManExerciseHint.setY(UPDATED_Y);

        restHangManExerciseHintMockMvc.perform(put("/api/hangManExerciseHints")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hangManExerciseHint)))
                .andExpect(status().isOk());

        // Validate the HangManExerciseHint in the database
        List<HangManExerciseHint> hangManExerciseHints = hangManExerciseHintRepository.findAll();
        assertThat(hangManExerciseHints).hasSize(databaseSizeBeforeUpdate);
        HangManExerciseHint testHangManExerciseHint = hangManExerciseHints.get(hangManExerciseHints.size() - 1);
        assertThat(testHangManExerciseHint.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testHangManExerciseHint.getX()).isEqualTo(UPDATED_X);
        assertThat(testHangManExerciseHint.getY()).isEqualTo(UPDATED_Y);
    }

    @Test
    @Transactional
    public void deleteHangManExerciseHint() throws Exception {
        // Initialize the database
        hangManExerciseHintRepository.saveAndFlush(hangManExerciseHint);

		int databaseSizeBeforeDelete = hangManExerciseHintRepository.findAll().size();

        // Get the hangManExerciseHint
        restHangManExerciseHintMockMvc.perform(delete("/api/hangManExerciseHints/{id}", hangManExerciseHint.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<HangManExerciseHint> hangManExerciseHints = hangManExerciseHintRepository.findAll();
        assertThat(hangManExerciseHints).hasSize(databaseSizeBeforeDelete - 1);
    }
}
