package co.edu.sena.web.rest;

import static co.edu.sena.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.edu.sena.IntegrationTest;
import co.edu.sena.domain.Allergy;
import co.edu.sena.domain.Contract;
import co.edu.sena.domain.Employee;
import co.edu.sena.domain.OperatorMatriz;
import co.edu.sena.domain.OperatorType;
import co.edu.sena.domain.Period;
import co.edu.sena.domain.PositionArl;
import co.edu.sena.domain.ProjectMaster;
import co.edu.sena.domain.SocialPayments;
import co.edu.sena.domain.SocialSecurity;
import co.edu.sena.domain.User;
import co.edu.sena.domain.enumeration.StateEmployee;
import co.edu.sena.repository.EmployeeRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link EmployeeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EmployeeResourceIT {

    private static final String DEFAULT_COMPLETE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPLETE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_START = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_START = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final Integer DEFAULT_MOBILE = 1;
    private static final Integer UPDATED_MOBILE = 2;

    private static final StateEmployee DEFAULT_STATE_EMPLOYEE = StateEmployee.ACTIVE;
    private static final StateEmployee UPDATED_STATE_EMPLOYEE = StateEmployee.INACTIVE;

    private static final String ENTITY_API_URL = "/api/employees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeRepository employeeRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeMockMvc;

    private Employee employee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createEntity(EntityManager em) {
        Employee employee = new Employee()
            .completeName(DEFAULT_COMPLETE_NAME)
            .address(DEFAULT_ADDRESS)
            .dateStart(DEFAULT_DATE_START)
            .city(DEFAULT_CITY)
            .mobile(DEFAULT_MOBILE)
            .stateEmployee(DEFAULT_STATE_EMPLOYEE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        employee.setUser(user);
        // Add required entity
        Contract contract;
        if (TestUtil.findAll(em, Contract.class).isEmpty()) {
            contract = ContractResourceIT.createEntity(em);
            em.persist(contract);
            em.flush();
        } else {
            contract = TestUtil.findAll(em, Contract.class).get(0);
        }
        employee.setContract(contract);
        // Add required entity
        Allergy allergy;
        if (TestUtil.findAll(em, Allergy.class).isEmpty()) {
            allergy = AllergyResourceIT.createEntity(em);
            em.persist(allergy);
            em.flush();
        } else {
            allergy = TestUtil.findAll(em, Allergy.class).get(0);
        }
        employee.setAllergy(allergy);
        // Add required entity
        SocialPayments socialPayments;
        if (TestUtil.findAll(em, SocialPayments.class).isEmpty()) {
            socialPayments = SocialPaymentsResourceIT.createEntity(em);
            em.persist(socialPayments);
            em.flush();
        } else {
            socialPayments = TestUtil.findAll(em, SocialPayments.class).get(0);
        }
        employee.setSocialPayments(socialPayments);
        // Add required entity
        PositionArl positionArl;
        if (TestUtil.findAll(em, PositionArl.class).isEmpty()) {
            positionArl = PositionArlResourceIT.createEntity(em);
            em.persist(positionArl);
            em.flush();
        } else {
            positionArl = TestUtil.findAll(em, PositionArl.class).get(0);
        }
        employee.setPositionArl(positionArl);
        // Add required entity
        Period period;
        if (TestUtil.findAll(em, Period.class).isEmpty()) {
            period = PeriodResourceIT.createEntity(em);
            em.persist(period);
            em.flush();
        } else {
            period = TestUtil.findAll(em, Period.class).get(0);
        }
        employee.setPeriod(period);
        // Add required entity
        OperatorType operatorType;
        if (TestUtil.findAll(em, OperatorType.class).isEmpty()) {
            operatorType = OperatorTypeResourceIT.createEntity(em);
            em.persist(operatorType);
            em.flush();
        } else {
            operatorType = TestUtil.findAll(em, OperatorType.class).get(0);
        }
        employee.setOperatorType(operatorType);
        // Add required entity
        OperatorMatriz operatorMatriz;
        if (TestUtil.findAll(em, OperatorMatriz.class).isEmpty()) {
            operatorMatriz = OperatorMatrizResourceIT.createEntity(em);
            em.persist(operatorMatriz);
            em.flush();
        } else {
            operatorMatriz = TestUtil.findAll(em, OperatorMatriz.class).get(0);
        }
        employee.setOperatorMatriz(operatorMatriz);
        // Add required entity
        SocialSecurity socialSecurity;
        if (TestUtil.findAll(em, SocialSecurity.class).isEmpty()) {
            socialSecurity = SocialSecurityResourceIT.createEntity(em);
            em.persist(socialSecurity);
            em.flush();
        } else {
            socialSecurity = TestUtil.findAll(em, SocialSecurity.class).get(0);
        }
        employee.setSocialSecurity(socialSecurity);
        // Add required entity
        ProjectMaster projectMaster;
        if (TestUtil.findAll(em, ProjectMaster.class).isEmpty()) {
            projectMaster = ProjectMasterResourceIT.createEntity(em);
            em.persist(projectMaster);
            em.flush();
        } else {
            projectMaster = TestUtil.findAll(em, ProjectMaster.class).get(0);
        }
        employee.getEmployees().add(projectMaster);
        return employee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createUpdatedEntity(EntityManager em) {
        Employee employee = new Employee()
            .completeName(UPDATED_COMPLETE_NAME)
            .address(UPDATED_ADDRESS)
            .dateStart(UPDATED_DATE_START)
            .city(UPDATED_CITY)
            .mobile(UPDATED_MOBILE)
            .stateEmployee(UPDATED_STATE_EMPLOYEE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        employee.setUser(user);
        // Add required entity
        Contract contract;
        if (TestUtil.findAll(em, Contract.class).isEmpty()) {
            contract = ContractResourceIT.createUpdatedEntity(em);
            em.persist(contract);
            em.flush();
        } else {
            contract = TestUtil.findAll(em, Contract.class).get(0);
        }
        employee.setContract(contract);
        // Add required entity
        Allergy allergy;
        if (TestUtil.findAll(em, Allergy.class).isEmpty()) {
            allergy = AllergyResourceIT.createUpdatedEntity(em);
            em.persist(allergy);
            em.flush();
        } else {
            allergy = TestUtil.findAll(em, Allergy.class).get(0);
        }
        employee.setAllergy(allergy);
        // Add required entity
        SocialPayments socialPayments;
        if (TestUtil.findAll(em, SocialPayments.class).isEmpty()) {
            socialPayments = SocialPaymentsResourceIT.createUpdatedEntity(em);
            em.persist(socialPayments);
            em.flush();
        } else {
            socialPayments = TestUtil.findAll(em, SocialPayments.class).get(0);
        }
        employee.setSocialPayments(socialPayments);
        // Add required entity
        PositionArl positionArl;
        if (TestUtil.findAll(em, PositionArl.class).isEmpty()) {
            positionArl = PositionArlResourceIT.createUpdatedEntity(em);
            em.persist(positionArl);
            em.flush();
        } else {
            positionArl = TestUtil.findAll(em, PositionArl.class).get(0);
        }
        employee.setPositionArl(positionArl);
        // Add required entity
        Period period;
        if (TestUtil.findAll(em, Period.class).isEmpty()) {
            period = PeriodResourceIT.createUpdatedEntity(em);
            em.persist(period);
            em.flush();
        } else {
            period = TestUtil.findAll(em, Period.class).get(0);
        }
        employee.setPeriod(period);
        // Add required entity
        OperatorType operatorType;
        if (TestUtil.findAll(em, OperatorType.class).isEmpty()) {
            operatorType = OperatorTypeResourceIT.createUpdatedEntity(em);
            em.persist(operatorType);
            em.flush();
        } else {
            operatorType = TestUtil.findAll(em, OperatorType.class).get(0);
        }
        employee.setOperatorType(operatorType);
        // Add required entity
        OperatorMatriz operatorMatriz;
        if (TestUtil.findAll(em, OperatorMatriz.class).isEmpty()) {
            operatorMatriz = OperatorMatrizResourceIT.createUpdatedEntity(em);
            em.persist(operatorMatriz);
            em.flush();
        } else {
            operatorMatriz = TestUtil.findAll(em, OperatorMatriz.class).get(0);
        }
        employee.setOperatorMatriz(operatorMatriz);
        // Add required entity
        SocialSecurity socialSecurity;
        if (TestUtil.findAll(em, SocialSecurity.class).isEmpty()) {
            socialSecurity = SocialSecurityResourceIT.createUpdatedEntity(em);
            em.persist(socialSecurity);
            em.flush();
        } else {
            socialSecurity = TestUtil.findAll(em, SocialSecurity.class).get(0);
        }
        employee.setSocialSecurity(socialSecurity);
        // Add required entity
        ProjectMaster projectMaster;
        if (TestUtil.findAll(em, ProjectMaster.class).isEmpty()) {
            projectMaster = ProjectMasterResourceIT.createUpdatedEntity(em);
            em.persist(projectMaster);
            em.flush();
        } else {
            projectMaster = TestUtil.findAll(em, ProjectMaster.class).get(0);
        }
        employee.getEmployees().add(projectMaster);
        return employee;
    }

    @BeforeEach
    public void initTest() {
        employee = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployee() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().size();
        // Create the Employee
        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isCreated());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate + 1);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getCompleteName()).isEqualTo(DEFAULT_COMPLETE_NAME);
        assertThat(testEmployee.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testEmployee.getDateStart()).isEqualTo(DEFAULT_DATE_START);
        assertThat(testEmployee.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testEmployee.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testEmployee.getStateEmployee()).isEqualTo(DEFAULT_STATE_EMPLOYEE);
    }

    @Test
    @Transactional
    void createEmployeeWithExistingId() throws Exception {
        // Create the Employee with an existing ID
        employee.setId(1L);

        int databaseSizeBeforeCreate = employeeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCompleteNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setCompleteName(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setAddress(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateStartIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setDateStart(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setCity(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMobileIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setMobile(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStateEmployeeIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setStateEmployee(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployees() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].completeName").value(hasItem(DEFAULT_COMPLETE_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].dateStart").value(hasItem(sameInstant(DEFAULT_DATE_START))))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].stateEmployee").value(hasItem(DEFAULT_STATE_EMPLOYEE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployeesWithEagerRelationshipsIsEnabled() throws Exception {
        when(employeeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmployeeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(employeeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployeesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(employeeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmployeeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(employeeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get the employee
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL_ID, employee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employee.getId().intValue()))
            .andExpect(jsonPath("$.completeName").value(DEFAULT_COMPLETE_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.dateStart").value(sameInstant(DEFAULT_DATE_START)))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE))
            .andExpect(jsonPath("$.stateEmployee").value(DEFAULT_STATE_EMPLOYEE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEmployee() throws Exception {
        // Get the employee
        restEmployeeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee
        Employee updatedEmployee = employeeRepository.findById(employee.getId()).get();
        // Disconnect from session so that the updates on updatedEmployee are not directly saved in db
        em.detach(updatedEmployee);
        updatedEmployee
            .completeName(UPDATED_COMPLETE_NAME)
            .address(UPDATED_ADDRESS)
            .dateStart(UPDATED_DATE_START)
            .city(UPDATED_CITY)
            .mobile(UPDATED_MOBILE)
            .stateEmployee(UPDATED_STATE_EMPLOYEE);

        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmployee.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getCompleteName()).isEqualTo(UPDATED_COMPLETE_NAME);
        assertThat(testEmployee.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testEmployee.getDateStart()).isEqualTo(UPDATED_DATE_START);
        assertThat(testEmployee.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testEmployee.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testEmployee.getStateEmployee()).isEqualTo(UPDATED_STATE_EMPLOYEE);
    }

    @Test
    @Transactional
    void putNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employee.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee.address(UPDATED_ADDRESS).dateStart(UPDATED_DATE_START);

        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getCompleteName()).isEqualTo(DEFAULT_COMPLETE_NAME);
        assertThat(testEmployee.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testEmployee.getDateStart()).isEqualTo(UPDATED_DATE_START);
        assertThat(testEmployee.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testEmployee.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testEmployee.getStateEmployee()).isEqualTo(DEFAULT_STATE_EMPLOYEE);
    }

    @Test
    @Transactional
    void fullUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee
            .completeName(UPDATED_COMPLETE_NAME)
            .address(UPDATED_ADDRESS)
            .dateStart(UPDATED_DATE_START)
            .city(UPDATED_CITY)
            .mobile(UPDATED_MOBILE)
            .stateEmployee(UPDATED_STATE_EMPLOYEE);

        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getCompleteName()).isEqualTo(UPDATED_COMPLETE_NAME);
        assertThat(testEmployee.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testEmployee.getDateStart()).isEqualTo(UPDATED_DATE_START);
        assertThat(testEmployee.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testEmployee.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testEmployee.getStateEmployee()).isEqualTo(UPDATED_STATE_EMPLOYEE);
    }

    @Test
    @Transactional
    void patchNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeDelete = employeeRepository.findAll().size();

        // Delete the employee
        restEmployeeMockMvc
            .perform(delete(ENTITY_API_URL_ID, employee.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
