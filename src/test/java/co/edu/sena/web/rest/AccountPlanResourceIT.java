package co.edu.sena.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.sena.IntegrationTest;
import co.edu.sena.domain.AccountPlan;
import co.edu.sena.repository.AccountPlanRepository;
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
 * Integration tests for the {@link AccountPlanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccountPlanResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/account-plans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AccountPlanRepository accountPlanRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccountPlanMockMvc;

    private AccountPlan accountPlan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountPlan createEntity(EntityManager em) {
        AccountPlan accountPlan = new AccountPlan().code(DEFAULT_CODE).description(DEFAULT_DESCRIPTION);
        return accountPlan;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountPlan createUpdatedEntity(EntityManager em) {
        AccountPlan accountPlan = new AccountPlan().code(UPDATED_CODE).description(UPDATED_DESCRIPTION);
        return accountPlan;
    }

    @BeforeEach
    public void initTest() {
        accountPlan = createEntity(em);
    }

    @Test
    @Transactional
    void createAccountPlan() throws Exception {
        int databaseSizeBeforeCreate = accountPlanRepository.findAll().size();
        // Create the AccountPlan
        restAccountPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountPlan)))
            .andExpect(status().isCreated());

        // Validate the AccountPlan in the database
        List<AccountPlan> accountPlanList = accountPlanRepository.findAll();
        assertThat(accountPlanList).hasSize(databaseSizeBeforeCreate + 1);
        AccountPlan testAccountPlan = accountPlanList.get(accountPlanList.size() - 1);
        assertThat(testAccountPlan.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testAccountPlan.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createAccountPlanWithExistingId() throws Exception {
        // Create the AccountPlan with an existing ID
        accountPlan.setId(1L);

        int databaseSizeBeforeCreate = accountPlanRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountPlan)))
            .andExpect(status().isBadRequest());

        // Validate the AccountPlan in the database
        List<AccountPlan> accountPlanList = accountPlanRepository.findAll();
        assertThat(accountPlanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountPlanRepository.findAll().size();
        // set the field null
        accountPlan.setCode(null);

        // Create the AccountPlan, which fails.

        restAccountPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountPlan)))
            .andExpect(status().isBadRequest());

        List<AccountPlan> accountPlanList = accountPlanRepository.findAll();
        assertThat(accountPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountPlanRepository.findAll().size();
        // set the field null
        accountPlan.setDescription(null);

        // Create the AccountPlan, which fails.

        restAccountPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountPlan)))
            .andExpect(status().isBadRequest());

        List<AccountPlan> accountPlanList = accountPlanRepository.findAll();
        assertThat(accountPlanList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAccountPlans() throws Exception {
        // Initialize the database
        accountPlanRepository.saveAndFlush(accountPlan);

        // Get all the accountPlanList
        restAccountPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountPlan.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getAccountPlan() throws Exception {
        // Initialize the database
        accountPlanRepository.saveAndFlush(accountPlan);

        // Get the accountPlan
        restAccountPlanMockMvc
            .perform(get(ENTITY_API_URL_ID, accountPlan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accountPlan.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingAccountPlan() throws Exception {
        // Get the accountPlan
        restAccountPlanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAccountPlan() throws Exception {
        // Initialize the database
        accountPlanRepository.saveAndFlush(accountPlan);

        int databaseSizeBeforeUpdate = accountPlanRepository.findAll().size();

        // Update the accountPlan
        AccountPlan updatedAccountPlan = accountPlanRepository.findById(accountPlan.getId()).get();
        // Disconnect from session so that the updates on updatedAccountPlan are not directly saved in db
        em.detach(updatedAccountPlan);
        updatedAccountPlan.code(UPDATED_CODE).description(UPDATED_DESCRIPTION);

        restAccountPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAccountPlan.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAccountPlan))
            )
            .andExpect(status().isOk());

        // Validate the AccountPlan in the database
        List<AccountPlan> accountPlanList = accountPlanRepository.findAll();
        assertThat(accountPlanList).hasSize(databaseSizeBeforeUpdate);
        AccountPlan testAccountPlan = accountPlanList.get(accountPlanList.size() - 1);
        assertThat(testAccountPlan.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testAccountPlan.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingAccountPlan() throws Exception {
        int databaseSizeBeforeUpdate = accountPlanRepository.findAll().size();
        accountPlan.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountPlan.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountPlan))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountPlan in the database
        List<AccountPlan> accountPlanList = accountPlanRepository.findAll();
        assertThat(accountPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccountPlan() throws Exception {
        int databaseSizeBeforeUpdate = accountPlanRepository.findAll().size();
        accountPlan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountPlan))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountPlan in the database
        List<AccountPlan> accountPlanList = accountPlanRepository.findAll();
        assertThat(accountPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccountPlan() throws Exception {
        int databaseSizeBeforeUpdate = accountPlanRepository.findAll().size();
        accountPlan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountPlanMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountPlan)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountPlan in the database
        List<AccountPlan> accountPlanList = accountPlanRepository.findAll();
        assertThat(accountPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAccountPlanWithPatch() throws Exception {
        // Initialize the database
        accountPlanRepository.saveAndFlush(accountPlan);

        int databaseSizeBeforeUpdate = accountPlanRepository.findAll().size();

        // Update the accountPlan using partial update
        AccountPlan partialUpdatedAccountPlan = new AccountPlan();
        partialUpdatedAccountPlan.setId(accountPlan.getId());

        partialUpdatedAccountPlan.description(UPDATED_DESCRIPTION);

        restAccountPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountPlan))
            )
            .andExpect(status().isOk());

        // Validate the AccountPlan in the database
        List<AccountPlan> accountPlanList = accountPlanRepository.findAll();
        assertThat(accountPlanList).hasSize(databaseSizeBeforeUpdate);
        AccountPlan testAccountPlan = accountPlanList.get(accountPlanList.size() - 1);
        assertThat(testAccountPlan.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testAccountPlan.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateAccountPlanWithPatch() throws Exception {
        // Initialize the database
        accountPlanRepository.saveAndFlush(accountPlan);

        int databaseSizeBeforeUpdate = accountPlanRepository.findAll().size();

        // Update the accountPlan using partial update
        AccountPlan partialUpdatedAccountPlan = new AccountPlan();
        partialUpdatedAccountPlan.setId(accountPlan.getId());

        partialUpdatedAccountPlan.code(UPDATED_CODE).description(UPDATED_DESCRIPTION);

        restAccountPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountPlan))
            )
            .andExpect(status().isOk());

        // Validate the AccountPlan in the database
        List<AccountPlan> accountPlanList = accountPlanRepository.findAll();
        assertThat(accountPlanList).hasSize(databaseSizeBeforeUpdate);
        AccountPlan testAccountPlan = accountPlanList.get(accountPlanList.size() - 1);
        assertThat(testAccountPlan.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testAccountPlan.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingAccountPlan() throws Exception {
        int databaseSizeBeforeUpdate = accountPlanRepository.findAll().size();
        accountPlan.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accountPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountPlan))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountPlan in the database
        List<AccountPlan> accountPlanList = accountPlanRepository.findAll();
        assertThat(accountPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccountPlan() throws Exception {
        int databaseSizeBeforeUpdate = accountPlanRepository.findAll().size();
        accountPlan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountPlan))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountPlan in the database
        List<AccountPlan> accountPlanList = accountPlanRepository.findAll();
        assertThat(accountPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccountPlan() throws Exception {
        int databaseSizeBeforeUpdate = accountPlanRepository.findAll().size();
        accountPlan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountPlanMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(accountPlan))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountPlan in the database
        List<AccountPlan> accountPlanList = accountPlanRepository.findAll();
        assertThat(accountPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAccountPlan() throws Exception {
        // Initialize the database
        accountPlanRepository.saveAndFlush(accountPlan);

        int databaseSizeBeforeDelete = accountPlanRepository.findAll().size();

        // Delete the accountPlan
        restAccountPlanMockMvc
            .perform(delete(ENTITY_API_URL_ID, accountPlan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AccountPlan> accountPlanList = accountPlanRepository.findAll();
        assertThat(accountPlanList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
