package co.edu.sena.web.rest;

import co.edu.sena.domain.OperatorMatriz;
import co.edu.sena.repository.OperatorMatrizRepository;
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
 * REST controller for managing {@link co.edu.sena.domain.OperatorMatriz}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OperatorMatrizResource {

    private final Logger log = LoggerFactory.getLogger(OperatorMatrizResource.class);

    private static final String ENTITY_NAME = "operatorMatriz";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OperatorMatrizRepository operatorMatrizRepository;

    public OperatorMatrizResource(OperatorMatrizRepository operatorMatrizRepository) {
        this.operatorMatrizRepository = operatorMatrizRepository;
    }

    /**
     * {@code POST  /operator-matrizs} : Create a new operatorMatriz.
     *
     * @param operatorMatriz the operatorMatriz to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new operatorMatriz, or with status {@code 400 (Bad Request)} if the operatorMatriz has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/operator-matrizs")
    public ResponseEntity<OperatorMatriz> createOperatorMatriz(@Valid @RequestBody OperatorMatriz operatorMatriz)
        throws URISyntaxException {
        log.debug("REST request to save OperatorMatriz : {}", operatorMatriz);
        if (operatorMatriz.getId() != null) {
            throw new BadRequestAlertException("A new operatorMatriz cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OperatorMatriz result = operatorMatrizRepository.save(operatorMatriz);
        return ResponseEntity
            .created(new URI("/api/operator-matrizs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /operator-matrizs/:id} : Updates an existing operatorMatriz.
     *
     * @param id the id of the operatorMatriz to save.
     * @param operatorMatriz the operatorMatriz to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated operatorMatriz,
     * or with status {@code 400 (Bad Request)} if the operatorMatriz is not valid,
     * or with status {@code 500 (Internal Server Error)} if the operatorMatriz couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/operator-matrizs/{id}")
    public ResponseEntity<OperatorMatriz> updateOperatorMatriz(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OperatorMatriz operatorMatriz
    ) throws URISyntaxException {
        log.debug("REST request to update OperatorMatriz : {}, {}", id, operatorMatriz);
        if (operatorMatriz.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, operatorMatriz.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!operatorMatrizRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OperatorMatriz result = operatorMatrizRepository.save(operatorMatriz);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, operatorMatriz.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /operator-matrizs/:id} : Partial updates given fields of an existing operatorMatriz, field will ignore if it is null
     *
     * @param id the id of the operatorMatriz to save.
     * @param operatorMatriz the operatorMatriz to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated operatorMatriz,
     * or with status {@code 400 (Bad Request)} if the operatorMatriz is not valid,
     * or with status {@code 404 (Not Found)} if the operatorMatriz is not found,
     * or with status {@code 500 (Internal Server Error)} if the operatorMatriz couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/operator-matrizs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OperatorMatriz> partialUpdateOperatorMatriz(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OperatorMatriz operatorMatriz
    ) throws URISyntaxException {
        log.debug("REST request to partial update OperatorMatriz partially : {}, {}", id, operatorMatriz);
        if (operatorMatriz.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, operatorMatriz.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!operatorMatrizRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OperatorMatriz> result = operatorMatrizRepository
            .findById(operatorMatriz.getId())
            .map(existingOperatorMatriz -> {
                if (operatorMatriz.getNumberid() != null) {
                    existingOperatorMatriz.setNumberid(operatorMatriz.getNumberid());
                }
                if (operatorMatriz.getDigitverification() != null) {
                    existingOperatorMatriz.setDigitverification(operatorMatriz.getDigitverification());
                }
                if (operatorMatriz.getName() != null) {
                    existingOperatorMatriz.setName(operatorMatriz.getName());
                }
                if (operatorMatriz.getAddress() != null) {
                    existingOperatorMatriz.setAddress(operatorMatriz.getAddress());
                }
                if (operatorMatriz.getCity() != null) {
                    existingOperatorMatriz.setCity(operatorMatriz.getCity());
                }
                if (operatorMatriz.getEmail() != null) {
                    existingOperatorMatriz.setEmail(operatorMatriz.getEmail());
                }

                return existingOperatorMatriz;
            })
            .map(operatorMatrizRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, operatorMatriz.getId().toString())
        );
    }

    /**
     * {@code GET  /operator-matrizs} : get all the operatorMatrizs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of operatorMatrizs in body.
     */
    @GetMapping("/operator-matrizs")
    public List<OperatorMatriz> getAllOperatorMatrizs() {
        log.debug("REST request to get all OperatorMatrizs");
        return operatorMatrizRepository.findAll();
    }

    /**
     * {@code GET  /operator-matrizs/:id} : get the "id" operatorMatriz.
     *
     * @param id the id of the operatorMatriz to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the operatorMatriz, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/operator-matrizs/{id}")
    public ResponseEntity<OperatorMatriz> getOperatorMatriz(@PathVariable Long id) {
        log.debug("REST request to get OperatorMatriz : {}", id);
        Optional<OperatorMatriz> operatorMatriz = operatorMatrizRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(operatorMatriz);
    }

    /**
     * {@code DELETE  /operator-matrizs/:id} : delete the "id" operatorMatriz.
     *
     * @param id the id of the operatorMatriz to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/operator-matrizs/{id}")
    public ResponseEntity<Void> deleteOperatorMatriz(@PathVariable Long id) {
        log.debug("REST request to delete OperatorMatriz : {}", id);
        operatorMatrizRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
