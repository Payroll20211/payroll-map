package co.edu.sena.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.sena.IntegrationTest;
import co.edu.sena.domain.Deduction;
import co.edu.sena.domain.Employee;
import co.edu.sena.repository.DeductionRepository;
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
 * Integration tests for the {@link DeductionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DeductionResourceIT {

    private static final String DEFAULT_DEDUCTION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DEDUCTION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/deductions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeductionRepository deductionRepository;

    @Mock
    private DeductionRepository deductionRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeductionMockMvc;

    private Deduction deduction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deduction createEntity(EntityManager em) {
        Deduction deduction = new Deduction().deductionCode(DEFAULT_DEDUCTION_CODE).description(DEFAULT_DESCRIPTION);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        deduction.getDeductions().add(employee);
        return deduction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deduction createUpdatedEntity(EntityManager em) {
        Deduction deduction = new Deduction().deductionCode(UPDATED_DEDUCTION_CODE).description(UPDATED_DESCRIPTION);
        // Add required entity
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createUpdatedEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        deduction.getDeductions().add(employee);
        return deduction;
    }

    @BeforeEach
    public void initTest() {
        deduction = createEntity(em);
    }

    @Test
    @Transactional
    void createDeduction() throws Exception {
        int databaseSizeBeforeCreate = deductionRepository.findAll().size();
        // Create the Deduction
        restDeductionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deduction)))
            .andExpect(status().isCreated());

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll();
        assertThat(deductionList).hasSize(databaseSizeBeforeCreate + 1);
        Deduction testDeduction = deductionList.get(deductionList.size() - 1);
        assertThat(testDeduction.getDeductionCode()).isEqualTo(DEFAULT_DEDUCTION_CODE);
        assertThat(testDeduction.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createDeductionWithExistingId() throws Exception {
        // Create the Deduction with an existing ID
        deduction.setId(1L);

        int databaseSizeBeforeCreate = deductionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeductionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deduction)))
            .andExpect(status().isBadRequest());

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll();
        assertThat(deductionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDeductionCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = deductionRepository.findAll().size();
        // set the field null
        deduction.setDeductionCode(null);

        // Create the Deduction, which fails.

        restDeductionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deduction)))
            .andExpect(status().isBadRequest());

        List<Deduction> deductionList = deductionRepository.findAll();
        assertThat(deductionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = deductionRepository.findAll().size();
        // set the field null
        deduction.setDescription(null);

        // Create the Deduction, which fails.

        restDeductionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deduction)))
            .andExpect(status().isBadRequest());

        List<Deduction> deductionList = deductionRepository.findAll();
        assertThat(deductionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDeductions() throws Exception {
        // Initialize the database
        deductionRepository.saveAndFlush(deduction);

        // Get all the deductionList
        restDeductionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deduction.getId().intValue())))
            .andExpect(jsonPath("$.[*].deductionCode").value(hasItem(DEFAULT_DEDUCTION_CODE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDeductionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(deductionRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDeductionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(deductionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDeductionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(deductionRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDeductionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(deductionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDeduction() throws Exception {
        // Initialize the database
        deductionRepository.saveAndFlush(deduction);

        // Get the deduction
        restDeductionMockMvc
            .perform(get(ENTITY_API_URL_ID, deduction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deduction.getId().intValue()))
            .andExpect(jsonPath("$.deductionCode").value(DEFAULT_DEDUCTION_CODE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingDeduction() throws Exception {
        // Get the deduction
        restDeductionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDeduction() throws Exception {
        // Initialize the database
        deductionRepository.saveAndFlush(deduction);

        int databaseSizeBeforeUpdate = deductionRepository.findAll().size();

        // Update the deduction
        Deduction updatedDeduction = deductionRepository.findById(deduction.getId()).get();
        // Disconnect from session so that the updates on updatedDeduction are not directly saved in db
        em.detach(updatedDeduction);
        updatedDeduction.deductionCode(UPDATED_DEDUCTION_CODE).description(UPDATED_DESCRIPTION);

        restDeductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDeduction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDeduction))
            )
            .andExpect(status().isOk());

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll();
        assertThat(deductionList).hasSize(databaseSizeBeforeUpdate);
        Deduction testDeduction = deductionList.get(deductionList.size() - 1);
        assertThat(testDeduction.getDeductionCode()).isEqualTo(UPDATED_DEDUCTION_CODE);
        assertThat(testDeduction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingDeduction() throws Exception {
        int databaseSizeBeforeUpdate = deductionRepository.findAll().size();
        deduction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deduction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deduction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll();
        assertThat(deductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeduction() throws Exception {
        int databaseSizeBeforeUpdate = deductionRepository.findAll().size();
        deduction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deduction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll();
        assertThat(deductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeduction() throws Exception {
        int databaseSizeBeforeUpdate = deductionRepository.findAll().size();
        deduction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeductionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deduction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll();
        assertThat(deductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeductionWithPatch() throws Exception {
        // Initialize the database
        deductionRepository.saveAndFlush(deduction);

        int databaseSizeBeforeUpdate = deductionRepository.findAll().size();

        // Update the deduction using partial update
        Deduction partialUpdatedDeduction = new Deduction();
        partialUpdatedDeduction.setId(deduction.getId());

        partialUpdatedDeduction.description(UPDATED_DESCRIPTION);

        restDeductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeduction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeduction))
            )
            .andExpect(status().isOk());

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll();
        assertThat(deductionList).hasSize(databaseSizeBeforeUpdate);
        Deduction testDeduction = deductionList.get(deductionList.size() - 1);
        assertThat(testDeduction.getDeductionCode()).isEqualTo(DEFAULT_DEDUCTION_CODE);
        assertThat(testDeduction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateDeductionWithPatch() throws Exception {
        // Initialize the database
        deductionRepository.saveAndFlush(deduction);

        int databaseSizeBeforeUpdate = deductionRepository.findAll().size();

        // Update the deduction using partial update
        Deduction partialUpdatedDeduction = new Deduction();
        partialUpdatedDeduction.setId(deduction.getId());

        partialUpdatedDeduction.deductionCode(UPDATED_DEDUCTION_CODE).description(UPDATED_DESCRIPTION);

        restDeductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeduction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeduction))
            )
            .andExpect(status().isOk());

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll();
        assertThat(deductionList).hasSize(databaseSizeBeforeUpdate);
        Deduction testDeduction = deductionList.get(deductionList.size() - 1);
        assertThat(testDeduction.getDeductionCode()).isEqualTo(UPDATED_DEDUCTION_CODE);
        assertThat(testDeduction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingDeduction() throws Exception {
        int databaseSizeBeforeUpdate = deductionRepository.findAll().size();
        deduction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deduction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deduction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll();
        assertThat(deductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeduction() throws Exception {
        int databaseSizeBeforeUpdate = deductionRepository.findAll().size();
        deduction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deduction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll();
        assertThat(deductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeduction() throws Exception {
        int databaseSizeBeforeUpdate = deductionRepository.findAll().size();
        deduction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeductionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(deduction))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deduction in the database
        List<Deduction> deductionList = deductionRepository.findAll();
        assertThat(deductionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeduction() throws Exception {
        // Initialize the database
        deductionRepository.saveAndFlush(deduction);

        int databaseSizeBeforeDelete = deductionRepository.findAll().size();

        // Delete the deduction
        restDeductionMockMvc
            .perform(delete(ENTITY_API_URL_ID, deduction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Deduction> deductionList = deductionRepository.findAll();
        assertThat(deductionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
