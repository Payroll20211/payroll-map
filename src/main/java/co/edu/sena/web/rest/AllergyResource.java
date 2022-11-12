package co.edu.sena.web.rest;

import co.edu.sena.domain.Allergy;
import co.edu.sena.repository.AllergyRepository;
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
 * REST controller for managing {@link co.edu.sena.domain.Allergy}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AllergyResource {

    private final Logger log = LoggerFactory.getLogger(AllergyResource.class);

    private static final String ENTITY_NAME = "allergy";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AllergyRepository allergyRepository;

    public AllergyResource(AllergyRepository allergyRepository) {
        this.allergyRepository = allergyRepository;
    }

    /**
     * {@code POST  /allergies} : Create a new allergy.
     *
     * @param allergy the allergy to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new allergy, or with status {@code 400 (Bad Request)} if the allergy has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/allergies")
    public ResponseEntity<Allergy> createAllergy(@Valid @RequestBody Allergy allergy) throws URISyntaxException {
        log.debug("REST request to save Allergy : {}", allergy);
        if (allergy.getId() != null) {
            throw new BadRequestAlertException("A new allergy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Allergy result = allergyRepository.save(allergy);
        return ResponseEntity
            .created(new URI("/api/allergies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /allergies/:id} : Updates an existing allergy.
     *
     * @param id the id of the allergy to save.
     * @param allergy the allergy to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated allergy,
     * or with status {@code 400 (Bad Request)} if the allergy is not valid,
     * or with status {@code 500 (Internal Server Error)} if the allergy couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/allergies/{id}")
    public ResponseEntity<Allergy> updateAllergy(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Allergy allergy
    ) throws URISyntaxException {
        log.debug("REST request to update Allergy : {}, {}", id, allergy);
        if (allergy.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, allergy.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!allergyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Allergy result = allergyRepository.save(allergy);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, allergy.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /allergies/:id} : Partial updates given fields of an existing allergy, field will ignore if it is null
     *
     * @param id the id of the allergy to save.
     * @param allergy the allergy to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated allergy,
     * or with status {@code 400 (Bad Request)} if the allergy is not valid,
     * or with status {@code 404 (Not Found)} if the allergy is not found,
     * or with status {@code 500 (Internal Server Error)} if the allergy couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/allergies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Allergy> partialUpdateAllergy(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Allergy allergy
    ) throws URISyntaxException {
        log.debug("REST request to partial update Allergy partially : {}, {}", id, allergy);
        if (allergy.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, allergy.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!allergyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Allergy> result = allergyRepository
            .findById(allergy.getId())
            .map(existingAllergy -> {
                if (allergy.getDescription() != null) {
                    existingAllergy.setDescription(allergy.getDescription());
                }
                if (allergy.getTreatment() != null) {
                    existingAllergy.setTreatment(allergy.getTreatment());
                }

                return existingAllergy;
            })
            .map(allergyRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, allergy.getId().toString())
        );
    }

    /**
     * {@code GET  /allergies} : get all the allergies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of allergies in body.
     */
    @GetMapping("/allergies")
    public List<Allergy> getAllAllergies() {
        log.debug("REST request to get all Allergies");
        return allergyRepository.findAll();
    }

    /**
     * {@code GET  /allergies/:id} : get the "id" allergy.
     *
     * @param id the id of the allergy to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the allergy, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/allergies/{id}")
    public ResponseEntity<Allergy> getAllergy(@PathVariable Long id) {
        log.debug("REST request to get Allergy : {}", id);
        Optional<Allergy> allergy = allergyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(allergy);
    }

    /**
     * {@code DELETE  /allergies/:id} : delete the "id" allergy.
     *
     * @param id the id of the allergy to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/allergies/{id}")
    public ResponseEntity<Void> deleteAllergy(@PathVariable Long id) {
        log.debug("REST request to delete Allergy : {}", id);
        allergyRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
