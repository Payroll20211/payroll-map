package co.edu.sena.web.rest;

import co.edu.sena.domain.Deduction;
import co.edu.sena.repository.DeductionRepository;
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
 * REST controller for managing {@link co.edu.sena.domain.Deduction}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DeductionResource {

    private final Logger log = LoggerFactory.getLogger(DeductionResource.class);

    private static final String ENTITY_NAME = "deduction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeductionRepository deductionRepository;

    public DeductionResource(DeductionRepository deductionRepository) {
        this.deductionRepository = deductionRepository;
    }

    /**
     * {@code POST  /deductions} : Create a new deduction.
     *
     * @param deduction the deduction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deduction, or with status {@code 400 (Bad Request)} if the deduction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/deductions")
    public ResponseEntity<Deduction> createDeduction(@Valid @RequestBody Deduction deduction) throws URISyntaxException {
        log.debug("REST request to save Deduction : {}", deduction);
        if (deduction.getId() != null) {
            throw new BadRequestAlertException("A new deduction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Deduction result = deductionRepository.save(deduction);
        return ResponseEntity
            .created(new URI("/api/deductions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /deductions/:id} : Updates an existing deduction.
     *
     * @param id the id of the deduction to save.
     * @param deduction the deduction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deduction,
     * or with status {@code 400 (Bad Request)} if the deduction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deduction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/deductions/{id}")
    public ResponseEntity<Deduction> updateDeduction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Deduction deduction
    ) throws URISyntaxException {
        log.debug("REST request to update Deduction : {}, {}", id, deduction);
        if (deduction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deduction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deductionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Deduction result = deductionRepository.save(deduction);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deduction.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /deductions/:id} : Partial updates given fields of an existing deduction, field will ignore if it is null
     *
     * @param id the id of the deduction to save.
     * @param deduction the deduction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deduction,
     * or with status {@code 400 (Bad Request)} if the deduction is not valid,
     * or with status {@code 404 (Not Found)} if the deduction is not found,
     * or with status {@code 500 (Internal Server Error)} if the deduction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/deductions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Deduction> partialUpdateDeduction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Deduction deduction
    ) throws URISyntaxException {
        log.debug("REST request to partial update Deduction partially : {}, {}", id, deduction);
        if (deduction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deduction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deductionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Deduction> result = deductionRepository
            .findById(deduction.getId())
            .map(existingDeduction -> {
                if (deduction.getDeductionCode() != null) {
                    existingDeduction.setDeductionCode(deduction.getDeductionCode());
                }
                if (deduction.getDescription() != null) {
                    existingDeduction.setDescription(deduction.getDescription());
                }

                return existingDeduction;
            })
            .map(deductionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deduction.getId().toString())
        );
    }

    /**
     * {@code GET  /deductions} : get all the deductions.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deductions in body.
     */
    @GetMapping("/deductions")
    public List<Deduction> getAllDeductions(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Deductions");
        return deductionRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /deductions/:id} : get the "id" deduction.
     *
     * @param id the id of the deduction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deduction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/deductions/{id}")
    public ResponseEntity<Deduction> getDeduction(@PathVariable Long id) {
        log.debug("REST request to get Deduction : {}", id);
        Optional<Deduction> deduction = deductionRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(deduction);
    }

    /**
     * {@code DELETE  /deductions/:id} : delete the "id" deduction.
     *
     * @param id the id of the deduction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/deductions/{id}")
    public ResponseEntity<Void> deleteDeduction(@PathVariable Long id) {
        log.debug("REST request to delete Deduction : {}", id);
        deductionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
