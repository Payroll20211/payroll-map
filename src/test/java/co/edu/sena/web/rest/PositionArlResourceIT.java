package co.edu.sena.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.sena.IntegrationTest;
import co.edu.sena.domain.PositionArl;
import co.edu.sena.repository.PositionArlRepository;
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
 * Integration tests for the {@link PositionArlResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PositionArlResourceIT {

    private static final Integer DEFAULT_RISK_CLASS = 1;
    private static final Integer UPDATED_RISK_CLASS = 2;

    private static final String DEFAULT_POSITION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSITION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/position-arls";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PositionArlRepository positionArlRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPositionArlMockMvc;

    private PositionArl positionArl;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PositionArl createEntity(EntityManager em) {
        PositionArl positionArl = new PositionArl()
            .riskClass(DEFAULT_RISK_CLASS)
            .positionCode(DEFAULT_POSITION_CODE)
            .position(DEFAULT_POSITION);
        return positionArl;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PositionArl createUpdatedEntity(EntityManager em) {
        PositionArl positionArl = new PositionArl()
            .riskClass(UPDATED_RISK_CLASS)
            .positionCode(UPDATED_POSITION_CODE)
            .position(UPDATED_POSITION);
        return positionArl;
    }

    @BeforeEach
    public void initTest() {
        positionArl = createEntity(em);
    }

    @Test
    @Transactional
    void createPositionArl() throws Exception {
        int databaseSizeBeforeCreate = positionArlRepository.findAll().size();
        // Create the PositionArl
        restPositionArlMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(positionArl)))
            .andExpect(status().isCreated());

        // Validate the PositionArl in the database
        List<PositionArl> positionArlList = positionArlRepository.findAll();
        assertThat(positionArlList).hasSize(databaseSizeBeforeCreate + 1);
        PositionArl testPositionArl = positionArlList.get(positionArlList.size() - 1);
        assertThat(testPositionArl.getRiskClass()).isEqualTo(DEFAULT_RISK_CLASS);
        assertThat(testPositionArl.getPositionCode()).isEqualTo(DEFAULT_POSITION_CODE);
        assertThat(testPositionArl.getPosition()).isEqualTo(DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void createPositionArlWithExistingId() throws Exception {
        // Create the PositionArl with an existing ID
        positionArl.setId(1L);

        int databaseSizeBeforeCreate = positionArlRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPositionArlMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(positionArl)))
            .andExpect(status().isBadRequest());

        // Validate the PositionArl in the database
        List<PositionArl> positionArlList = positionArlRepository.findAll();
        assertThat(positionArlList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRiskClassIsRequired() throws Exception {
        int databaseSizeBeforeTest = positionArlRepository.findAll().size();
        // set the field null
        positionArl.setRiskClass(null);

        // Create the PositionArl, which fails.

        restPositionArlMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(positionArl)))
            .andExpect(status().isBadRequest());

        List<PositionArl> positionArlList = positionArlRepository.findAll();
        assertThat(positionArlList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPositionCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = positionArlRepository.findAll().size();
        // set the field null
        positionArl.setPositionCode(null);

        // Create the PositionArl, which fails.

        restPositionArlMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(positionArl)))
            .andExpect(status().isBadRequest());

        List<PositionArl> positionArlList = positionArlRepository.findAll();
        assertThat(positionArlList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPositionIsRequired() throws Exception {
        int databaseSizeBeforeTest = positionArlRepository.findAll().size();
        // set the field null
        positionArl.setPosition(null);

        // Create the PositionArl, which fails.

        restPositionArlMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(positionArl)))
            .andExpect(status().isBadRequest());

        List<PositionArl> positionArlList = positionArlRepository.findAll();
        assertThat(positionArlList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPositionArls() throws Exception {
        // Initialize the database
        positionArlRepository.saveAndFlush(positionArl);

        // Get all the positionArlList
        restPositionArlMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(positionArl.getId().intValue())))
            .andExpect(jsonPath("$.[*].riskClass").value(hasItem(DEFAULT_RISK_CLASS)))
            .andExpect(jsonPath("$.[*].positionCode").value(hasItem(DEFAULT_POSITION_CODE)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    void getPositionArl() throws Exception {
        // Initialize the database
        positionArlRepository.saveAndFlush(positionArl);

        // Get the positionArl
        restPositionArlMockMvc
            .perform(get(ENTITY_API_URL_ID, positionArl.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(positionArl.getId().intValue()))
            .andExpect(jsonPath("$.riskClass").value(DEFAULT_RISK_CLASS))
            .andExpect(jsonPath("$.positionCode").value(DEFAULT_POSITION_CODE))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getNonExistingPositionArl() throws Exception {
        // Get the positionArl
        restPositionArlMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPositionArl() throws Exception {
        // Initialize the database
        positionArlRepository.saveAndFlush(positionArl);

        int databaseSizeBeforeUpdate = positionArlRepository.findAll().size();

        // Update the positionArl
        PositionArl updatedPositionArl = positionArlRepository.findById(positionArl.getId()).get();
        // Disconnect from session so that the updates on updatedPositionArl are not directly saved in db
        em.detach(updatedPositionArl);
        updatedPositionArl.riskClass(UPDATED_RISK_CLASS).positionCode(UPDATED_POSITION_CODE).position(UPDATED_POSITION);

        restPositionArlMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPositionArl.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPositionArl))
            )
            .andExpect(status().isOk());

        // Validate the PositionArl in the database
        List<PositionArl> positionArlList = positionArlRepository.findAll();
        assertThat(positionArlList).hasSize(databaseSizeBeforeUpdate);
        PositionArl testPositionArl = positionArlList.get(positionArlList.size() - 1);
        assertThat(testPositionArl.getRiskClass()).isEqualTo(UPDATED_RISK_CLASS);
        assertThat(testPositionArl.getPositionCode()).isEqualTo(UPDATED_POSITION_CODE);
        assertThat(testPositionArl.getPosition()).isEqualTo(UPDATED_POSITION);
    }

    @Test
    @Transactional
    void putNonExistingPositionArl() throws Exception {
        int databaseSizeBeforeUpdate = positionArlRepository.findAll().size();
        positionArl.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPositionArlMockMvc
            .perform(
                put(ENTITY_API_URL_ID, positionArl.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(positionArl))
            )
            .andExpect(status().isBadRequest());

        // Validate the PositionArl in the database
        List<PositionArl> positionArlList = positionArlRepository.findAll();
        assertThat(positionArlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPositionArl() throws Exception {
        int databaseSizeBeforeUpdate = positionArlRepository.findAll().size();
        positionArl.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionArlMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(positionArl))
            )
            .andExpect(status().isBadRequest());

        // Validate the PositionArl in the database
        List<PositionArl> positionArlList = positionArlRepository.findAll();
        assertThat(positionArlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPositionArl() throws Exception {
        int databaseSizeBeforeUpdate = positionArlRepository.findAll().size();
        positionArl.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionArlMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(positionArl)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PositionArl in the database
        List<PositionArl> positionArlList = positionArlRepository.findAll();
        assertThat(positionArlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePositionArlWithPatch() throws Exception {
        // Initialize the database
        positionArlRepository.saveAndFlush(positionArl);

        int databaseSizeBeforeUpdate = positionArlRepository.findAll().size();

        // Update the positionArl using partial update
        PositionArl partialUpdatedPositionArl = new PositionArl();
        partialUpdatedPositionArl.setId(positionArl.getId());

        partialUpdatedPositionArl.positionCode(UPDATED_POSITION_CODE).position(UPDATED_POSITION);

        restPositionArlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPositionArl.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPositionArl))
            )
            .andExpect(status().isOk());

        // Validate the PositionArl in the database
        List<PositionArl> positionArlList = positionArlRepository.findAll();
        assertThat(positionArlList).hasSize(databaseSizeBeforeUpdate);
        PositionArl testPositionArl = positionArlList.get(positionArlList.size() - 1);
        assertThat(testPositionArl.getRiskClass()).isEqualTo(DEFAULT_RISK_CLASS);
        assertThat(testPositionArl.getPositionCode()).isEqualTo(UPDATED_POSITION_CODE);
        assertThat(testPositionArl.getPosition()).isEqualTo(UPDATED_POSITION);
    }

    @Test
    @Transactional
    void fullUpdatePositionArlWithPatch() throws Exception {
        // Initialize the database
        positionArlRepository.saveAndFlush(positionArl);

        int databaseSizeBeforeUpdate = positionArlRepository.findAll().size();

        // Update the positionArl using partial update
        PositionArl partialUpdatedPositionArl = new PositionArl();
        partialUpdatedPositionArl.setId(positionArl.getId());

        partialUpdatedPositionArl.riskClass(UPDATED_RISK_CLASS).positionCode(UPDATED_POSITION_CODE).position(UPDATED_POSITION);

        restPositionArlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPositionArl.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPositionArl))
            )
            .andExpect(status().isOk());

        // Validate the PositionArl in the database
        List<PositionArl> positionArlList = positionArlRepository.findAll();
        assertThat(positionArlList).hasSize(databaseSizeBeforeUpdate);
        PositionArl testPositionArl = positionArlList.get(positionArlList.size() - 1);
        assertThat(testPositionArl.getRiskClass()).isEqualTo(UPDATED_RISK_CLASS);
        assertThat(testPositionArl.getPositionCode()).isEqualTo(UPDATED_POSITION_CODE);
        assertThat(testPositionArl.getPosition()).isEqualTo(UPDATED_POSITION);
    }

    @Test
    @Transactional
    void patchNonExistingPositionArl() throws Exception {
        int databaseSizeBeforeUpdate = positionArlRepository.findAll().size();
        positionArl.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPositionArlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, positionArl.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(positionArl))
            )
            .andExpect(status().isBadRequest());

        // Validate the PositionArl in the database
        List<PositionArl> positionArlList = positionArlRepository.findAll();
        assertThat(positionArlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPositionArl() throws Exception {
        int databaseSizeBeforeUpdate = positionArlRepository.findAll().size();
        positionArl.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionArlMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(positionArl))
            )
            .andExpect(status().isBadRequest());

        // Validate the PositionArl in the database
        List<PositionArl> positionArlList = positionArlRepository.findAll();
        assertThat(positionArlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPositionArl() throws Exception {
        int databaseSizeBeforeUpdate = positionArlRepository.findAll().size();
        positionArl.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPositionArlMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(positionArl))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PositionArl in the database
        List<PositionArl> positionArlList = positionArlRepository.findAll();
        assertThat(positionArlList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePositionArl() throws Exception {
        // Initialize the database
        positionArlRepository.saveAndFlush(positionArl);

        int databaseSizeBeforeDelete = positionArlRepository.findAll().size();

        // Delete the positionArl
        restPositionArlMockMvc
            .perform(delete(ENTITY_API_URL_ID, positionArl.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PositionArl> positionArlList = positionArlRepository.findAll();
        assertThat(positionArlList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
