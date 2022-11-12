package co.edu.sena.web.rest;

import co.edu.sena.domain.OperatorType;
import co.edu.sena.repository.OperatorTypeRepository;
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
 * REST controller for managing {@link co.edu.sena.domain.OperatorType}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OperatorTypeResource {

    private final Logger log = LoggerFactory.getLogger(OperatorTypeResource.class);

    private static final String ENTITY_NAME = "operatorType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OperatorTypeRepository operatorTypeRepository;

    public OperatorTypeResource(OperatorTypeRepository operatorTypeRepository) {
        this.operatorTypeRepository = operatorTypeRepository;
    }

    /**
     * {@code POST  /operator-types} : Create a new operatorType.
     *
     * @param operatorType the operatorType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new operatorType, or with status {@code 400 (Bad Request)} if the operatorType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/operator-types")
    public ResponseEntity<OperatorType> createOperatorType(@Valid @RequestBody OperatorType operatorType) throws URISyntaxException {
        log.debug("REST request to save OperatorType : {}", operatorType);
        if (operatorType.getId() != null) {
            throw new BadRequestAlertException("A new operatorType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OperatorType result = operatorTypeRepository.save(operatorType);
        return ResponseEntity
            .created(new URI("/api/operator-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /operator-types/:id} : Updates an existing operatorType.
     *
     * @param id the id of the operatorType to save.
     * @param operatorType the operatorType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated operatorType,
     * or with status {@code 400 (Bad Request)} if the operatorType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the operatorType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/operator-types/{id}")
    public ResponseEntity<OperatorType> updateOperatorType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OperatorType operatorType
    ) throws URISyntaxException {
        log.debug("REST request to update OperatorType : {}, {}", id, operatorType);
        if (operatorType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, operatorType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!operatorTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OperatorType result = operatorTypeRepository.save(operatorType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, operatorType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /operator-types/:id} : Partial updates given fields of an existing operatorType, field will ignore if it is null
     *
     * @param id the id of the operatorType to save.
     * @param operatorType the operatorType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated operatorType,
     * or with status {@code 400 (Bad Request)} if the operatorType is not valid,
     * or with status {@code 404 (Not Found)} if the operatorType is not found,
     * or with status {@code 500 (Internal Server Error)} if the operatorType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/operator-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OperatorType> partialUpdateOperatorType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OperatorType operatorType
    ) throws URISyntaxException {
        log.debug("REST request to partial update OperatorType partially : {}, {}", id, operatorType);
        if (operatorType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, operatorType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!operatorTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OperatorType> result = operatorTypeRepository
            .findById(operatorType.getId())
            .map(existingOperatorType -> {
                if (operatorType.getCode() != null) {
                    existingOperatorType.setCode(operatorType.getCode());
                }
                if (operatorType.getDescription() != null) {
                    existingOperatorType.setDescription(operatorType.getDescription());
                }

                return existingOperatorType;
            })
            .map(operatorTypeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, operatorType.getId().toString())
        );
    }

    /**
     * {@code GET  /operator-types} : get all the operatorTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of operatorTypes in body.
     */
    @GetMapping("/operator-types")
    public List<OperatorType> getAllOperatorTypes() {
        log.debug("REST request to get all OperatorTypes");
        return operatorTypeRepository.findAll();
    }

    /**
     * {@code GET  /operator-types/:id} : get the "id" operatorType.
     *
     * @param id the id of the operatorType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the operatorType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/operator-types/{id}")
    public ResponseEntity<OperatorType> getOperatorType(@PathVariable Long id) {
        log.debug("REST request to get OperatorType : {}", id);
        Optional<OperatorType> operatorType = operatorTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(operatorType);
    }

    /**
     * {@code DELETE  /operator-types/:id} : delete the "id" operatorType.
     *
     * @param id the id of the operatorType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/operator-types/{id}")
    public ResponseEntity<Void> deleteOperatorType(@PathVariable Long id) {
        log.debug("REST request to delete OperatorType : {}", id);
        operatorTypeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
