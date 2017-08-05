package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.HangManGameContainer;
import com.ciis.buenojo.repository.HangManGameContainerRepository;

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
 * Test class for the HangManGameContainerResource REST controller.
 *
 * @see HangManGameContainerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HangManGameContainerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private HangManGameContainerRepository hangManGameContainerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restHangManGameContainerMockMvc;

    private HangManGameContainer hangManGameContainer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HangManGameContainerResource hangManGameContainerResource = new HangManGameContainerResource();
        ReflectionTestUtils.setField(hangManGameContainerResource, "hangManGameContainerRepository", hangManGameContainerRepository);
        this.restHangManGameContainerMockMvc = MockMvcBuilders.standaloneSetup(hangManGameContainerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        hangManGameContainer = new HangManGameContainer();
        hangManGameContainer.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createHangManGameContainer() throws Exception {
        int databaseSizeBeforeCreate = hangManGameContainerRepository.findAll().size();

        // Create the HangManGameContainer

        restHangManGameContainerMockMvc.perform(post("/api/hangManGameContainers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hangManGameContainer)))
                .andExpect(status().isCreated());

        // Validate the HangManGameContainer in the database
        List<HangManGameContainer> hangManGameContainers = hangManGameContainerRepository.findAll();
        assertThat(hangManGameContainers).hasSize(databaseSizeBeforeCreate + 1);
        HangManGameContainer testHangManGameContainer = hangManGameContainers.get(hangManGameContainers.size() - 1);
        assertThat(testHangManGameContainer.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = hangManGameContainerRepository.findAll().size();
        // set the field null
        hangManGameContainer.setName(null);

        // Create the HangManGameContainer, which fails.

        restHangManGameContainerMockMvc.perform(post("/api/hangManGameContainers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hangManGameContainer)))
                .andExpect(status().isBadRequest());

        List<HangManGameContainer> hangManGameContainers = hangManGameContainerRepository.findAll();
        assertThat(hangManGameContainers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHangManGameContainers() throws Exception {
        // Initialize the database
        hangManGameContainerRepository.saveAndFlush(hangManGameContainer);

        // Get all the hangManGameContainers
        restHangManGameContainerMockMvc.perform(get("/api/hangManGameContainers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(hangManGameContainer.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getHangManGameContainer() throws Exception {
        // Initialize the database
        hangManGameContainerRepository.saveAndFlush(hangManGameContainer);

        // Get the hangManGameContainer
        restHangManGameContainerMockMvc.perform(get("/api/hangManGameContainers/{id}", hangManGameContainer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(hangManGameContainer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHangManGameContainer() throws Exception {
        // Get the hangManGameContainer
        restHangManGameContainerMockMvc.perform(get("/api/hangManGameContainers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHangManGameContainer() throws Exception {
        // Initialize the database
        hangManGameContainerRepository.saveAndFlush(hangManGameContainer);

		int databaseSizeBeforeUpdate = hangManGameContainerRepository.findAll().size();

        // Update the hangManGameContainer
        hangManGameContainer.setName(UPDATED_NAME);

        restHangManGameContainerMockMvc.perform(put("/api/hangManGameContainers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hangManGameContainer)))
                .andExpect(status().isOk());

        // Validate the HangManGameContainer in the database
        List<HangManGameContainer> hangManGameContainers = hangManGameContainerRepository.findAll();
        assertThat(hangManGameContainers).hasSize(databaseSizeBeforeUpdate);
        HangManGameContainer testHangManGameContainer = hangManGameContainers.get(hangManGameContainers.size() - 1);
        assertThat(testHangManGameContainer.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteHangManGameContainer() throws Exception {
        // Initialize the database
        hangManGameContainerRepository.saveAndFlush(hangManGameContainer);

		int databaseSizeBeforeDelete = hangManGameContainerRepository.findAll().size();

        // Get the hangManGameContainer
        restHangManGameContainerMockMvc.perform(delete("/api/hangManGameContainers/{id}", hangManGameContainer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<HangManGameContainer> hangManGameContainers = hangManGameContainerRepository.findAll();
        assertThat(hangManGameContainers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
