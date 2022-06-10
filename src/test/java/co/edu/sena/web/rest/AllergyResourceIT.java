package co.edu.sena.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.sena.IntegrationTest;
import co.edu.sena.domain.Allergy;
import co.edu.sena.repository.AllergyRepository;
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
 * Integration tests for the {@link AllergyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AllergyResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TREATMENT = "AAAAAAAAAA";
    private static final String UPDATED_TREATMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/allergies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AllergyRepository allergyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAllergyMockMvc;

    private Allergy allergy;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Allergy createEntity(EntityManager em) {
        Allergy allergy = new Allergy().description(DEFAULT_DESCRIPTION).treatment(DEFAULT_TREATMENT);
        return allergy;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Allergy createUpdatedEntity(EntityManager em) {
        Allergy allergy = new Allergy().description(UPDATED_DESCRIPTION).treatment(UPDATED_TREATMENT);
        return allergy;
    }

    @BeforeEach
    public void initTest() {
        allergy = createEntity(em);
    }

    @Test
    @Transactional
    void createAllergy() throws Exception {
        int databaseSizeBeforeCreate = allergyRepository.findAll().size();
        // Create the Allergy
        restAllergyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(allergy)))
            .andExpect(status().isCreated());

        // Validate the Allergy in the database
        List<Allergy> allergyList = allergyRepository.findAll();
        assertThat(allergyList).hasSize(databaseSizeBeforeCreate + 1);
        Allergy testAllergy = allergyList.get(allergyList.size() - 1);
        assertThat(testAllergy.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAllergy.getTreatment()).isEqualTo(DEFAULT_TREATMENT);
    }

    @Test
    @Transactional
    void createAllergyWithExistingId() throws Exception {
        // Create the Allergy with an existing ID
        allergy.setId(1L);

        int databaseSizeBeforeCreate = allergyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAllergyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(allergy)))
            .andExpect(status().isBadRequest());

        // Validate the Allergy in the database
        List<Allergy> allergyList = allergyRepository.findAll();
        assertThat(allergyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = allergyRepository.findAll().size();
        // set the field null
        allergy.setDescription(null);

        // Create the Allergy, which fails.

        restAllergyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(allergy)))
            .andExpect(status().isBadRequest());

        List<Allergy> allergyList = allergyRepository.findAll();
        assertThat(allergyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTreatmentIsRequired() throws Exception {
        int databaseSizeBeforeTest = allergyRepository.findAll().size();
        // set the field null
        allergy.setTreatment(null);

        // Create the Allergy, which fails.

        restAllergyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(allergy)))
            .andExpect(status().isBadRequest());

        List<Allergy> allergyList = allergyRepository.findAll();
        assertThat(allergyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAllergies() throws Exception {
        // Initialize the database
        allergyRepository.saveAndFlush(allergy);

        // Get all the allergyList
        restAllergyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(allergy.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].treatment").value(hasItem(DEFAULT_TREATMENT)));
    }

    @Test
    @Transactional
    void getAllergy() throws Exception {
        // Initialize the database
        allergyRepository.saveAndFlush(allergy);

        // Get the allergy
        restAllergyMockMvc
            .perform(get(ENTITY_API_URL_ID, allergy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(allergy.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.treatment").value(DEFAULT_TREATMENT));
    }

    @Test
    @Transactional
    void getNonExistingAllergy() throws Exception {
        // Get the allergy
        restAllergyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAllergy() throws Exception {
        // Initialize the database
        allergyRepository.saveAndFlush(allergy);

        int databaseSizeBeforeUpdate = allergyRepository.findAll().size();

        // Update the allergy
        Allergy updatedAllergy = allergyRepository.findById(allergy.getId()).get();
        // Disconnect from session so that the updates on updatedAllergy are not directly saved in db
        em.detach(updatedAllergy);
        updatedAllergy.description(UPDATED_DESCRIPTION).treatment(UPDATED_TREATMENT);

        restAllergyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAllergy.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAllergy))
            )
            .andExpect(status().isOk());

        // Validate the Allergy in the database
        List<Allergy> allergyList = allergyRepository.findAll();
        assertThat(allergyList).hasSize(databaseSizeBeforeUpdate);
        Allergy testAllergy = allergyList.get(allergyList.size() - 1);
        assertThat(testAllergy.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAllergy.getTreatment()).isEqualTo(UPDATED_TREATMENT);
    }

    @Test
    @Transactional
    void putNonExistingAllergy() throws Exception {
        int databaseSizeBeforeUpdate = allergyRepository.findAll().size();
        allergy.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAllergyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, allergy.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(allergy))
            )
            .andExpect(status().isBadRequest());

        // Validate the Allergy in the database
        List<Allergy> allergyList = allergyRepository.findAll();
        assertThat(allergyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAllergy() throws Exception {
        int databaseSizeBeforeUpdate = allergyRepository.findAll().size();
        allergy.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllergyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(allergy))
            )
            .andExpect(status().isBadRequest());

        // Validate the Allergy in the database
        List<Allergy> allergyList = allergyRepository.findAll();
        assertThat(allergyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAllergy() throws Exception {
        int databaseSizeBeforeUpdate = allergyRepository.findAll().size();
        allergy.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllergyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(allergy)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Allergy in the database
        List<Allergy> allergyList = allergyRepository.findAll();
        assertThat(allergyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAllergyWithPatch() throws Exception {
        // Initialize the database
        allergyRepository.saveAndFlush(allergy);

        int databaseSizeBeforeUpdate = allergyRepository.findAll().size();

        // Update the allergy using partial update
        Allergy partialUpdatedAllergy = new Allergy();
        partialUpdatedAllergy.setId(allergy.getId());

        partialUpdatedAllergy.treatment(UPDATED_TREATMENT);

        restAllergyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAllergy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAllergy))
            )
            .andExpect(status().isOk());

        // Validate the Allergy in the database
        List<Allergy> allergyList = allergyRepository.findAll();
        assertThat(allergyList).hasSize(databaseSizeBeforeUpdate);
        Allergy testAllergy = allergyList.get(allergyList.size() - 1);
        assertThat(testAllergy.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAllergy.getTreatment()).isEqualTo(UPDATED_TREATMENT);
    }

    @Test
    @Transactional
    void fullUpdateAllergyWithPatch() throws Exception {
        // Initialize the database
        allergyRepository.saveAndFlush(allergy);

        int databaseSizeBeforeUpdate = allergyRepository.findAll().size();

        // Update the allergy using partial update
        Allergy partialUpdatedAllergy = new Allergy();
        partialUpdatedAllergy.setId(allergy.getId());

        partialUpdatedAllergy.description(UPDATED_DESCRIPTION).treatment(UPDATED_TREATMENT);

        restAllergyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAllergy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAllergy))
            )
            .andExpect(status().isOk());

        // Validate the Allergy in the database
        List<Allergy> allergyList = allergyRepository.findAll();
        assertThat(allergyList).hasSize(databaseSizeBeforeUpdate);
        Allergy testAllergy = allergyList.get(allergyList.size() - 1);
        assertThat(testAllergy.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAllergy.getTreatment()).isEqualTo(UPDATED_TREATMENT);
    }

    @Test
    @Transactional
    void patchNonExistingAllergy() throws Exception {
        int databaseSizeBeforeUpdate = allergyRepository.findAll().size();
        allergy.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAllergyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, allergy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(allergy))
            )
            .andExpect(status().isBadRequest());

        // Validate the Allergy in the database
        List<Allergy> allergyList = allergyRepository.findAll();
        assertThat(allergyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAllergy() throws Exception {
        int databaseSizeBeforeUpdate = allergyRepository.findAll().size();
        allergy.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllergyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(allergy))
            )
            .andExpect(status().isBadRequest());

        // Validate the Allergy in the database
        List<Allergy> allergyList = allergyRepository.findAll();
        assertThat(allergyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAllergy() throws Exception {
        int databaseSizeBeforeUpdate = allergyRepository.findAll().size();
        allergy.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAllergyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(allergy)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Allergy in the database
        List<Allergy> allergyList = allergyRepository.findAll();
        assertThat(allergyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAllergy() throws Exception {
        // Initialize the database
        allergyRepository.saveAndFlush(allergy);

        int databaseSizeBeforeDelete = allergyRepository.findAll().size();

        // Delete the allergy
        restAllergyMockMvc
            .perform(delete(ENTITY_API_URL_ID, allergy.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Allergy> allergyList = allergyRepository.findAll();
        assertThat(allergyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
