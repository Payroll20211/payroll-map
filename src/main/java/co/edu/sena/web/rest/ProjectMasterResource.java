package co.edu.sena.web.rest;

import co.edu.sena.domain.ProjectMaster;
import co.edu.sena.repository.ProjectMasterRepository;
import co.edu.sena.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link co.edu.sena.domain.ProjectMaster}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProjectMasterResource {

    private final Logger log = LoggerFactory.getLogger(ProjectMasterResource.class);

    private static final String ENTITY_NAME = "projectMaster";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProjectMasterRepository projectMasterRepository;

    public ProjectMasterResource(ProjectMasterRepository projectMasterRepository) {
        this.projectMasterRepository = projectMasterRepository;
    }

    /**
     * {@code POST  /project-masters} : Create a new projectMaster.
     *
     * @param projectMaster the projectMaster to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new projectMaster, or with status {@code 400 (Bad Request)} if the projectMaster has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/project-masters")
    public ResponseEntity<ProjectMaster> createProjectMaster(@Valid @RequestBody ProjectMaster projectMaster) throws URISyntaxException {
        log.debug("REST request to save ProjectMaster : {}", projectMaster);
        if (projectMaster.getId() != null) {
            throw new BadRequestAlertException("A new projectMaster cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProjectMaster result = projectMasterRepository.save(projectMaster);
        return ResponseEntity
            .created(new URI("/api/project-masters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /project-masters/:id} : Updates an existing projectMaster.
     *
     * @param id the id of the projectMaster to save.
     * @param projectMaster the projectMaster to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projectMaster,
     * or with status {@code 400 (Bad Request)} if the projectMaster is not valid,
     * or with status {@code 500 (Internal Server Error)} if the projectMaster couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/project-masters/{id}")
    public ResponseEntity<ProjectMaster> updateProjectMaster(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProjectMaster projectMaster
    ) throws URISyntaxException {
        log.debug("REST request to update ProjectMaster : {}, {}", id, projectMaster);
        if (projectMaster.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, projectMaster.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!projectMasterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProjectMaster result = projectMasterRepository.save(projectMaster);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projectMaster.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /project-masters/:id} : Partial updates given fields of an existing projectMaster, field will ignore if it is null
     *
     * @param id the id of the projectMaster to save.
     * @param projectMaster the projectMaster to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated projectMaster,
     * or with status {@code 400 (Bad Request)} if the projectMaster is not valid,
     * or with status {@code 404 (Not Found)} if the projectMaster is not found,
     * or with status {@code 500 (Internal Server Error)} if the projectMaster couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/project-masters/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProjectMaster> partialUpdateProjectMaster(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProjectMaster projectMaster
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProjectMaster partially : {}, {}", id, projectMaster);
        if (projectMaster.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, projectMaster.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!projectMasterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProjectMaster> result = projectMasterRepository
            .findById(projectMaster.getId())
            .map(existingProjectMaster -> {
                if (projectMaster.getProjectMasterCode() != null) {
                    existingProjectMaster.setProjectMasterCode(projectMaster.getProjectMasterCode());
                }
                if (projectMaster.getProjectMasterName() != null) {
                    existingProjectMaster.setProjectMasterName(projectMaster.getProjectMasterName());
                }
                if (projectMaster.getCostCenterType() != null) {
                    existingProjectMaster.setCostCenterType(projectMaster.getCostCenterType());
                }
                if (projectMaster.getProjectDirectorName() != null) {
                    existingProjectMaster.setProjectDirectorName(projectMaster.getProjectDirectorName());
                }
                if (projectMaster.getPhone() != null) {
                    existingProjectMaster.setPhone(projectMaster.getPhone());
                }

                return existingProjectMaster;
            })
            .map(projectMasterRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projectMaster.getId().toString())
        );
    }

    /**
     * {@code GET  /project-masters} : get all the projectMasters.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of projectMasters in body.
     */
    @GetMapping("/project-masters")
    public List<ProjectMaster> getAllProjectMasters(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all ProjectMasters");
        return projectMasterRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /project-masters/:id} : get the "id" projectMaster.
     *
     * @param id the id of the projectMaster to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the projectMaster, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/project-masters/{id}")
    public ResponseEntity<ProjectMaster> getProjectMaster(@PathVariable Long id) {
        log.debug("REST request to get ProjectMaster : {}", id);
        Optional<ProjectMaster> projectMaster = projectMasterRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(projectMaster);
    }

    /**
     * {@code DELETE  /project-masters/:id} : delete the "id" projectMaster.
     *
     * @param id the id of the projectMaster to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/project-masters/{id}")
    public ResponseEntity<Void> deleteProjectMaster(@PathVariable Long id) {
        log.debug("REST request to delete ProjectMaster : {}", id);
        projectMasterRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
