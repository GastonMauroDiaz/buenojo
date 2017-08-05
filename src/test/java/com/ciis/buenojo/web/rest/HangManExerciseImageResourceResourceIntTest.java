package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.HangManExerciseImageResource;
import com.ciis.buenojo.repository.HangManExerciseImageResourceRepository;

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
 * Test class for the HangManExerciseImageResourceResource REST controller.
 *
 * @see HangManExerciseImageResourceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HangManExerciseImageResourceResourceIntTest {


    @Inject
    private HangManExerciseImageResourceRepository hangManExerciseImageResourceRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restHangManExerciseImageResourceMockMvc;

    private HangManExerciseImageResource hangManExerciseImageResource;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HangManExerciseImageResourceResource hangManExerciseImageResourceResource = new HangManExerciseImageResourceResource();
        ReflectionTestUtils.setField(hangManExerciseImageResourceResource, "hangManExerciseImageResourceRepository", hangManExerciseImageResourceRepository);
        this.restHangManExerciseImageResourceMockMvc = MockMvcBuilders.standaloneSetup(hangManExerciseImageResourceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        hangManExerciseImageResource = new HangManExerciseImageResource();
    }

    @Test
    @Transactional
    public void createHangManExerciseImageResource() throws Exception {
        int databaseSizeBeforeCreate = hangManExerciseImageResourceRepository.findAll().size();

        // Create the HangManExerciseImageResource

        restHangManExerciseImageResourceMockMvc.perform(post("/api/hangManExerciseImageResources")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hangManExerciseImageResource)))
                .andExpect(status().isCreated());

        // Validate the HangManExerciseImageResource in the database
        List<HangManExerciseImageResource> hangManExerciseImageResources = hangManExerciseImageResourceRepository.findAll();
        assertThat(hangManExerciseImageResources).hasSize(databaseSizeBeforeCreate + 1);
        HangManExerciseImageResource testHangManExerciseImageResource = hangManExerciseImageResources.get(hangManExerciseImageResources.size() - 1);
    }

    @Test
    @Transactional
    public void getAllHangManExerciseImageResources() throws Exception {
        // Initialize the database
        hangManExerciseImageResourceRepository.saveAndFlush(hangManExerciseImageResource);

        // Get all the hangManExerciseImageResources
        restHangManExerciseImageResourceMockMvc.perform(get("/api/hangManExerciseImageResources"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(hangManExerciseImageResource.getId().intValue())));
    }

    @Test
    @Transactional
    public void getHangManExerciseImageResource() throws Exception {
        // Initialize the database
        hangManExerciseImageResourceRepository.saveAndFlush(hangManExerciseImageResource);

        // Get the hangManExerciseImageResource
        restHangManExerciseImageResourceMockMvc.perform(get("/api/hangManExerciseImageResources/{id}", hangManExerciseImageResource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(hangManExerciseImageResource.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingHangManExerciseImageResource() throws Exception {
        // Get the hangManExerciseImageResource
        restHangManExerciseImageResourceMockMvc.perform(get("/api/hangManExerciseImageResources/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHangManExerciseImageResource() throws Exception {
        // Initialize the database
        hangManExerciseImageResourceRepository.saveAndFlush(hangManExerciseImageResource);

		int databaseSizeBeforeUpdate = hangManExerciseImageResourceRepository.findAll().size();

        // Update the hangManExerciseImageResource

        restHangManExerciseImageResourceMockMvc.perform(put("/api/hangManExerciseImageResources")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hangManExerciseImageResource)))
                .andExpect(status().isOk());

        // Validate the HangManExerciseImageResource in the database
        List<HangManExerciseImageResource> hangManExerciseImageResources = hangManExerciseImageResourceRepository.findAll();
        assertThat(hangManExerciseImageResources).hasSize(databaseSizeBeforeUpdate);
        HangManExerciseImageResource testHangManExerciseImageResource = hangManExerciseImageResources.get(hangManExerciseImageResources.size() - 1);
    }

    @Test
    @Transactional
    public void deleteHangManExerciseImageResource() throws Exception {
        // Initialize the database
        hangManExerciseImageResourceRepository.saveAndFlush(hangManExerciseImageResource);

		int databaseSizeBeforeDelete = hangManExerciseImageResourceRepository.findAll().size();

        // Get the hangManExerciseImageResource
        restHangManExerciseImageResourceMockMvc.perform(delete("/api/hangManExerciseImageResources/{id}", hangManExerciseImageResource.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<HangManExerciseImageResource> hangManExerciseImageResources = hangManExerciseImageResourceRepository.findAll();
        assertThat(hangManExerciseImageResources).hasSize(databaseSizeBeforeDelete - 1);
    }
}
