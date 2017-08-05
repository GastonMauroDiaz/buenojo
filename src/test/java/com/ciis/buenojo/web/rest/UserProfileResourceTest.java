package com.ciis.buenojo.web.rest;

import com.ciis.buenojo.Application;
import com.ciis.buenojo.domain.UserProfile;
import com.ciis.buenojo.repository.UserProfileRepository;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the UserProfileResource REST controller.
 *
 * @see UserProfileResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UserProfileResourceTest {

    private static final String DEFAULT_PHONES = "AAAAA";
    private static final String UPDATED_PHONES = "BBBBB";
    private static final String DEFAULT_ADDRESS = "AAAAA";
    private static final String UPDATED_ADDRESS = "BBBBB";

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    @Inject
    private UserProfileRepository userProfileRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUserProfileMockMvc;

    private UserProfile userProfile;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserProfileResource userProfileResource = new UserProfileResource();
        ReflectionTestUtils.setField(userProfileResource, "userProfileRepository", userProfileRepository);
        this.restUserProfileMockMvc = MockMvcBuilders.standaloneSetup(userProfileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        userProfile = new UserProfile();
        userProfile.setPhones(DEFAULT_PHONES);
        userProfile.setAddress(DEFAULT_ADDRESS);
        userProfile.setPicture(DEFAULT_PICTURE);
        userProfile.setPictureContentType(DEFAULT_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createUserProfile() throws Exception {
        int databaseSizeBeforeCreate = userProfileRepository.findAll().size();

        // Create the UserProfile

        restUserProfileMockMvc.perform(post("/api/userProfiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userProfile)))
                .andExpect(status().isCreated());

        // Validate the UserProfile in the database
        List<UserProfile> userProfiles = userProfileRepository.findAll();
        assertThat(userProfiles).hasSize(databaseSizeBeforeCreate + 1);
        UserProfile testUserProfile = userProfiles.get(userProfiles.size() - 1);
        assertThat(testUserProfile.getPhones()).isEqualTo(DEFAULT_PHONES);
        assertThat(testUserProfile.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testUserProfile.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testUserProfile.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllUserProfiles() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get all the userProfiles
        restUserProfileMockMvc.perform(get("/api/userProfiles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userProfile.getId().intValue())))
                .andExpect(jsonPath("$.[*].phones").value(hasItem(DEFAULT_PHONES.toString())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))));
    }

    @Test
    @Transactional
    public void getUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

        // Get the userProfile
        restUserProfileMockMvc.perform(get("/api/userProfiles/{id}", userProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userProfile.getId().intValue()))
            .andExpect(jsonPath("$.phones").value(DEFAULT_PHONES.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)));
    }

    @Test
    @Transactional
    public void getNonExistingUserProfile() throws Exception {
        // Get the userProfile
        restUserProfileMockMvc.perform(get("/api/userProfiles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

		int databaseSizeBeforeUpdate = userProfileRepository.findAll().size();

        // Update the userProfile
        userProfile.setPhones(UPDATED_PHONES);
        userProfile.setAddress(UPDATED_ADDRESS);
        userProfile.setPicture(UPDATED_PICTURE);
        userProfile.setPictureContentType(UPDATED_PICTURE_CONTENT_TYPE);

        restUserProfileMockMvc.perform(put("/api/userProfiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userProfile)))
                .andExpect(status().isOk());

        // Validate the UserProfile in the database
        List<UserProfile> userProfiles = userProfileRepository.findAll();
        assertThat(userProfiles).hasSize(databaseSizeBeforeUpdate);
        UserProfile testUserProfile = userProfiles.get(userProfiles.size() - 1);
        assertThat(testUserProfile.getPhones()).isEqualTo(UPDATED_PHONES);
        assertThat(testUserProfile.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testUserProfile.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testUserProfile.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void deleteUserProfile() throws Exception {
        // Initialize the database
        userProfileRepository.saveAndFlush(userProfile);

		int databaseSizeBeforeDelete = userProfileRepository.findAll().size();

        // Get the userProfile
        restUserProfileMockMvc.perform(delete("/api/userProfiles/{id}", userProfile.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserProfile> userProfiles = userProfileRepository.findAll();
        assertThat(userProfiles).hasSize(databaseSizeBeforeDelete - 1);
    }
}
