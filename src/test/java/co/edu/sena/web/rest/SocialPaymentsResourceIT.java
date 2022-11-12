package co.edu.sena.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.sena.IntegrationTest;
import co.edu.sena.domain.SocialPayments;
import co.edu.sena.repository.SocialPaymentsRepository;
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
 * Integration tests for the {@link SocialPaymentsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SocialPaymentsResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/social-payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SocialPaymentsRepository socialPaymentsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSocialPaymentsMockMvc;

    private SocialPayments socialPayments;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SocialPayments createEntity(EntityManager em) {
        SocialPayments socialPayments = new SocialPayments().code(DEFAULT_CODE).description(DEFAULT_DESCRIPTION);
        return socialPayments;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SocialPayments createUpdatedEntity(EntityManager em) {
        SocialPayments socialPayments = new SocialPayments().code(UPDATED_CODE).description(UPDATED_DESCRIPTION);
        return socialPayments;
    }

    @BeforeEach
    public void initTest() {
        socialPayments = createEntity(em);
    }

    @Test
    @Transactional
    void createSocialPayments() throws Exception {
        int databaseSizeBeforeCreate = socialPaymentsRepository.findAll().size();
        // Create the SocialPayments
        restSocialPaymentsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialPayments))
            )
            .andExpect(status().isCreated());

        // Validate the SocialPayments in the database
        List<SocialPayments> socialPaymentsList = socialPaymentsRepository.findAll();
        assertThat(socialPaymentsList).hasSize(databaseSizeBeforeCreate + 1);
        SocialPayments testSocialPayments = socialPaymentsList.get(socialPaymentsList.size() - 1);
        assertThat(testSocialPayments.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSocialPayments.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createSocialPaymentsWithExistingId() throws Exception {
        // Create the SocialPayments with an existing ID
        socialPayments.setId(1L);

        int databaseSizeBeforeCreate = socialPaymentsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSocialPaymentsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialPayments))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialPayments in the database
        List<SocialPayments> socialPaymentsList = socialPaymentsRepository.findAll();
        assertThat(socialPaymentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialPaymentsRepository.findAll().size();
        // set the field null
        socialPayments.setCode(null);

        // Create the SocialPayments, which fails.

        restSocialPaymentsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialPayments))
            )
            .andExpect(status().isBadRequest());

        List<SocialPayments> socialPaymentsList = socialPaymentsRepository.findAll();
        assertThat(socialPaymentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = socialPaymentsRepository.findAll().size();
        // set the field null
        socialPayments.setDescription(null);

        // Create the SocialPayments, which fails.

        restSocialPaymentsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialPayments))
            )
            .andExpect(status().isBadRequest());

        List<SocialPayments> socialPaymentsList = socialPaymentsRepository.findAll();
        assertThat(socialPaymentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSocialPayments() throws Exception {
        // Initialize the database
        socialPaymentsRepository.saveAndFlush(socialPayments);

        // Get all the socialPaymentsList
        restSocialPaymentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(socialPayments.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getSocialPayments() throws Exception {
        // Initialize the database
        socialPaymentsRepository.saveAndFlush(socialPayments);

        // Get the socialPayments
        restSocialPaymentsMockMvc
            .perform(get(ENTITY_API_URL_ID, socialPayments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(socialPayments.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingSocialPayments() throws Exception {
        // Get the socialPayments
        restSocialPaymentsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSocialPayments() throws Exception {
        // Initialize the database
        socialPaymentsRepository.saveAndFlush(socialPayments);

        int databaseSizeBeforeUpdate = socialPaymentsRepository.findAll().size();

        // Update the socialPayments
        SocialPayments updatedSocialPayments = socialPaymentsRepository.findById(socialPayments.getId()).get();
        // Disconnect from session so that the updates on updatedSocialPayments are not directly saved in db
        em.detach(updatedSocialPayments);
        updatedSocialPayments.code(UPDATED_CODE).description(UPDATED_DESCRIPTION);

        restSocialPaymentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSocialPayments.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSocialPayments))
            )
            .andExpect(status().isOk());

        // Validate the SocialPayments in the database
        List<SocialPayments> socialPaymentsList = socialPaymentsRepository.findAll();
        assertThat(socialPaymentsList).hasSize(databaseSizeBeforeUpdate);
        SocialPayments testSocialPayments = socialPaymentsList.get(socialPaymentsList.size() - 1);
        assertThat(testSocialPayments.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSocialPayments.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingSocialPayments() throws Exception {
        int databaseSizeBeforeUpdate = socialPaymentsRepository.findAll().size();
        socialPayments.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocialPaymentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, socialPayments.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(socialPayments))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialPayments in the database
        List<SocialPayments> socialPaymentsList = socialPaymentsRepository.findAll();
        assertThat(socialPaymentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSocialPayments() throws Exception {
        int databaseSizeBeforeUpdate = socialPaymentsRepository.findAll().size();
        socialPayments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialPaymentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(socialPayments))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialPayments in the database
        List<SocialPayments> socialPaymentsList = socialPaymentsRepository.findAll();
        assertThat(socialPaymentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSocialPayments() throws Exception {
        int databaseSizeBeforeUpdate = socialPaymentsRepository.findAll().size();
        socialPayments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialPaymentsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(socialPayments)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SocialPayments in the database
        List<SocialPayments> socialPaymentsList = socialPaymentsRepository.findAll();
        assertThat(socialPaymentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSocialPaymentsWithPatch() throws Exception {
        // Initialize the database
        socialPaymentsRepository.saveAndFlush(socialPayments);

        int databaseSizeBeforeUpdate = socialPaymentsRepository.findAll().size();

        // Update the socialPayments using partial update
        SocialPayments partialUpdatedSocialPayments = new SocialPayments();
        partialUpdatedSocialPayments.setId(socialPayments.getId());

        partialUpdatedSocialPayments.code(UPDATED_CODE).description(UPDATED_DESCRIPTION);

        restSocialPaymentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSocialPayments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSocialPayments))
            )
            .andExpect(status().isOk());

        // Validate the SocialPayments in the database
        List<SocialPayments> socialPaymentsList = socialPaymentsRepository.findAll();
        assertThat(socialPaymentsList).hasSize(databaseSizeBeforeUpdate);
        SocialPayments testSocialPayments = socialPaymentsList.get(socialPaymentsList.size() - 1);
        assertThat(testSocialPayments.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSocialPayments.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateSocialPaymentsWithPatch() throws Exception {
        // Initialize the database
        socialPaymentsRepository.saveAndFlush(socialPayments);

        int databaseSizeBeforeUpdate = socialPaymentsRepository.findAll().size();

        // Update the socialPayments using partial update
        SocialPayments partialUpdatedSocialPayments = new SocialPayments();
        partialUpdatedSocialPayments.setId(socialPayments.getId());

        partialUpdatedSocialPayments.code(UPDATED_CODE).description(UPDATED_DESCRIPTION);

        restSocialPaymentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSocialPayments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSocialPayments))
            )
            .andExpect(status().isOk());

        // Validate the SocialPayments in the database
        List<SocialPayments> socialPaymentsList = socialPaymentsRepository.findAll();
        assertThat(socialPaymentsList).hasSize(databaseSizeBeforeUpdate);
        SocialPayments testSocialPayments = socialPaymentsList.get(socialPaymentsList.size() - 1);
        assertThat(testSocialPayments.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSocialPayments.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingSocialPayments() throws Exception {
        int databaseSizeBeforeUpdate = socialPaymentsRepository.findAll().size();
        socialPayments.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSocialPaymentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, socialPayments.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(socialPayments))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialPayments in the database
        List<SocialPayments> socialPaymentsList = socialPaymentsRepository.findAll();
        assertThat(socialPaymentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSocialPayments() throws Exception {
        int databaseSizeBeforeUpdate = socialPaymentsRepository.findAll().size();
        socialPayments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialPaymentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(socialPayments))
            )
            .andExpect(status().isBadRequest());

        // Validate the SocialPayments in the database
        List<SocialPayments> socialPaymentsList = socialPaymentsRepository.findAll();
        assertThat(socialPaymentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSocialPayments() throws Exception {
        int databaseSizeBeforeUpdate = socialPaymentsRepository.findAll().size();
        socialPayments.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSocialPaymentsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(socialPayments))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SocialPayments in the database
        List<SocialPayments> socialPaymentsList = socialPaymentsRepository.findAll();
        assertThat(socialPaymentsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSocialPayments() throws Exception {
        // Initialize the database
        socialPaymentsRepository.saveAndFlush(socialPayments);

        int databaseSizeBeforeDelete = socialPaymentsRepository.findAll().size();

        // Delete the socialPayments
        restSocialPaymentsMockMvc
            .perform(delete(ENTITY_API_URL_ID, socialPayments.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SocialPayments> socialPaymentsList = socialPaymentsRepository.findAll();
        assertThat(socialPaymentsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
