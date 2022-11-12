package co.edu.sena.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.sena.IntegrationTest;
import co.edu.sena.domain.SocialSecurity;
import co.edu.sena.repository.SocialSecurityRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SocialSecurityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SocialSecurityResourceIT {

    private static final String DEFAULT_EPS = "AAAAAAAAAA";
    private static final String UPDATED_EPS = "BBBBBBBBBB";

    private static final String DEFAULT_AFP = "AAAAAAAAAA";
    private static final String UPDATED_AFP = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/social-securities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SocialSecurityRepository socialSecurityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSocialSecurityMockMvc;

    private SocialSecurity socialSecurity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SocialSecurity createEntity(EntityManager em) {
        SocialSecurity socialSecurity = new SocialSecurity().eps(DEFAULT_EPS).afp(DEFAULT_AFP);
        return socialSecurity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SocialSecurity createUpdatedEntity(EntityManager em) {
        SocialSecurity socialSecurity = new SocialSecurity().eps(UPDATED_EPS).afp(UPDATED_AFP);
        return socialSecurity;
    }

    @BeforeEach
    public void initTest() {
        socialSecurity = createEntity(em);
    }

    @Test
    @Transactional
    void createSocialSecurity() throws Exception {
        int databaseSizeBeforeCreate = socialSecurityRepository.findAll().size();
        // Create the SocialSecurity
        restSocialSecurityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialSecurity))
            )
            .andExpect(status().isCreated());

        // Validate the SocialSecurity in the database
        List<SocialSecurity> socialSecurityList = socialSecurityRepository.findAll();
        assertThat(socialSecurityList).hasSize(databaseSizeBeforeCreate + 1);
        SocialSecurity testSocialSecurity = socialSecurityList.get(socialSecurityList.size() - 1);
        assertThat(testSocialSecurity.getEps()).isEqualTo(DEFAULT_EPS);
        assertThat(testSocialSecurity.getAfp()).isEqualTo(DEFAULT_AFP);
    }

    @Test
    @Transactional
    void createSocialSecurityWithExistingId() throws Exception {
        // Create the SocialSecurity with an existing ID
        socialSecurity.setId(1L);

        int databaseSizeBeforeCreate = socialSecurityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSocialSecurityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialSecurity))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialSecurity in the database
        List<SocialSecurity> socialSecurityList = socialSecurityRepository.findAll();
        assertThat(socialSecurityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEpsIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialSecurityRepository.findAll().size();
        // set the field null
        socialSecurity.setEps(null);

        // Create the SocialSecurity, which fails.

        restSocialSecurityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialSecurity))
            )
            .andExpect(status().isBadRequest());

        List<SocialSecurity> socialSecurityList = socialSecurityRepository.findAll();
        assertThat(socialSecurityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAfpIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialSecurityRepository.findAll().size();
        // set the field null
        socialSecurity.setAfp(null);

        // Create the SocialSecurity, which fails.

        restSocialSecurityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialSecurity))
            )
            .andExpect(status().isBadRequest());

        List<SocialSecurity> socialSecurityList = socialSecurityRepository.findAll();
        assertThat(socialSecurityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSocialSecurities() throws Exception {
        // Initialize the database
        socialSecurityRepository.saveAndFlush(socialSecurity);

        // Get all the socialSecurityList
        restSocialSecurityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(socialSecurity.getId().intValue())))
            .andExpect(jsonPath("$.[*].eps").value(hasItem(DEFAULT_EPS)))
            .andExpect(jsonPath("$.[*].afp").value(hasItem(DEFAULT_AFP)));
    }

    @Test
    @Transactional
    void getSocialSecurity() throws Exception {
        // Initialize the database
        socialSecurityRepository.saveAndFlush(socialSecurity);

        // Get the socialSecurity
        restSocialSecurityMockMvc
            .perform(get(ENTITY_API_URL_ID, socialSecurity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(socialSecurity.getId().intValue()))
            .andExpect(jsonPath("$.eps").value(DEFAULT_EPS))
            .andExpect(jsonPath("$.afp").value(DEFAULT_AFP));
    }

    @Test
    @Transactional
    void getNonExistingSocialSecurity() throws Exception {
        // Get the socialSecurity
        restSocialSecurityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSocialSecurity() throws Exception {
        // Initialize the database
        socialSecurityRepository.saveAndFlush(socialSecurity);

        int databaseSizeBeforeUpdate = socialSecurityRepository.findAll().size();

        // Update the socialSecurity
        SocialSecurity updatedSocialSecurity = socialSecurityRepository.findById(socialSecurity.getId()).get();
        // Disconnect from session so that the updates on updatedSocialSecurity are not directly saved in db
        em.detach(updatedSocialSecurity);
        updatedSocialSecurity.eps(UPDATED_EPS).afp(UPDATED_AFP);

        restSocialSecurityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSocialSecurity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSocialSecurity))
            )
            .andExpect(status().isOk());

        // Validate the SocialSecurity in the database
        List<SocialSecurity> socialSecurityList = socialSecurityRepository.findAll();
        assertThat(socialSecurityList).hasSize(databaseSizeBeforeUpdate);
        SocialSecurity testSocialSecurity = socialSecurityList.get(socialSecurityList.size() - 1);
        assertThat(testSocialSecurity.getEps()).isEqualTo(UPDATED_EPS);
        assertThat(testSocialSecurity.getAfp()).isEqualTo(UPDATED_AFP);
    }

    @Test
    @Transactional
    void putNonExistingSocialSecurity() throws Exception {
        int databaseSizeBeforeUpdate = socialSecurityRepository.findAll().size();
        socialSecurity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocialSecurityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, socialSecurity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(socialSecurity))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialSecurity in the database
        List<SocialSecurity> socialSecurityList = socialSecurityRepository.findAll();
        assertThat(socialSecurityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSocialSecurity() throws Exception {
        int databaseSizeBeforeUpdate = socialSecurityRepository.findAll().size();
        socialSecurity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialSecurityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(socialSecurity))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialSecurity in the database
        List<SocialSecurity> socialSecurityList = socialSecurityRepository.findAll();
        assertThat(socialSecurityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSocialSecurity() throws Exception {
        int databaseSizeBeforeUpdate = socialSecurityRepository.findAll().size();
        socialSecurity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialSecurityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialSecurity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SocialSecurity in the database
        List<SocialSecurity> socialSecurityList = socialSecurityRepository.findAll();
        assertThat(socialSecurityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSocialSecurityWithPatch() throws Exception {
        // Initialize the database
        socialSecurityRepository.saveAndFlush(socialSecurity);

        int databaseSizeBeforeUpdate = socialSecurityRepository.findAll().size();

        // Update the socialSecurity using partial update
        SocialSecurity partialUpdatedSocialSecurity = new SocialSecurity();
        partialUpdatedSocialSecurity.setId(socialSecurity.getId());

        restSocialSecurityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSocialSecurity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSocialSecurity))
            )
            .andExpect(status().isOk());

        // Validate the SocialSecurity in the database
        List<SocialSecurity> socialSecurityList = socialSecurityRepository.findAll();
        assertThat(socialSecurityList).hasSize(databaseSizeBeforeUpdate);
        SocialSecurity testSocialSecurity = socialSecurityList.get(socialSecurityList.size() - 1);
        assertThat(testSocialSecurity.getEps()).isEqualTo(DEFAULT_EPS);
        assertThat(testSocialSecurity.getAfp()).isEqualTo(DEFAULT_AFP);
    }

    @Test
    @Transactional
    void fullUpdateSocialSecurityWithPatch() throws Exception {
        // Initialize the database
        socialSecurityRepository.saveAndFlush(socialSecurity);

        int databaseSizeBeforeUpdate = socialSecurityRepository.findAll().size();

        // Update the socialSecurity using partial update
        SocialSecurity partialUpdatedSocialSecurity = new SocialSecurity();
        partialUpdatedSocialSecurity.setId(socialSecurity.getId());

        partialUpdatedSocialSecurity.eps(UPDATED_EPS).afp(UPDATED_AFP);

        restSocialSecurityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSocialSecurity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSocialSecurity))
            )
            .andExpect(status().isOk());

        // Validate the SocialSecurity in the database
        List<SocialSecurity> socialSecurityList = socialSecurityRepository.findAll();
        assertThat(socialSecurityList).hasSize(databaseSizeBeforeUpdate);
        SocialSecurity testSocialSecurity = socialSecurityList.get(socialSecurityList.size() - 1);
        assertThat(testSocialSecurity.getEps()).isEqualTo(UPDATED_EPS);
        assertThat(testSocialSecurity.getAfp()).isEqualTo(UPDATED_AFP);
    }

    @Test
    @Transactional
    void patchNonExistingSocialSecurity() throws Exception {
        int databaseSizeBeforeUpdate = socialSecurityRepository.findAll().size();
        socialSecurity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocialSecurityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, socialSecurity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(socialSecurity))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialSecurity in the database
        List<SocialSecurity> socialSecurityList = socialSecurityRepository.findAll();
        assertThat(socialSecurityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSocialSecurity() throws Exception {
        int databaseSizeBeforeUpdate = socialSecurityRepository.findAll().size();
        socialSecurity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialSecurityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(socialSecurity))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialSecurity in the database
        List<SocialSecurity> socialSecurityList = socialSecurityRepository.findAll();
        assertThat(socialSecurityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSocialSecurity() throws Exception {
        int databaseSizeBeforeUpdate = socialSecurityRepository.findAll().size();
        socialSecurity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialSecurityMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(socialSecurity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SocialSecurity in the database
        List<SocialSecurity> socialSecurityList = socialSecurityRepository.findAll();
        assertThat(socialSecurityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSocialSecurity() throws Exception {
        // Initialize the database
        socialSecurityRepository.saveAndFlush(socialSecurity);

        int databaseSizeBeforeDelete = socialSecurityRepository.findAll().size();

        // Delete the socialSecurity
        restSocialSecurityMockMvc
            .perform(delete(ENTITY_API_URL_ID, socialSecurity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SocialSecurity> socialSecurityList = socialSecurityRepository.findAll();
        assertThat(socialSecurityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
