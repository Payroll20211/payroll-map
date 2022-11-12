package co.edu.sena.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.sena.IntegrationTest;
import co.edu.sena.domain.CostCenter;
import co.edu.sena.repository.CostCenterRepository;
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
 * Integration tests for the {@link CostCenterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CostCenterResourceIT {

    private static final String DEFAULT_COST_CENTER_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COST_CENTER_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COST_CENTER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COST_CENTER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COST_CENTER_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_COST_CENTER_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cost-centers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CostCenterRepository costCenterRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCostCenterMockMvc;

    private CostCenter costCenter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CostCenter createEntity(EntityManager em) {
        CostCenter costCenter = new CostCenter()
            .costCenterCode(DEFAULT_COST_CENTER_CODE)
            .costCenterName(DEFAULT_COST_CENTER_NAME)
            .costCenterType(DEFAULT_COST_CENTER_TYPE);
        return costCenter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CostCenter createUpdatedEntity(EntityManager em) {
        CostCenter costCenter = new CostCenter()
            .costCenterCode(UPDATED_COST_CENTER_CODE)
            .costCenterName(UPDATED_COST_CENTER_NAME)
            .costCenterType(UPDATED_COST_CENTER_TYPE);
        return costCenter;
    }

    @BeforeEach
    public void initTest() {
        costCenter = createEntity(em);
    }

    @Test
    @Transactional
    void createCostCenter() throws Exception {
        int databaseSizeBeforeCreate = costCenterRepository.findAll().size();
        // Create the CostCenter
        restCostCenterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(costCenter)))
            .andExpect(status().isCreated());

        // Validate the CostCenter in the database
        List<CostCenter> costCenterList = costCenterRepository.findAll();
        assertThat(costCenterList).hasSize(databaseSizeBeforeCreate + 1);
        CostCenter testCostCenter = costCenterList.get(costCenterList.size() - 1);
        assertThat(testCostCenter.getCostCenterCode()).isEqualTo(DEFAULT_COST_CENTER_CODE);
        assertThat(testCostCenter.getCostCenterName()).isEqualTo(DEFAULT_COST_CENTER_NAME);
        assertThat(testCostCenter.getCostCenterType()).isEqualTo(DEFAULT_COST_CENTER_TYPE);
    }

    @Test
    @Transactional
    void createCostCenterWithExistingId() throws Exception {
        // Create the CostCenter with an existing ID
        costCenter.setId(1L);

        int databaseSizeBeforeCreate = costCenterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCostCenterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(costCenter)))
            .andExpect(status().isBadRequest());

        // Validate the CostCenter in the database
        List<CostCenter> costCenterList = costCenterRepository.findAll();
        assertThat(costCenterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCostCenterCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = costCenterRepository.findAll().size();
        // set the field null
        costCenter.setCostCenterCode(null);

        // Create the CostCenter, which fails.

        restCostCenterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(costCenter)))
            .andExpect(status().isBadRequest());

        List<CostCenter> costCenterList = costCenterRepository.findAll();
        assertThat(costCenterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCostCenterNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = costCenterRepository.findAll().size();
        // set the field null
        costCenter.setCostCenterName(null);

        // Create the CostCenter, which fails.

        restCostCenterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(costCenter)))
            .andExpect(status().isBadRequest());

        List<CostCenter> costCenterList = costCenterRepository.findAll();
        assertThat(costCenterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCostCenterTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = costCenterRepository.findAll().size();
        // set the field null
        costCenter.setCostCenterType(null);

        // Create the CostCenter, which fails.

        restCostCenterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(costCenter)))
            .andExpect(status().isBadRequest());

        List<CostCenter> costCenterList = costCenterRepository.findAll();
        assertThat(costCenterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCostCenters() throws Exception {
        // Initialize the database
        costCenterRepository.saveAndFlush(costCenter);

        // Get all the costCenterList
        restCostCenterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(costCenter.getId().intValue())))
            .andExpect(jsonPath("$.[*].costCenterCode").value(hasItem(DEFAULT_COST_CENTER_CODE)))
            .andExpect(jsonPath("$.[*].costCenterName").value(hasItem(DEFAULT_COST_CENTER_NAME)))
            .andExpect(jsonPath("$.[*].costCenterType").value(hasItem(DEFAULT_COST_CENTER_TYPE)));
    }

    @Test
    @Transactional
    void getCostCenter() throws Exception {
        // Initialize the database
        costCenterRepository.saveAndFlush(costCenter);

        // Get the costCenter
        restCostCenterMockMvc
            .perform(get(ENTITY_API_URL_ID, costCenter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(costCenter.getId().intValue()))
            .andExpect(jsonPath("$.costCenterCode").value(DEFAULT_COST_CENTER_CODE))
            .andExpect(jsonPath("$.costCenterName").value(DEFAULT_COST_CENTER_NAME))
            .andExpect(jsonPath("$.costCenterType").value(DEFAULT_COST_CENTER_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingCostCenter() throws Exception {
        // Get the costCenter
        restCostCenterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCostCenter() throws Exception {
        // Initialize the database
        costCenterRepository.saveAndFlush(costCenter);

        int databaseSizeBeforeUpdate = costCenterRepository.findAll().size();

        // Update the costCenter
        CostCenter updatedCostCenter = costCenterRepository.findById(costCenter.getId()).get();
        // Disconnect from session so that the updates on updatedCostCenter are not directly saved in db
        em.detach(updatedCostCenter);
        updatedCostCenter
            .costCenterCode(UPDATED_COST_CENTER_CODE)
            .costCenterName(UPDATED_COST_CENTER_NAME)
            .costCenterType(UPDATED_COST_CENTER_TYPE);

        restCostCenterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCostCenter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCostCenter))
            )
            .andExpect(status().isOk());

        // Validate the CostCenter in the database
        List<CostCenter> costCenterList = costCenterRepository.findAll();
        assertThat(costCenterList).hasSize(databaseSizeBeforeUpdate);
        CostCenter testCostCenter = costCenterList.get(costCenterList.size() - 1);
        assertThat(testCostCenter.getCostCenterCode()).isEqualTo(UPDATED_COST_CENTER_CODE);
        assertThat(testCostCenter.getCostCenterName()).isEqualTo(UPDATED_COST_CENTER_NAME);
        assertThat(testCostCenter.getCostCenterType()).isEqualTo(UPDATED_COST_CENTER_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingCostCenter() throws Exception {
        int databaseSizeBeforeUpdate = costCenterRepository.findAll().size();
        costCenter.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCostCenterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, costCenter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(costCenter))
            )
            .andExpect(status().isBadRequest());

        // Validate the CostCenter in the database
        List<CostCenter> costCenterList = costCenterRepository.findAll();
        assertThat(costCenterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCostCenter() throws Exception {
        int databaseSizeBeforeUpdate = costCenterRepository.findAll().size();
        costCenter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCostCenterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(costCenter))
            )
            .andExpect(status().isBadRequest());

        // Validate the CostCenter in the database
        List<CostCenter> costCenterList = costCenterRepository.findAll();
        assertThat(costCenterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCostCenter() throws Exception {
        int databaseSizeBeforeUpdate = costCenterRepository.findAll().size();
        costCenter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCostCenterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(costCenter)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CostCenter in the database
        List<CostCenter> costCenterList = costCenterRepository.findAll();
        assertThat(costCenterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCostCenterWithPatch() throws Exception {
        // Initialize the database
        costCenterRepository.saveAndFlush(costCenter);

        int databaseSizeBeforeUpdate = costCenterRepository.findAll().size();

        // Update the costCenter using partial update
        CostCenter partialUpdatedCostCenter = new CostCenter();
        partialUpdatedCostCenter.setId(costCenter.getId());

        partialUpdatedCostCenter.costCenterType(UPDATED_COST_CENTER_TYPE);

        restCostCenterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCostCenter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCostCenter))
            )
            .andExpect(status().isOk());

        // Validate the CostCenter in the database
        List<CostCenter> costCenterList = costCenterRepository.findAll();
        assertThat(costCenterList).hasSize(databaseSizeBeforeUpdate);
        CostCenter testCostCenter = costCenterList.get(costCenterList.size() - 1);
        assertThat(testCostCenter.getCostCenterCode()).isEqualTo(DEFAULT_COST_CENTER_CODE);
        assertThat(testCostCenter.getCostCenterName()).isEqualTo(DEFAULT_COST_CENTER_NAME);
        assertThat(testCostCenter.getCostCenterType()).isEqualTo(UPDATED_COST_CENTER_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateCostCenterWithPatch() throws Exception {
        // Initialize the database
        costCenterRepository.saveAndFlush(costCenter);

        int databaseSizeBeforeUpdate = costCenterRepository.findAll().size();

        // Update the costCenter using partial update
        CostCenter partialUpdatedCostCenter = new CostCenter();
        partialUpdatedCostCenter.setId(costCenter.getId());

        partialUpdatedCostCenter
            .costCenterCode(UPDATED_COST_CENTER_CODE)
            .costCenterName(UPDATED_COST_CENTER_NAME)
            .costCenterType(UPDATED_COST_CENTER_TYPE);

        restCostCenterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCostCenter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCostCenter))
            )
            .andExpect(status().isOk());

        // Validate the CostCenter in the database
        List<CostCenter> costCenterList = costCenterRepository.findAll();
        assertThat(costCenterList).hasSize(databaseSizeBeforeUpdate);
        CostCenter testCostCenter = costCenterList.get(costCenterList.size() - 1);
        assertThat(testCostCenter.getCostCenterCode()).isEqualTo(UPDATED_COST_CENTER_CODE);
        assertThat(testCostCenter.getCostCenterName()).isEqualTo(UPDATED_COST_CENTER_NAME);
        assertThat(testCostCenter.getCostCenterType()).isEqualTo(UPDATED_COST_CENTER_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingCostCenter() throws Exception {
        int databaseSizeBeforeUpdate = costCenterRepository.findAll().size();
        costCenter.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCostCenterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, costCenter.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(costCenter))
            )
            .andExpect(status().isBadRequest());

        // Validate the CostCenter in the database
        List<CostCenter> costCenterList = costCenterRepository.findAll();
        assertThat(costCenterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCostCenter() throws Exception {
        int databaseSizeBeforeUpdate = costCenterRepository.findAll().size();
        costCenter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCostCenterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(costCenter))
            )
            .andExpect(status().isBadRequest());

        // Validate the CostCenter in the database
        List<CostCenter> costCenterList = costCenterRepository.findAll();
        assertThat(costCenterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCostCenter() throws Exception {
        int databaseSizeBeforeUpdate = costCenterRepository.findAll().size();
        costCenter.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCostCenterMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(costCenter))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CostCenter in the database
        List<CostCenter> costCenterList = costCenterRepository.findAll();
        assertThat(costCenterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCostCenter() throws Exception {
        // Initialize the database
        costCenterRepository.saveAndFlush(costCenter);

        int databaseSizeBeforeDelete = costCenterRepository.findAll().size();

        // Delete the costCenter
        restCostCenterMockMvc
            .perform(delete(ENTITY_API_URL_ID, costCenter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CostCenter> costCenterList = costCenterRepository.findAll();
        assertThat(costCenterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
