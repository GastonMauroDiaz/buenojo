package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.LoaderTrace;
import com.ciis.buenojo.repository.LoaderTraceRepository;

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

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ciis.buenojo.domain.enumeration.LoaderResult;
import com.ciis.buenojo.domain.enumeration.LoaderType;

/**
 * Test class for the LoaderTraceResource REST controller.
 *
 * @see LoaderTraceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class LoaderTraceResourceTest {

//    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");



private static final LoaderResult DEFAULT_LOADER_RESULT = LoaderResult.Done;
    private static final LoaderResult UPDATED_LOADER_RESULT = LoaderResult.Failed;
    private static final String DEFAULT_AUTHOR = "AAAAA";
    private static final String UPDATED_AUTHOR = "BBBBB";


private static final LoaderType DEFAULT_LOADER_TYPE = LoaderType.ImageCompletion;
    private static final LoaderType UPDATED_LOADER_TYPE = LoaderType.PhotoLocation;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.now();
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.from(DEFAULT_DATE);
//    private static final String DEFAULT_DATE_STR = dateTimeFormatter.print(DEFAULT_DATE);
    private static final String DEFAULT_RESULT_LOG = "AAAAA";
    private static final String UPDATED_RESULT_LOG = "BBBBB";
    private static final String DEFAULT_DATASET_NAME = "AAAAA";
    private static final String UPDATED_DATASET_NAME = "BBBBB";

    @Inject
    private LoaderTraceRepository loaderTraceRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLoaderTraceMockMvc;

    private LoaderTrace loaderTrace;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LoaderTraceResource loaderTraceResource = new LoaderTraceResource();
        ReflectionTestUtils.setField(loaderTraceResource, "loaderTraceRepository", loaderTraceRepository);
        this.restLoaderTraceMockMvc = MockMvcBuilders.standaloneSetup(loaderTraceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        loaderTrace = new LoaderTrace();
        loaderTrace.setLoaderResult(DEFAULT_LOADER_RESULT);
        loaderTrace.setAuthor(DEFAULT_AUTHOR);
        loaderTrace.setLoaderType(DEFAULT_LOADER_TYPE);
        loaderTrace.setDate(DEFAULT_DATE);
        loaderTrace.setResultLog(DEFAULT_RESULT_LOG);
        loaderTrace.setDatasetName(DEFAULT_DATASET_NAME);
    }

    @Test
    @Transactional
    public void createLoaderTrace() throws Exception {
        int databaseSizeBeforeCreate = loaderTraceRepository.findAll().size();

        // Create the LoaderTrace

        restLoaderTraceMockMvc.perform(post("/api/loaderTraces")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(loaderTrace)))
                .andExpect(status().isCreated());

        // Validate the LoaderTrace in the database
        List<LoaderTrace> loaderTraces = loaderTraceRepository.findAll();
        assertThat(loaderTraces).hasSize(databaseSizeBeforeCreate + 1);
        LoaderTrace testLoaderTrace = loaderTraces.get(loaderTraces.size() - 1);
        assertThat(testLoaderTrace.getLoaderResult()).isEqualTo(DEFAULT_LOADER_RESULT);
        assertThat(testLoaderTrace.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testLoaderTrace.getLoaderType()).isEqualTo(DEFAULT_LOADER_TYPE);
        assertThat(testLoaderTrace.getDate().isEqual(DEFAULT_DATE));
        assertThat(testLoaderTrace.getResultLog()).isEqualTo(DEFAULT_RESULT_LOG);
        assertThat(testLoaderTrace.getDatasetName()).isEqualTo(DEFAULT_DATASET_NAME);
    }

    @Test
    @Transactional
    public void checkLoaderResultIsRequired() throws Exception {
        int databaseSizeBeforeTest = loaderTraceRepository.findAll().size();
        // set the field null
        loaderTrace.setLoaderResult(null);

        // Create the LoaderTrace, which fails.

        restLoaderTraceMockMvc.perform(post("/api/loaderTraces")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(loaderTrace)))
                .andExpect(status().isBadRequest());

        List<LoaderTrace> loaderTraces = loaderTraceRepository.findAll();
        assertThat(loaderTraces).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAuthorIsRequired() throws Exception {
        int databaseSizeBeforeTest = loaderTraceRepository.findAll().size();
        // set the field null
        loaderTrace.setAuthor(null);

        // Create the LoaderTrace, which fails.

        restLoaderTraceMockMvc.perform(post("/api/loaderTraces")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(loaderTrace)))
                .andExpect(status().isBadRequest());

        List<LoaderTrace> loaderTraces = loaderTraceRepository.findAll();
        assertThat(loaderTraces).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLoaderTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = loaderTraceRepository.findAll().size();
        // set the field null
        loaderTrace.setLoaderType(null);

        // Create the LoaderTrace, which fails.

        restLoaderTraceMockMvc.perform(post("/api/loaderTraces")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(loaderTrace)))
                .andExpect(status().isBadRequest());

        List<LoaderTrace> loaderTraces = loaderTraceRepository.findAll();
        assertThat(loaderTraces).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = loaderTraceRepository.findAll().size();
        // set the field null
        loaderTrace.setDate(null);

        // Create the LoaderTrace, which fails.

        restLoaderTraceMockMvc.perform(post("/api/loaderTraces")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(loaderTrace)))
                .andExpect(status().isBadRequest());

        List<LoaderTrace> loaderTraces = loaderTraceRepository.findAll();
        assertThat(loaderTraces).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDatasetNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = loaderTraceRepository.findAll().size();
        // set the field null
        loaderTrace.setDatasetName(null);

        // Create the LoaderTrace, which fails.

        restLoaderTraceMockMvc.perform(post("/api/loaderTraces")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(loaderTrace)))
                .andExpect(status().isBadRequest());

        List<LoaderTrace> loaderTraces = loaderTraceRepository.findAll();
        assertThat(loaderTraces).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLoaderTraces() throws Exception {
        // Initialize the database
        loaderTraceRepository.saveAndFlush(loaderTrace);

        // Get all the loaderTraces
        restLoaderTraceMockMvc.perform(get("/api/loaderTraces"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(loaderTrace.getId().intValue())))
                .andExpect(jsonPath("$.[*].loaderResult").value(hasItem(DEFAULT_LOADER_RESULT.toString())))
                .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR.toString())))
                .andExpect(jsonPath("$.[*].loaderType").value(hasItem(DEFAULT_LOADER_TYPE.toString())))
                
                .andExpect(jsonPath("$.[*].resultLog").value(hasItem(DEFAULT_RESULT_LOG.toString())))
                .andExpect(jsonPath("$.[*].datasetName").value(hasItem(DEFAULT_DATASET_NAME.toString())));
    }

    @Test
    @Transactional
    public void getLoaderTrace() throws Exception {
        // Initialize the database
        loaderTraceRepository.saveAndFlush(loaderTrace);

        // Get the loaderTrace
        restLoaderTraceMockMvc.perform(get("/api/loaderTraces/{id}", loaderTrace.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(loaderTrace.getId().intValue()))
            .andExpect(jsonPath("$.loaderResult").value(DEFAULT_LOADER_RESULT.toString()))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR.toString()))
            .andExpect(jsonPath("$.loaderType").value(DEFAULT_LOADER_TYPE.toString()))
            
            .andExpect(jsonPath("$.resultLog").value(DEFAULT_RESULT_LOG.toString()))
            .andExpect(jsonPath("$.datasetName").value(DEFAULT_DATASET_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLoaderTrace() throws Exception {
        // Get the loaderTrace
        restLoaderTraceMockMvc.perform(get("/api/loaderTraces/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLoaderTrace() throws Exception {
        // Initialize the database
        loaderTraceRepository.saveAndFlush(loaderTrace);

		int databaseSizeBeforeUpdate = loaderTraceRepository.findAll().size();

        // Update the loaderTrace
        loaderTrace.setLoaderResult(UPDATED_LOADER_RESULT);
        loaderTrace.setAuthor(UPDATED_AUTHOR);
        loaderTrace.setLoaderType(UPDATED_LOADER_TYPE);
        loaderTrace.setDate(UPDATED_DATE);
        loaderTrace.setResultLog(UPDATED_RESULT_LOG);
        loaderTrace.setDatasetName(UPDATED_DATASET_NAME);

        restLoaderTraceMockMvc.perform(put("/api/loaderTraces")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(loaderTrace)))
                .andExpect(status().isOk());

        // Validate the LoaderTrace in the database
        List<LoaderTrace> loaderTraces = loaderTraceRepository.findAll();
        assertThat(loaderTraces).hasSize(databaseSizeBeforeUpdate);
        LoaderTrace testLoaderTrace = loaderTraces.get(loaderTraces.size() - 1);
        assertThat(testLoaderTrace.getLoaderResult()).isEqualTo(UPDATED_LOADER_RESULT);
        assertThat(testLoaderTrace.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testLoaderTrace.getLoaderType()).isEqualTo(UPDATED_LOADER_TYPE);
        assertThat(testLoaderTrace.getDate().isEqual(UPDATED_DATE));
        assertThat(testLoaderTrace.getResultLog()).isEqualTo(UPDATED_RESULT_LOG);
        assertThat(testLoaderTrace.getDatasetName()).isEqualTo(UPDATED_DATASET_NAME);
    }

    @Test
    @Transactional
    public void deleteLoaderTrace() throws Exception {
        // Initialize the database
        loaderTraceRepository.saveAndFlush(loaderTrace);

		int databaseSizeBeforeDelete = loaderTraceRepository.findAll().size();

        // Get the loaderTrace
        restLoaderTraceMockMvc.perform(delete("/api/loaderTraces/{id}", loaderTrace.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<LoaderTrace> loaderTraces = loaderTraceRepository.findAll();
        assertThat(loaderTraces).hasSize(databaseSizeBeforeDelete - 1);
    }
}
