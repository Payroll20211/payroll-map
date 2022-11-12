package co.edu.sena.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.sena.IntegrationTest;
import co.edu.sena.domain.OperatorType;
import co.edu.sena.repository.OperatorTypeRepository;
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
 * Integration tests for the {@link OperatorTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OperatorTypeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/operator-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OperatorTypeRepository operatorTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOperatorTypeMockMvc;

    private OperatorType operatorType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OperatorType createEntity(EntityManager em) {
        OperatorType operatorType = new OperatorType().code(DEFAULT_CODE).description(DEFAULT_DESCRIPTION);
        return operatorType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OperatorType createUpdatedEntity(EntityManager em) {
        OperatorType operatorType = new OperatorType().code(UPDATED_CODE).description(UPDATED_DESCRIPTION);
        return operatorType;
    }

    @BeforeEach
    public void initTest() {
        operatorType = createEntity(em);
    }

    @Test
    @Transactional
    void createOperatorType() throws Exception {
        int databaseSizeBeforeCreate = operatorTypeRepository.findAll().size();
        // Create the OperatorType
        restOperatorTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatorType)))
            .andExpect(status().isCreated());

        // Validate the OperatorType in the database
        List<OperatorType> operatorTypeList = operatorTypeRepository.findAll();
        assertThat(operatorTypeList).hasSize(databaseSizeBeforeCreate + 1);
        OperatorType testOperatorType = operatorTypeList.get(operatorTypeList.size() - 1);
        assertThat(testOperatorType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOperatorType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createOperatorTypeWithExistingId() throws Exception {
        // Create the OperatorType with an existing ID
        operatorType.setId(1L);

        int databaseSizeBeforeCreate = operatorTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperatorTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatorType)))
            .andExpect(status().isBadRequest());

        // Validate the OperatorType in the database
        List<OperatorType> operatorTypeList = operatorTypeRepository.findAll();
        assertThat(operatorTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = operatorTypeRepository.findAll().size();
        // set the field null
        operatorType.setCode(null);

        // Create the OperatorType, which fails.

        restOperatorTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatorType)))
            .andExpect(status().isBadRequest());

        List<OperatorType> operatorTypeList = operatorTypeRepository.findAll();
        assertThat(operatorTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = operatorTypeRepository.findAll().size();
        // set the field null
        operatorType.setDescription(null);

        // Create the OperatorType, which fails.

        restOperatorTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatorType)))
            .andExpect(status().isBadRequest());

        List<OperatorType> operatorTypeList = operatorTypeRepository.findAll();
        assertThat(operatorTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOperatorTypes() throws Exception {
        // Initialize the database
        operatorTypeRepository.saveAndFlush(operatorType);

        // Get all the operatorTypeList
        restOperatorTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operatorType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getOperatorType() throws Exception {
        // Initialize the database
        operatorTypeRepository.saveAndFlush(operatorType);

        // Get the operatorType
        restOperatorTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, operatorType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(operatorType.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingOperatorType() throws Exception {
        // Get the operatorType
        restOperatorTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOperatorType() throws Exception {
        // Initialize the database
        operatorTypeRepository.saveAndFlush(operatorType);

        int databaseSizeBeforeUpdate = operatorTypeRepository.findAll().size();

        // Update the operatorType
        OperatorType updatedOperatorType = operatorTypeRepository.findById(operatorType.getId()).get();
        // Disconnect from session so that the updates on updatedOperatorType are not directly saved in db
        em.detach(updatedOperatorType);
        updatedOperatorType.code(UPDATED_CODE).description(UPDATED_DESCRIPTION);

        restOperatorTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOperatorType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOperatorType))
            )
            .andExpect(status().isOk());

        // Validate the OperatorType in the database
        List<OperatorType> operatorTypeList = operatorTypeRepository.findAll();
        assertThat(operatorTypeList).hasSize(databaseSizeBeforeUpdate);
        OperatorType testOperatorType = operatorTypeList.get(operatorTypeList.size() - 1);
        assertThat(testOperatorType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOperatorType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingOperatorType() throws Exception {
        int databaseSizeBeforeUpdate = operatorTypeRepository.findAll().size();
        operatorType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperatorTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operatorType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operatorType))
            )
            .andExpect(status().isBadRequest());

        // Validate the OperatorType in the database
        List<OperatorType> operatorTypeList = operatorTypeRepository.findAll();
        assertThat(operatorTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOperatorType() throws Exception {
        int databaseSizeBeforeUpdate = operatorTypeRepository.findAll().size();
        operatorType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatorTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operatorType))
            )
            .andExpect(status().isBadRequest());

        // Validate the OperatorType in the database
        List<OperatorType> operatorTypeList = operatorTypeRepository.findAll();
        assertThat(operatorTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOperatorType() throws Exception {
        int databaseSizeBeforeUpdate = operatorTypeRepository.findAll().size();
        operatorType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatorTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatorType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OperatorType in the database
        List<OperatorType> operatorTypeList = operatorTypeRepository.findAll();
        assertThat(operatorTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOperatorTypeWithPatch() throws Exception {
        // Initialize the database
        operatorTypeRepository.saveAndFlush(operatorType);

        int databaseSizeBeforeUpdate = operatorTypeRepository.findAll().size();

        // Update the operatorType using partial update
        OperatorType partialUpdatedOperatorType = new OperatorType();
        partialUpdatedOperatorType.setId(operatorType.getId());

        partialUpdatedOperatorType.description(UPDATED_DESCRIPTION);

        restOperatorTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperatorType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperatorType))
            )
            .andExpect(status().isOk());

        // Validate the OperatorType in the database
        List<OperatorType> operatorTypeList = operatorTypeRepository.findAll();
        assertThat(operatorTypeList).hasSize(databaseSizeBeforeUpdate);
        OperatorType testOperatorType = operatorTypeList.get(operatorTypeList.size() - 1);
        assertThat(testOperatorType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOperatorType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateOperatorTypeWithPatch() throws Exception {
        // Initialize the database
        operatorTypeRepository.saveAndFlush(operatorType);

        int databaseSizeBeforeUpdate = operatorTypeRepository.findAll().size();

        // Update the operatorType using partial update
        OperatorType partialUpdatedOperatorType = new OperatorType();
        partialUpdatedOperatorType.setId(operatorType.getId());

        partialUpdatedOperatorType.code(UPDATED_CODE).description(UPDATED_DESCRIPTION);

        restOperatorTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperatorType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperatorType))
            )
            .andExpect(status().isOk());

        // Validate the OperatorType in the database
        List<OperatorType> operatorTypeList = operatorTypeRepository.findAll();
        assertThat(operatorTypeList).hasSize(databaseSizeBeforeUpdate);
        OperatorType testOperatorType = operatorTypeList.get(operatorTypeList.size() - 1);
        assertThat(testOperatorType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOperatorType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingOperatorType() throws Exception {
        int databaseSizeBeforeUpdate = operatorTypeRepository.findAll().size();
        operatorType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperatorTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, operatorType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operatorType))
            )
            .andExpect(status().isBadRequest());

        // Validate the OperatorType in the database
        List<OperatorType> operatorTypeList = operatorTypeRepository.findAll();
        assertThat(operatorTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOperatorType() throws Exception {
        int databaseSizeBeforeUpdate = operatorTypeRepository.findAll().size();
        operatorType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatorTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operatorType))
            )
            .andExpect(status().isBadRequest());

        // Validate the OperatorType in the database
        List<OperatorType> operatorTypeList = operatorTypeRepository.findAll();
        assertThat(operatorTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOperatorType() throws Exception {
        int databaseSizeBeforeUpdate = operatorTypeRepository.findAll().size();
        operatorType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatorTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(operatorType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OperatorType in the database
        List<OperatorType> operatorTypeList = operatorTypeRepository.findAll();
        assertThat(operatorTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOperatorType() throws Exception {
        // Initialize the database
        operatorTypeRepository.saveAndFlush(operatorType);

        int databaseSizeBeforeDelete = operatorTypeRepository.findAll().size();

        // Delete the operatorType
        restOperatorTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, operatorType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OperatorType> operatorTypeList = operatorTypeRepository.findAll();
        assertThat(operatorTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
