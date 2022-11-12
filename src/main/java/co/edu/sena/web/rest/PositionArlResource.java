package co.edu.sena.web.rest;

import co.edu.sena.domain.PositionArl;
import co.edu.sena.repository.PositionArlRepository;
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
 * REST controller for managing {@link co.edu.sena.domain.PositionArl}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PositionArlResource {

    private final Logger log = LoggerFactory.getLogger(PositionArlResource.class);

    private static final String ENTITY_NAME = "positionArl";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PositionArlRepository positionArlRepository;

    public PositionArlResource(PositionArlRepository positionArlRepository) {
        this.positionArlRepository = positionArlRepository;
    }

    /**
     * {@code POST  /position-arls} : Create a new positionArl.
     *
     * @param positionArl the positionArl to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new positionArl, or with status {@code 400 (Bad Request)} if the positionArl has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/position-arls")
    public ResponseEntity<PositionArl> createPositionArl(@Valid @RequestBody PositionArl positionArl) throws URISyntaxException {
        log.debug("REST request to save PositionArl : {}", positionArl);
        if (positionArl.getId() != null) {
            throw new BadRequestAlertException("A new positionArl cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PositionArl result = positionArlRepository.save(positionArl);
        return ResponseEntity
            .created(new URI("/api/position-arls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /position-arls/:id} : Updates an existing positionArl.
     *
     * @param id the id of the positionArl to save.
     * @param positionArl the positionArl to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated positionArl,
     * or with status {@code 400 (Bad Request)} if the positionArl is not valid,
     * or with status {@code 500 (Internal Server Error)} if the positionArl couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/position-arls/{id}")
    public ResponseEntity<PositionArl> updatePositionArl(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PositionArl positionArl
    ) throws URISyntaxException {
        log.debug("REST request to update PositionArl : {}, {}", id, positionArl);
        if (positionArl.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, positionArl.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!positionArlRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PositionArl result = positionArlRepository.save(positionArl);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, positionArl.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /position-arls/:id} : Partial updates given fields of an existing positionArl, field will ignore if it is null
     *
     * @param id the id of the positionArl to save.
     * @param positionArl the positionArl to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated positionArl,
     * or with status {@code 400 (Bad Request)} if the positionArl is not valid,
     * or with status {@code 404 (Not Found)} if the positionArl is not found,
     * or with status {@code 500 (Internal Server Error)} if the positionArl couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/position-arls/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PositionArl> partialUpdatePositionArl(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PositionArl positionArl
    ) throws URISyntaxException {
        log.debug("REST request to partial update PositionArl partially : {}, {}", id, positionArl);
        if (positionArl.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, positionArl.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!positionArlRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PositionArl> result = positionArlRepository
            .findById(positionArl.getId())
            .map(existingPositionArl -> {
                if (positionArl.getRiskClass() != null) {
                    existingPositionArl.setRiskClass(positionArl.getRiskClass());
                }
                if (positionArl.getPositionCode() != null) {
                    existingPositionArl.setPositionCode(positionArl.getPositionCode());
                }
                if (positionArl.getPosition() != null) {
                    existingPositionArl.setPosition(positionArl.getPosition());
                }

                return existingPositionArl;
            })
            .map(positionArlRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, positionArl.getId().toString())
        );
    }

    /**
     * {@code GET  /position-arls} : get all the positionArls.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of positionArls in body.
     */
    @GetMapping("/position-arls")
    public List<PositionArl> getAllPositionArls() {
        log.debug("REST request to get all PositionArls");
        return positionArlRepository.findAll();
    }

    /**
     * {@code GET  /position-arls/:id} : get the "id" positionArl.
     *
     * @param id the id of the positionArl to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the positionArl, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/position-arls/{id}")
    public ResponseEntity<PositionArl> getPositionArl(@PathVariable Long id) {
        log.debug("REST request to get PositionArl : {}", id);
        Optional<PositionArl> positionArl = positionArlRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(positionArl);
    }

    /**
     * {@code DELETE  /position-arls/:id} : delete the "id" positionArl.
     *
     * @param id the id of the positionArl to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/position-arls/{id}")
    public ResponseEntity<Void> deletePositionArl(@PathVariable Long id) {
        log.debug("REST request to delete PositionArl : {}", id);
        positionArlRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
