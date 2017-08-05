package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.Enrollment;
import com.ciis.buenojo.repository.EnrollmentRepository;

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

import com.ciis.buenojo.domain.enumeration.EnrollmentStatus;

/**
 * Test class for the EnrollmentResource REST controller.
 *
 * @see EnrollmentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EnrollmentResourceTest {



private static final EnrollmentStatus DEFAULT_STATUS = EnrollmentStatus.Started;
    private static final EnrollmentStatus UPDATED_STATUS = EnrollmentStatus.InProgress;

    @Inject
    private EnrollmentRepository enrollmentRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEnrollmentMockMvc;

    private Enrollment enrollment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EnrollmentResource enrollmentResource = new EnrollmentResource();
        ReflectionTestUtils.setField(enrollmentResource, "enrollmentRepository", enrollmentRepository);
        this.restEnrollmentMockMvc = MockMvcBuilders.standaloneSetup(enrollmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        enrollment = new Enrollment();
        enrollment.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createEnrollment() throws Exception {
        int databaseSizeBeforeCreate = enrollmentRepository.findAll().size();

        // Create the Enrollment

        restEnrollmentMockMvc.perform(post("/api/enrollments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(enrollment)))
                .andExpect(status().isCreated());

        // Validate the Enrollment in the database
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        assertThat(enrollments).hasSize(databaseSizeBeforeCreate + 1);
        Enrollment testEnrollment = enrollments.get(enrollments.size() - 1);
        assertThat(testEnrollment.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void getAllEnrollments() throws Exception {
        // Initialize the database
        enrollmentRepository.saveAndFlush(enrollment);

        // Get all the enrollments
        restEnrollmentMockMvc.perform(get("/api/enrollments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(enrollment.getId().intValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getEnrollment() throws Exception {
        // Initialize the database
        enrollmentRepository.saveAndFlush(enrollment);

        // Get the enrollment
        restEnrollmentMockMvc.perform(get("/api/enrollments/{id}", enrollment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(enrollment.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEnrollment() throws Exception {
        // Get the enrollment
        restEnrollmentMockMvc.perform(get("/api/enrollments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEnrollment() throws Exception {
        // Initialize the database
        enrollmentRepository.saveAndFlush(enrollment);

		int databaseSizeBeforeUpdate = enrollmentRepository.findAll().size();

        // Update the enrollment
        enrollment.setStatus(UPDATED_STATUS);

        restEnrollmentMockMvc.perform(put("/api/enrollments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(enrollment)))
                .andExpect(status().isOk());

        // Validate the Enrollment in the database
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        assertThat(enrollments).hasSize(databaseSizeBeforeUpdate);
        Enrollment testEnrollment = enrollments.get(enrollments.size() - 1);
        assertThat(testEnrollment.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteEnrollment() throws Exception {
        // Initialize the database
        enrollmentRepository.saveAndFlush(enrollment);

		int databaseSizeBeforeDelete = enrollmentRepository.findAll().size();

        // Get the enrollment
        restEnrollmentMockMvc.perform(delete("/api/enrollments/{id}", enrollment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        assertThat(enrollments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
