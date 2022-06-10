package co.edu.sena.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.sena.IntegrationTest;
import co.edu.sena.domain.Employee;
import co.edu.sena.domain.Income;
import co.edu.sena.repository.IncomeRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link IncomeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class IncomeResourceIT {

    private static final String DEFAULT_INCOME_CODE = "AAAAAAAAAA";
    private static final String UPDATED_INCOME_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/incomes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IncomeRepository incomeRepository;

    @Mock
    private IncomeRepository incomeRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIncomeMockMvc;

    private Income income;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Income createEntity(EntityManager em) {
        Income income = new Income().incomeCode(DEFAULT_INCOME_CODE).description(DEFAULT_DESCRIPTION);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        income.getIncomes().add(employee);
        return income;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Income createUpdatedEntity(EntityManager em) {
        Income income = new Income().incomeCode(UPDATED_INCOME_CODE).description(UPDATED_DESCRIPTION);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        income.getIncomes().add(employee);
        return income;
    }

    @BeforeEach
    public void initTest() {
        income = createEntity(em);
    }

    @Test
    @Transactional
    void createIncome() throws Exception {
        int databaseSizeBeforeCreate = incomeRepository.findAll().size();
        // Create the Income
        restIncomeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(income)))
            .andExpect(status().isCreated());

        // Validate the Income in the database
        List<Income> incomeList = incomeRepository.findAll();
        assertThat(incomeList).hasSize(databaseSizeBeforeCreate + 1);
        Income testIncome = incomeList.get(incomeList.size() - 1);
        assertThat(testIncome.getIncomeCode()).isEqualTo(DEFAULT_INCOME_CODE);
        assertThat(testIncome.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createIncomeWithExistingId() throws Exception {
        // Create the Income with an existing ID
        income.setId(1L);

        int databaseSizeBeforeCreate = incomeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIncomeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(income)))
            .andExpect(status().isBadRequest());

        // Validate the Income in the database
        List<Income> incomeList = incomeRepository.findAll();
        assertThat(incomeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIncomeCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = incomeRepository.findAll().size();
        // set the field null
        income.setIncomeCode(null);

        // Create the Income, which fails.

        restIncomeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(income)))
            .andExpect(status().isBadRequest());

        List<Income> incomeList = incomeRepository.findAll();
        assertThat(incomeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = incomeRepository.findAll().size();
        // set the field null
        income.setDescription(null);

        // Create the Income, which fails.

        restIncomeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(income)))
            .andExpect(status().isBadRequest());

        List<Income> incomeList = incomeRepository.findAll();
        assertThat(incomeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIncomes() throws Exception {
        // Initialize the database
        incomeRepository.saveAndFlush(income);

        // Get all the incomeList
        restIncomeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(income.getId().intValue())))
            .andExpect(jsonPath("$.[*].incomeCode").value(hasItem(DEFAULT_INCOME_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllIncomesWithEagerRelationshipsIsEnabled() throws Exception {
        when(incomeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restIncomeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(incomeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllIncomesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(incomeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restIncomeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(incomeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getIncome() throws Exception {
        // Initialize the database
        incomeRepository.saveAndFlush(income);

        // Get the income
        restIncomeMockMvc
            .perform(get(ENTITY_API_URL_ID, income.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(income.getId().intValue()))
            .andExpect(jsonPath("$.incomeCode").value(DEFAULT_INCOME_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingIncome() throws Exception {
        // Get the income
        restIncomeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIncome() throws Exception {
        // Initialize the database
        incomeRepository.saveAndFlush(income);

        int databaseSizeBeforeUpdate = incomeRepository.findAll().size();

        // Update the income
        Income updatedIncome = incomeRepository.findById(income.getId()).get();
        // Disconnect from session so that the updates on updatedIncome are not directly saved in db
        em.detach(updatedIncome);
        updatedIncome.incomeCode(UPDATED_INCOME_CODE).description(UPDATED_DESCRIPTION);

        restIncomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIncome.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIncome))
            )
            .andExpect(status().isOk());

        // Validate the Income in the database
        List<Income> incomeList = incomeRepository.findAll();
        assertThat(incomeList).hasSize(databaseSizeBeforeUpdate);
        Income testIncome = incomeList.get(incomeList.size() - 1);
        assertThat(testIncome.getIncomeCode()).isEqualTo(UPDATED_INCOME_CODE);
        assertThat(testIncome.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingIncome() throws Exception {
        int databaseSizeBeforeUpdate = incomeRepository.findAll().size();
        income.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, income.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(income))
            )
            .andExpect(status().isBadRequest());

        // Validate the Income in the database
        List<Income> incomeList = incomeRepository.findAll();
        assertThat(incomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIncome() throws Exception {
        int databaseSizeBeforeUpdate = incomeRepository.findAll().size();
        income.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(income))
            )
            .andExpect(status().isBadRequest());

        // Validate the Income in the database
        List<Income> incomeList = incomeRepository.findAll();
        assertThat(incomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIncome() throws Exception {
        int databaseSizeBeforeUpdate = incomeRepository.findAll().size();
        income.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncomeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(income)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Income in the database
        List<Income> incomeList = incomeRepository.findAll();
        assertThat(incomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIncomeWithPatch() throws Exception {
        // Initialize the database
        incomeRepository.saveAndFlush(income);

        int databaseSizeBeforeUpdate = incomeRepository.findAll().size();

        // Update the income using partial update
        Income partialUpdatedIncome = new Income();
        partialUpdatedIncome.setId(income.getId());

        partialUpdatedIncome.description(UPDATED_DESCRIPTION);

        restIncomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIncome.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIncome))
            )
            .andExpect(status().isOk());

        // Validate the Income in the database
        List<Income> incomeList = incomeRepository.findAll();
        assertThat(incomeList).hasSize(databaseSizeBeforeUpdate);
        Income testIncome = incomeList.get(incomeList.size() - 1);
        assertThat(testIncome.getIncomeCode()).isEqualTo(DEFAULT_INCOME_CODE);
        assertThat(testIncome.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateIncomeWithPatch() throws Exception {
        // Initialize the database
        incomeRepository.saveAndFlush(income);

        int databaseSizeBeforeUpdate = incomeRepository.findAll().size();

        // Update the income using partial update
        Income partialUpdatedIncome = new Income();
        partialUpdatedIncome.setId(income.getId());

        partialUpdatedIncome.incomeCode(UPDATED_INCOME_CODE).description(UPDATED_DESCRIPTION);

        restIncomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIncome.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIncome))
            )
            .andExpect(status().isOk());

        // Validate the Income in the database
        List<Income> incomeList = incomeRepository.findAll();
        assertThat(incomeList).hasSize(databaseSizeBeforeUpdate);
        Income testIncome = incomeList.get(incomeList.size() - 1);
        assertThat(testIncome.getIncomeCode()).isEqualTo(UPDATED_INCOME_CODE);
        assertThat(testIncome.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingIncome() throws Exception {
        int databaseSizeBeforeUpdate = incomeRepository.findAll().size();
        income.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, income.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(income))
            )
            .andExpect(status().isBadRequest());

        // Validate the Income in the database
        List<Income> incomeList = incomeRepository.findAll();
        assertThat(incomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIncome() throws Exception {
        int databaseSizeBeforeUpdate = incomeRepository.findAll().size();
        income.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(income))
            )
            .andExpect(status().isBadRequest());

        // Validate the Income in the database
        List<Income> incomeList = incomeRepository.findAll();
        assertThat(incomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIncome() throws Exception {
        int databaseSizeBeforeUpdate = incomeRepository.findAll().size();
        income.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIncomeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(income)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Income in the database
        List<Income> incomeList = incomeRepository.findAll();
        assertThat(incomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIncome() throws Exception {
        // Initialize the database
        incomeRepository.saveAndFlush(income);

        int databaseSizeBeforeDelete = incomeRepository.findAll().size();

        // Delete the income
        restIncomeMockMvc
            .perform(delete(ENTITY_API_URL_ID, income.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Income> incomeList = incomeRepository.findAll();
        assertThat(incomeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
