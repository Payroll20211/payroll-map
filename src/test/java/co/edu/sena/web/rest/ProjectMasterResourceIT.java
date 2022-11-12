package co.edu.sena.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.sena.IntegrationTest;
import co.edu.sena.domain.CostCenter;
import co.edu.sena.domain.ProjectMaster;
import co.edu.sena.repository.ProjectMasterRepository;
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
 * Integration tests for the {@link ProjectMasterResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProjectMasterResourceIT {

    private static final String DEFAULT_PROJECT_MASTER_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_MASTER_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PROJECT_MASTER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_MASTER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COST_CENTER_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_COST_CENTER_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_PROJECT_DIRECTOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_DIRECTOR_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/project-masters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProjectMasterRepository projectMasterRepository;

    @Mock
    private ProjectMasterRepository projectMasterRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProjectMasterMockMvc;

    private ProjectMaster projectMaster;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectMaster createEntity(EntityManager em) {
        ProjectMaster projectMaster = new ProjectMaster()
            .projectMasterCode(DEFAULT_PROJECT_MASTER_CODE)
            .projectMasterName(DEFAULT_PROJECT_MASTER_NAME)
            .costCenterType(DEFAULT_COST_CENTER_TYPE)
            .projectDirectorName(DEFAULT_PROJECT_DIRECTOR_NAME)
            .phone(DEFAULT_PHONE);
        // Add required entity
        CostCenter costCenter;
        if (TestUtil.findAll(em, CostCenter.class).isEmpty()) {
            costCenter = CostCenterResourceIT.createEntity(em);
            em.persist(costCenter);
            em.flush();
        } else {
            costCenter = TestUtil.findAll(em, CostCenter.class).get(0);
        }
        projectMaster.setCostCenter(costCenter);
        return projectMaster;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProjectMaster createUpdatedEntity(EntityManager em) {
        ProjectMaster projectMaster = new ProjectMaster()
            .projectMasterCode(UPDATED_PROJECT_MASTER_CODE)
            .projectMasterName(UPDATED_PROJECT_MASTER_NAME)
            .costCenterType(UPDATED_COST_CENTER_TYPE)
            .projectDirectorName(UPDATED_PROJECT_DIRECTOR_NAME)
            .phone(UPDATED_PHONE);
        // Add required entity
        CostCenter costCenter;
        if (TestUtil.findAll(em, CostCenter.class).isEmpty()) {
            costCenter = CostCenterResourceIT.createUpdatedEntity(em);
            em.persist(costCenter);
            em.flush();
        } else {
            costCenter = TestUtil.findAll(em, CostCenter.class).get(0);
        }
        projectMaster.setCostCenter(costCenter);
        return projectMaster;
    }

    @BeforeEach
    public void initTest() {
        projectMaster = createEntity(em);
    }

    @Test
    @Transactional
    void createProjectMaster() throws Exception {
        int databaseSizeBeforeCreate = projectMasterRepository.findAll().size();
        // Create the ProjectMaster
        restProjectMasterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectMaster)))
            .andExpect(status().isCreated());

        // Validate the ProjectMaster in the database
        List<ProjectMaster> projectMasterList = projectMasterRepository.findAll();
        assertThat(projectMasterList).hasSize(databaseSizeBeforeCreate + 1);
        ProjectMaster testProjectMaster = projectMasterList.get(projectMasterList.size() - 1);
        assertThat(testProjectMaster.getProjectMasterCode()).isEqualTo(DEFAULT_PROJECT_MASTER_CODE);
        assertThat(testProjectMaster.getProjectMasterName()).isEqualTo(DEFAULT_PROJECT_MASTER_NAME);
        assertThat(testProjectMaster.getCostCenterType()).isEqualTo(DEFAULT_COST_CENTER_TYPE);
        assertThat(testProjectMaster.getProjectDirectorName()).isEqualTo(DEFAULT_PROJECT_DIRECTOR_NAME);
        assertThat(testProjectMaster.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void createProjectMasterWithExistingId() throws Exception {
        // Create the ProjectMaster with an existing ID
        projectMaster.setId(1L);

        int databaseSizeBeforeCreate = projectMasterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectMasterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectMaster)))
            .andExpect(status().isBadRequest());

        // Validate the ProjectMaster in the database
        List<ProjectMaster> projectMasterList = projectMasterRepository.findAll();
        assertThat(projectMasterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProjectMasterCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectMasterRepository.findAll().size();
        // set the field null
        projectMaster.setProjectMasterCode(null);

        // Create the ProjectMaster, which fails.

        restProjectMasterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectMaster)))
            .andExpect(status().isBadRequest());

        List<ProjectMaster> projectMasterList = projectMasterRepository.findAll();
        assertThat(projectMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProjectMasterNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectMasterRepository.findAll().size();
        // set the field null
        projectMaster.setProjectMasterName(null);

        // Create the ProjectMaster, which fails.

        restProjectMasterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectMaster)))
            .andExpect(status().isBadRequest());

        List<ProjectMaster> projectMasterList = projectMasterRepository.findAll();
        assertThat(projectMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCostCenterTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectMasterRepository.findAll().size();
        // set the field null
        projectMaster.setCostCenterType(null);

        // Create the ProjectMaster, which fails.

        restProjectMasterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectMaster)))
            .andExpect(status().isBadRequest());

        List<ProjectMaster> projectMasterList = projectMasterRepository.findAll();
        assertThat(projectMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProjectDirectorNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectMasterRepository.findAll().size();
        // set the field null
        projectMaster.setProjectDirectorName(null);

        // Create the ProjectMaster, which fails.

        restProjectMasterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectMaster)))
            .andExpect(status().isBadRequest());

        List<ProjectMaster> projectMasterList = projectMasterRepository.findAll();
        assertThat(projectMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectMasterRepository.findAll().size();
        // set the field null
        projectMaster.setPhone(null);

        // Create the ProjectMaster, which fails.

        restProjectMasterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectMaster)))
            .andExpect(status().isBadRequest());

        List<ProjectMaster> projectMasterList = projectMasterRepository.findAll();
        assertThat(projectMasterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProjectMasters() throws Exception {
        // Initialize the database
        projectMasterRepository.saveAndFlush(projectMaster);

        // Get all the projectMasterList
        restProjectMasterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectMaster.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectMasterCode").value(hasItem(DEFAULT_PROJECT_MASTER_CODE)))
            .andExpect(jsonPath("$.[*].projectMasterName").value(hasItem(DEFAULT_PROJECT_MASTER_NAME)))
            .andExpect(jsonPath("$.[*].costCenterType").value(hasItem(DEFAULT_COST_CENTER_TYPE)))
            .andExpect(jsonPath("$.[*].projectDirectorName").value(hasItem(DEFAULT_PROJECT_DIRECTOR_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProjectMastersWithEagerRelationshipsIsEnabled() throws Exception {
        when(projectMasterRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProjectMasterMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(projectMasterRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProjectMastersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(projectMasterRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProjectMasterMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(projectMasterRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getProjectMaster() throws Exception {
        // Initialize the database
        projectMasterRepository.saveAndFlush(projectMaster);

        // Get the projectMaster
        restProjectMasterMockMvc
            .perform(get(ENTITY_API_URL_ID, projectMaster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(projectMaster.getId().intValue()))
            .andExpect(jsonPath("$.projectMasterCode").value(DEFAULT_PROJECT_MASTER_CODE))
            .andExpect(jsonPath("$.projectMasterName").value(DEFAULT_PROJECT_MASTER_NAME))
            .andExpect(jsonPath("$.costCenterType").value(DEFAULT_COST_CENTER_TYPE))
            .andExpect(jsonPath("$.projectDirectorName").value(DEFAULT_PROJECT_DIRECTOR_NAME))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE));
    }

    @Test
    @Transactional
    void getNonExistingProjectMaster() throws Exception {
        // Get the projectMaster
        restProjectMasterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProjectMaster() throws Exception {
        // Initialize the database
        projectMasterRepository.saveAndFlush(projectMaster);

        int databaseSizeBeforeUpdate = projectMasterRepository.findAll().size();

        // Update the projectMaster
        ProjectMaster updatedProjectMaster = projectMasterRepository.findById(projectMaster.getId()).get();
        // Disconnect from session so that the updates on updatedProjectMaster are not directly saved in db
        em.detach(updatedProjectMaster);
        updatedProjectMaster
            .projectMasterCode(UPDATED_PROJECT_MASTER_CODE)
            .projectMasterName(UPDATED_PROJECT_MASTER_NAME)
            .costCenterType(UPDATED_COST_CENTER_TYPE)
            .projectDirectorName(UPDATED_PROJECT_DIRECTOR_NAME)
            .phone(UPDATED_PHONE);

        restProjectMasterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProjectMaster.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProjectMaster))
            )
            .andExpect(status().isOk());

        // Validate the ProjectMaster in the database
        List<ProjectMaster> projectMasterList = projectMasterRepository.findAll();
        assertThat(projectMasterList).hasSize(databaseSizeBeforeUpdate);
        ProjectMaster testProjectMaster = projectMasterList.get(projectMasterList.size() - 1);
        assertThat(testProjectMaster.getProjectMasterCode()).isEqualTo(UPDATED_PROJECT_MASTER_CODE);
        assertThat(testProjectMaster.getProjectMasterName()).isEqualTo(UPDATED_PROJECT_MASTER_NAME);
        assertThat(testProjectMaster.getCostCenterType()).isEqualTo(UPDATED_COST_CENTER_TYPE);
        assertThat(testProjectMaster.getProjectDirectorName()).isEqualTo(UPDATED_PROJECT_DIRECTOR_NAME);
        assertThat(testProjectMaster.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void putNonExistingProjectMaster() throws Exception {
        int databaseSizeBeforeUpdate = projectMasterRepository.findAll().size();
        projectMaster.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectMasterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, projectMaster.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projectMaster))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectMaster in the database
        List<ProjectMaster> projectMasterList = projectMasterRepository.findAll();
        assertThat(projectMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProjectMaster() throws Exception {
        int databaseSizeBeforeUpdate = projectMasterRepository.findAll().size();
        projectMaster.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMasterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projectMaster))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectMaster in the database
        List<ProjectMaster> projectMasterList = projectMasterRepository.findAll();
        assertThat(projectMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProjectMaster() throws Exception {
        int databaseSizeBeforeUpdate = projectMasterRepository.findAll().size();
        projectMaster.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMasterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projectMaster)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProjectMaster in the database
        List<ProjectMaster> projectMasterList = projectMasterRepository.findAll();
        assertThat(projectMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProjectMasterWithPatch() throws Exception {
        // Initialize the database
        projectMasterRepository.saveAndFlush(projectMaster);

        int databaseSizeBeforeUpdate = projectMasterRepository.findAll().size();

        // Update the projectMaster using partial update
        ProjectMaster partialUpdatedProjectMaster = new ProjectMaster();
        partialUpdatedProjectMaster.setId(projectMaster.getId());

        partialUpdatedProjectMaster
            .projectMasterCode(UPDATED_PROJECT_MASTER_CODE)
            .projectMasterName(UPDATED_PROJECT_MASTER_NAME)
            .costCenterType(UPDATED_COST_CENTER_TYPE)
            .projectDirectorName(UPDATED_PROJECT_DIRECTOR_NAME)
            .phone(UPDATED_PHONE);

        restProjectMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjectMaster.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjectMaster))
            )
            .andExpect(status().isOk());

        // Validate the ProjectMaster in the database
        List<ProjectMaster> projectMasterList = projectMasterRepository.findAll();
        assertThat(projectMasterList).hasSize(databaseSizeBeforeUpdate);
        ProjectMaster testProjectMaster = projectMasterList.get(projectMasterList.size() - 1);
        assertThat(testProjectMaster.getProjectMasterCode()).isEqualTo(UPDATED_PROJECT_MASTER_CODE);
        assertThat(testProjectMaster.getProjectMasterName()).isEqualTo(UPDATED_PROJECT_MASTER_NAME);
        assertThat(testProjectMaster.getCostCenterType()).isEqualTo(UPDATED_COST_CENTER_TYPE);
        assertThat(testProjectMaster.getProjectDirectorName()).isEqualTo(UPDATED_PROJECT_DIRECTOR_NAME);
        assertThat(testProjectMaster.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void fullUpdateProjectMasterWithPatch() throws Exception {
        // Initialize the database
        projectMasterRepository.saveAndFlush(projectMaster);

        int databaseSizeBeforeUpdate = projectMasterRepository.findAll().size();

        // Update the projectMaster using partial update
        ProjectMaster partialUpdatedProjectMaster = new ProjectMaster();
        partialUpdatedProjectMaster.setId(projectMaster.getId());

        partialUpdatedProjectMaster
            .projectMasterCode(UPDATED_PROJECT_MASTER_CODE)
            .projectMasterName(UPDATED_PROJECT_MASTER_NAME)
            .costCenterType(UPDATED_COST_CENTER_TYPE)
            .projectDirectorName(UPDATED_PROJECT_DIRECTOR_NAME)
            .phone(UPDATED_PHONE);

        restProjectMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjectMaster.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjectMaster))
            )
            .andExpect(status().isOk());

        // Validate the ProjectMaster in the database
        List<ProjectMaster> projectMasterList = projectMasterRepository.findAll();
        assertThat(projectMasterList).hasSize(databaseSizeBeforeUpdate);
        ProjectMaster testProjectMaster = projectMasterList.get(projectMasterList.size() - 1);
        assertThat(testProjectMaster.getProjectMasterCode()).isEqualTo(UPDATED_PROJECT_MASTER_CODE);
        assertThat(testProjectMaster.getProjectMasterName()).isEqualTo(UPDATED_PROJECT_MASTER_NAME);
        assertThat(testProjectMaster.getCostCenterType()).isEqualTo(UPDATED_COST_CENTER_TYPE);
        assertThat(testProjectMaster.getProjectDirectorName()).isEqualTo(UPDATED_PROJECT_DIRECTOR_NAME);
        assertThat(testProjectMaster.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    void patchNonExistingProjectMaster() throws Exception {
        int databaseSizeBeforeUpdate = projectMasterRepository.findAll().size();
        projectMaster.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, projectMaster.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projectMaster))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectMaster in the database
        List<ProjectMaster> projectMasterList = projectMasterRepository.findAll();
        assertThat(projectMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProjectMaster() throws Exception {
        int databaseSizeBeforeUpdate = projectMasterRepository.findAll().size();
        projectMaster.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projectMaster))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProjectMaster in the database
        List<ProjectMaster> projectMasterList = projectMasterRepository.findAll();
        assertThat(projectMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProjectMaster() throws Exception {
        int databaseSizeBeforeUpdate = projectMasterRepository.findAll().size();
        projectMaster.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjectMasterMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(projectMaster))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProjectMaster in the database
        List<ProjectMaster> projectMasterList = projectMasterRepository.findAll();
        assertThat(projectMasterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProjectMaster() throws Exception {
        // Initialize the database
        projectMasterRepository.saveAndFlush(projectMaster);

        int databaseSizeBeforeDelete = projectMasterRepository.findAll().size();

        // Delete the projectMaster
        restProjectMasterMockMvc
            .perform(delete(ENTITY_API_URL_ID, projectMaster.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProjectMaster> projectMasterList = projectMasterRepository.findAll();
        assertThat(projectMasterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
