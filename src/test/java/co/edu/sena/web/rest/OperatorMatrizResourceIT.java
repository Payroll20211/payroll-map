package co.edu.sena.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.sena.IntegrationTest;
import co.edu.sena.domain.OperatorMatriz;
import co.edu.sena.repository.OperatorMatrizRepository;
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
 * Integration tests for the {@link OperatorMatrizResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OperatorMatrizResourceIT {

    private static final Integer DEFAULT_NUMBERID = 1;
    private static final Integer UPDATED_NUMBERID = 2;

    private static final Integer DEFAULT_DIGITVERIFICATION = 1;
    private static final Integer UPDATED_DIGITVERIFICATION = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/operator-matrizs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OperatorMatrizRepository operatorMatrizRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOperatorMatrizMockMvc;

    private OperatorMatriz operatorMatriz;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OperatorMatriz createEntity(EntityManager em) {
        OperatorMatriz operatorMatriz = new OperatorMatriz()
            .numberid(DEFAULT_NUMBERID)
            .digitverification(DEFAULT_DIGITVERIFICATION)
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS)
            .city(DEFAULT_CITY)
            .email(DEFAULT_EMAIL);
        return operatorMatriz;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OperatorMatriz createUpdatedEntity(EntityManager em) {
        OperatorMatriz operatorMatriz = new OperatorMatriz()
            .numberid(UPDATED_NUMBERID)
            .digitverification(UPDATED_DIGITVERIFICATION)
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .email(UPDATED_EMAIL);
        return operatorMatriz;
    }

    @BeforeEach
    public void initTest() {
        operatorMatriz = createEntity(em);
    }

    @Test
    @Transactional
    void createOperatorMatriz() throws Exception {
        int databaseSizeBeforeCreate = operatorMatrizRepository.findAll().size();
        // Create the OperatorMatriz
        restOperatorMatrizMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatorMatriz))
            )
            .andExpect(status().isCreated());

        // Validate the OperatorMatriz in the database
        List<OperatorMatriz> operatorMatrizList = operatorMatrizRepository.findAll();
        assertThat(operatorMatrizList).hasSize(databaseSizeBeforeCreate + 1);
        OperatorMatriz testOperatorMatriz = operatorMatrizList.get(operatorMatrizList.size() - 1);
        assertThat(testOperatorMatriz.getNumberid()).isEqualTo(DEFAULT_NUMBERID);
        assertThat(testOperatorMatriz.getDigitverification()).isEqualTo(DEFAULT_DIGITVERIFICATION);
        assertThat(testOperatorMatriz.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOperatorMatriz.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testOperatorMatriz.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testOperatorMatriz.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void createOperatorMatrizWithExistingId() throws Exception {
        // Create the OperatorMatriz with an existing ID
        operatorMatriz.setId(1L);

        int databaseSizeBeforeCreate = operatorMatrizRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperatorMatrizMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatorMatriz))
            )
            .andExpect(status().isBadRequest());

        // Validate the OperatorMatriz in the database
        List<OperatorMatriz> operatorMatrizList = operatorMatrizRepository.findAll();
        assertThat(operatorMatrizList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = operatorMatrizRepository.findAll().size();
        // set the field null
        operatorMatriz.setName(null);

        // Create the OperatorMatriz, which fails.

        restOperatorMatrizMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatorMatriz))
            )
            .andExpect(status().isBadRequest());

        List<OperatorMatriz> operatorMatrizList = operatorMatrizRepository.findAll();
        assertThat(operatorMatrizList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = operatorMatrizRepository.findAll().size();
        // set the field null
        operatorMatriz.setAddress(null);

        // Create the OperatorMatriz, which fails.

        restOperatorMatrizMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatorMatriz))
            )
            .andExpect(status().isBadRequest());

        List<OperatorMatriz> operatorMatrizList = operatorMatrizRepository.findAll();
        assertThat(operatorMatrizList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = operatorMatrizRepository.findAll().size();
        // set the field null
        operatorMatriz.setCity(null);

        // Create the OperatorMatriz, which fails.

        restOperatorMatrizMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatorMatriz))
            )
            .andExpect(status().isBadRequest());

        List<OperatorMatriz> operatorMatrizList = operatorMatrizRepository.findAll();
        assertThat(operatorMatrizList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = operatorMatrizRepository.findAll().size();
        // set the field null
        operatorMatriz.setEmail(null);

        // Create the OperatorMatriz, which fails.

        restOperatorMatrizMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatorMatriz))
            )
            .andExpect(status().isBadRequest());

        List<OperatorMatriz> operatorMatrizList = operatorMatrizRepository.findAll();
        assertThat(operatorMatrizList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOperatorMatrizs() throws Exception {
        // Initialize the database
        operatorMatrizRepository.saveAndFlush(operatorMatriz);

        // Get all the operatorMatrizList
        restOperatorMatrizMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operatorMatriz.getId().intValue())))
            .andExpect(jsonPath("$.[*].numberid").value(hasItem(DEFAULT_NUMBERID)))
            .andExpect(jsonPath("$.[*].digitverification").value(hasItem(DEFAULT_DIGITVERIFICATION)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getOperatorMatriz() throws Exception {
        // Initialize the database
        operatorMatrizRepository.saveAndFlush(operatorMatriz);

        // Get the operatorMatriz
        restOperatorMatrizMockMvc
            .perform(get(ENTITY_API_URL_ID, operatorMatriz.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(operatorMatriz.getId().intValue()))
            .andExpect(jsonPath("$.numberid").value(DEFAULT_NUMBERID))
            .andExpect(jsonPath("$.digitverification").value(DEFAULT_DIGITVERIFICATION))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingOperatorMatriz() throws Exception {
        // Get the operatorMatriz
        restOperatorMatrizMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOperatorMatriz() throws Exception {
        // Initialize the database
        operatorMatrizRepository.saveAndFlush(operatorMatriz);

        int databaseSizeBeforeUpdate = operatorMatrizRepository.findAll().size();

        // Update the operatorMatriz
        OperatorMatriz updatedOperatorMatriz = operatorMatrizRepository.findById(operatorMatriz.getId()).get();
        // Disconnect from session so that the updates on updatedOperatorMatriz are not directly saved in db
        em.detach(updatedOperatorMatriz);
        updatedOperatorMatriz
            .numberid(UPDATED_NUMBERID)
            .digitverification(UPDATED_DIGITVERIFICATION)
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .email(UPDATED_EMAIL);

        restOperatorMatrizMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOperatorMatriz.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOperatorMatriz))
            )
            .andExpect(status().isOk());

        // Validate the OperatorMatriz in the database
        List<OperatorMatriz> operatorMatrizList = operatorMatrizRepository.findAll();
        assertThat(operatorMatrizList).hasSize(databaseSizeBeforeUpdate);
        OperatorMatriz testOperatorMatriz = operatorMatrizList.get(operatorMatrizList.size() - 1);
        assertThat(testOperatorMatriz.getNumberid()).isEqualTo(UPDATED_NUMBERID);
        assertThat(testOperatorMatriz.getDigitverification()).isEqualTo(UPDATED_DIGITVERIFICATION);
        assertThat(testOperatorMatriz.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOperatorMatriz.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testOperatorMatriz.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testOperatorMatriz.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void putNonExistingOperatorMatriz() throws Exception {
        int databaseSizeBeforeUpdate = operatorMatrizRepository.findAll().size();
        operatorMatriz.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperatorMatrizMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operatorMatriz.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operatorMatriz))
            )
            .andExpect(status().isBadRequest());

        // Validate the OperatorMatriz in the database
        List<OperatorMatriz> operatorMatrizList = operatorMatrizRepository.findAll();
        assertThat(operatorMatrizList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOperatorMatriz() throws Exception {
        int databaseSizeBeforeUpdate = operatorMatrizRepository.findAll().size();
        operatorMatriz.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatorMatrizMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(operatorMatriz))
            )
            .andExpect(status().isBadRequest());

        // Validate the OperatorMatriz in the database
        List<OperatorMatriz> operatorMatrizList = operatorMatrizRepository.findAll();
        assertThat(operatorMatrizList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOperatorMatriz() throws Exception {
        int databaseSizeBeforeUpdate = operatorMatrizRepository.findAll().size();
        operatorMatriz.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatorMatrizMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(operatorMatriz)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OperatorMatriz in the database
        List<OperatorMatriz> operatorMatrizList = operatorMatrizRepository.findAll();
        assertThat(operatorMatrizList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOperatorMatrizWithPatch() throws Exception {
        // Initialize the database
        operatorMatrizRepository.saveAndFlush(operatorMatriz);

        int databaseSizeBeforeUpdate = operatorMatrizRepository.findAll().size();

        // Update the operatorMatriz using partial update
        OperatorMatriz partialUpdatedOperatorMatriz = new OperatorMatriz();
        partialUpdatedOperatorMatriz.setId(operatorMatriz.getId());

        partialUpdatedOperatorMatriz.city(UPDATED_CITY);

        restOperatorMatrizMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperatorMatriz.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperatorMatriz))
            )
            .andExpect(status().isOk());

        // Validate the OperatorMatriz in the database
        List<OperatorMatriz> operatorMatrizList = operatorMatrizRepository.findAll();
        assertThat(operatorMatrizList).hasSize(databaseSizeBeforeUpdate);
        OperatorMatriz testOperatorMatriz = operatorMatrizList.get(operatorMatrizList.size() - 1);
        assertThat(testOperatorMatriz.getNumberid()).isEqualTo(DEFAULT_NUMBERID);
        assertThat(testOperatorMatriz.getDigitverification()).isEqualTo(DEFAULT_DIGITVERIFICATION);
        assertThat(testOperatorMatriz.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOperatorMatriz.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testOperatorMatriz.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testOperatorMatriz.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdateOperatorMatrizWithPatch() throws Exception {
        // Initialize the database
        operatorMatrizRepository.saveAndFlush(operatorMatriz);

        int databaseSizeBeforeUpdate = operatorMatrizRepository.findAll().size();

        // Update the operatorMatriz using partial update
        OperatorMatriz partialUpdatedOperatorMatriz = new OperatorMatriz();
        partialUpdatedOperatorMatriz.setId(operatorMatriz.getId());

        partialUpdatedOperatorMatriz
            .numberid(UPDATED_NUMBERID)
            .digitverification(UPDATED_DIGITVERIFICATION)
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .email(UPDATED_EMAIL);

        restOperatorMatrizMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperatorMatriz.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOperatorMatriz))
            )
            .andExpect(status().isOk());

        // Validate the OperatorMatriz in the database
        List<OperatorMatriz> operatorMatrizList = operatorMatrizRepository.findAll();
        assertThat(operatorMatrizList).hasSize(databaseSizeBeforeUpdate);
        OperatorMatriz testOperatorMatriz = operatorMatrizList.get(operatorMatrizList.size() - 1);
        assertThat(testOperatorMatriz.getNumberid()).isEqualTo(UPDATED_NUMBERID);
        assertThat(testOperatorMatriz.getDigitverification()).isEqualTo(UPDATED_DIGITVERIFICATION);
        assertThat(testOperatorMatriz.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOperatorMatriz.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testOperatorMatriz.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testOperatorMatriz.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingOperatorMatriz() throws Exception {
        int databaseSizeBeforeUpdate = operatorMatrizRepository.findAll().size();
        operatorMatriz.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperatorMatrizMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, operatorMatriz.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operatorMatriz))
            )
            .andExpect(status().isBadRequest());

        // Validate the OperatorMatriz in the database
        List<OperatorMatriz> operatorMatrizList = operatorMatrizRepository.findAll();
        assertThat(operatorMatrizList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOperatorMatriz() throws Exception {
        int databaseSizeBeforeUpdate = operatorMatrizRepository.findAll().size();
        operatorMatriz.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatorMatrizMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(operatorMatriz))
            )
            .andExpect(status().isBadRequest());

        // Validate the OperatorMatriz in the database
        List<OperatorMatriz> operatorMatrizList = operatorMatrizRepository.findAll();
        assertThat(operatorMatrizList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOperatorMatriz() throws Exception {
        int databaseSizeBeforeUpdate = operatorMatrizRepository.findAll().size();
        operatorMatriz.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperatorMatrizMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(operatorMatriz))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OperatorMatriz in the database
        List<OperatorMatriz> operatorMatrizList = operatorMatrizRepository.findAll();
        assertThat(operatorMatrizList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOperatorMatriz() throws Exception {
        // Initialize the database
        operatorMatrizRepository.saveAndFlush(operatorMatriz);

        int databaseSizeBeforeDelete = operatorMatrizRepository.findAll().size();

        // Delete the operatorMatriz
        restOperatorMatrizMockMvc
            .perform(delete(ENTITY_API_URL_ID, operatorMatriz.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OperatorMatriz> operatorMatrizList = operatorMatrizRepository.findAll();
        assertThat(operatorMatrizList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
