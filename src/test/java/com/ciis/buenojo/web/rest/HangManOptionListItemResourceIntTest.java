package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.HangManOptionListItem;
import com.ciis.buenojo.repository.HangManOptionListItemRepository;

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
 * Test class for the HangManOptionListItemResource REST controller.
 *
 * @see HangManOptionListItemResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HangManOptionListItemResourceIntTest {

    private static final String DEFAULT_OPTION_GROUP = "AAAAA";
    private static final String UPDATED_OPTION_GROUP = "BBBBB";
    private static final String DEFAULT_OPTION_TYPE = "AAAAA";
    private static final String UPDATED_OPTION_TYPE = "BBBBB";
    private static final String DEFAULT_OPTION_TEXT = "AA";
    private static final String UPDATED_OPTION_TEXT = "BB";

    @Inject
    private HangManOptionListItemRepository hangManOptionListItemRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restHangManOptionListItemMockMvc;

    private HangManOptionListItem hangManOptionListItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HangManOptionListItemResource hangManOptionListItemResource = new HangManOptionListItemResource();
        ReflectionTestUtils.setField(hangManOptionListItemResource, "hangManOptionListItemRepository", hangManOptionListItemRepository);
        this.restHangManOptionListItemMockMvc = MockMvcBuilders.standaloneSetup(hangManOptionListItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        hangManOptionListItem = new HangManOptionListItem();
        hangManOptionListItem.setOptionGroup(DEFAULT_OPTION_GROUP);
        hangManOptionListItem.setOptionType(DEFAULT_OPTION_TYPE);
        hangManOptionListItem.setOptionText(DEFAULT_OPTION_TEXT);
    }

    @Test
    @Transactional
    public void createHangManOptionListItem() throws Exception {
        int databaseSizeBeforeCreate = hangManOptionListItemRepository.findAll().size();

        // Create the HangManOptionListItem

        restHangManOptionListItemMockMvc.perform(post("/api/hangManOptionListItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hangManOptionListItem)))
                .andExpect(status().isCreated());

        // Validate the HangManOptionListItem in the database
        List<HangManOptionListItem> hangManOptionListItems = hangManOptionListItemRepository.findAll();
        assertThat(hangManOptionListItems).hasSize(databaseSizeBeforeCreate + 1);
        HangManOptionListItem testHangManOptionListItem = hangManOptionListItems.get(hangManOptionListItems.size() - 1);
        assertThat(testHangManOptionListItem.getOptionGroup()).isEqualTo(DEFAULT_OPTION_GROUP);
        assertThat(testHangManOptionListItem.getOptionType()).isEqualTo(DEFAULT_OPTION_TYPE);
        assertThat(testHangManOptionListItem.getOptionText()).isEqualTo(DEFAULT_OPTION_TEXT);
    }

    @Test
    @Transactional
    public void checkOptionGroupIsRequired() throws Exception {
        int databaseSizeBeforeTest = hangManOptionListItemRepository.findAll().size();
        // set the field null
        hangManOptionListItem.setOptionGroup(null);

        // Create the HangManOptionListItem, which fails.

        restHangManOptionListItemMockMvc.perform(post("/api/hangManOptionListItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hangManOptionListItem)))
                .andExpect(status().isBadRequest());

        List<HangManOptionListItem> hangManOptionListItems = hangManOptionListItemRepository.findAll();
        assertThat(hangManOptionListItems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOptionTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = hangManOptionListItemRepository.findAll().size();
        // set the field null
        hangManOptionListItem.setOptionText(null);

        // Create the HangManOptionListItem, which fails.

        restHangManOptionListItemMockMvc.perform(post("/api/hangManOptionListItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hangManOptionListItem)))
                .andExpect(status().isBadRequest());

        List<HangManOptionListItem> hangManOptionListItems = hangManOptionListItemRepository.findAll();
        assertThat(hangManOptionListItems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHangManOptionListItems() throws Exception {
        // Initialize the database
        hangManOptionListItemRepository.saveAndFlush(hangManOptionListItem);

        // Get all the hangManOptionListItems
        restHangManOptionListItemMockMvc.perform(get("/api/hangManOptionListItems"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(hangManOptionListItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].optionGroup").value(hasItem(DEFAULT_OPTION_GROUP.toString())))
                .andExpect(jsonPath("$.[*].optionType").value(hasItem(DEFAULT_OPTION_TYPE.toString())))
                .andExpect(jsonPath("$.[*].optionText").value(hasItem(DEFAULT_OPTION_TEXT.toString())));
    }

    @Test
    @Transactional
    public void getHangManOptionListItem() throws Exception {
        // Initialize the database
        hangManOptionListItemRepository.saveAndFlush(hangManOptionListItem);

        // Get the hangManOptionListItem
        restHangManOptionListItemMockMvc.perform(get("/api/hangManOptionListItems/{id}", hangManOptionListItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(hangManOptionListItem.getId().intValue()))
            .andExpect(jsonPath("$.optionGroup").value(DEFAULT_OPTION_GROUP.toString()))
            .andExpect(jsonPath("$.optionType").value(DEFAULT_OPTION_TYPE.toString()))
            .andExpect(jsonPath("$.optionText").value(DEFAULT_OPTION_TEXT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHangManOptionListItem() throws Exception {
        // Get the hangManOptionListItem
        restHangManOptionListItemMockMvc.perform(get("/api/hangManOptionListItems/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHangManOptionListItem() throws Exception {
        // Initialize the database
        hangManOptionListItemRepository.saveAndFlush(hangManOptionListItem);

		int databaseSizeBeforeUpdate = hangManOptionListItemRepository.findAll().size();

        // Update the hangManOptionListItem
        hangManOptionListItem.setOptionGroup(UPDATED_OPTION_GROUP);
        hangManOptionListItem.setOptionType(UPDATED_OPTION_TYPE);
        hangManOptionListItem.setOptionText(UPDATED_OPTION_TEXT);

        restHangManOptionListItemMockMvc.perform(put("/api/hangManOptionListItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hangManOptionListItem)))
                .andExpect(status().isOk());

        // Validate the HangManOptionListItem in the database
        List<HangManOptionListItem> hangManOptionListItems = hangManOptionListItemRepository.findAll();
        assertThat(hangManOptionListItems).hasSize(databaseSizeBeforeUpdate);
        HangManOptionListItem testHangManOptionListItem = hangManOptionListItems.get(hangManOptionListItems.size() - 1);
        assertThat(testHangManOptionListItem.getOptionGroup()).isEqualTo(UPDATED_OPTION_GROUP);
        assertThat(testHangManOptionListItem.getOptionType()).isEqualTo(UPDATED_OPTION_TYPE);
        assertThat(testHangManOptionListItem.getOptionText()).isEqualTo(UPDATED_OPTION_TEXT);
    }

    @Test
    @Transactional
    public void deleteHangManOptionListItem() throws Exception {
        // Initialize the database
        hangManOptionListItemRepository.saveAndFlush(hangManOptionListItem);

		int databaseSizeBeforeDelete = hangManOptionListItemRepository.findAll().size();

        // Get the hangManOptionListItem
        restHangManOptionListItemMockMvc.perform(delete("/api/hangManOptionListItems/{id}", hangManOptionListItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<HangManOptionListItem> hangManOptionListItems = hangManOptionListItemRepository.findAll();
        assertThat(hangManOptionListItems).hasSize(databaseSizeBeforeDelete - 1);
    }
}
