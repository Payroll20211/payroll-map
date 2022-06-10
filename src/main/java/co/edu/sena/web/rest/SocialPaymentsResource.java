package co.edu.sena.web.rest;

import co.edu.sena.domain.SocialPayments;
import co.edu.sena.repository.SocialPaymentsRepository;
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
 * REST controller for managing {@link co.edu.sena.domain.SocialPayments}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SocialPaymentsResource {

    private final Logger log = LoggerFactory.getLogger(SocialPaymentsResource.class);

    private static final String ENTITY_NAME = "socialPayments";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SocialPaymentsRepository socialPaymentsRepository;

    public SocialPaymentsResource(SocialPaymentsRepository socialPaymentsRepository) {
        this.socialPaymentsRepository = socialPaymentsRepository;
    }

    /**
     * {@code POST  /social-payments} : Create a new socialPayments.
     *
     * @param socialPayments the socialPayments to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new socialPayments, or with status {@code 400 (Bad Request)} if the socialPayments has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/social-payments")
    public ResponseEntity<SocialPayments> createSocialPayments(@Valid @RequestBody SocialPayments socialPayments)
        throws URISyntaxException {
        log.debug("REST request to save SocialPayments : {}", socialPayments);
        if (socialPayments.getId() != null) {
            throw new BadRequestAlertException("A new socialPayments cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SocialPayments result = socialPaymentsRepository.save(socialPayments);
        return ResponseEntity
            .created(new URI("/api/social-payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /social-payments/:id} : Updates an existing socialPayments.
     *
     * @param id the id of the socialPayments to save.
     * @param socialPayments the socialPayments to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated socialPayments,
     * or with status {@code 400 (Bad Request)} if the socialPayments is not valid,
     * or with status {@code 500 (Internal Server Error)} if the socialPayments couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/social-payments/{id}")
    public ResponseEntity<SocialPayments> updateSocialPayments(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SocialPayments socialPayments
    ) throws URISyntaxException {
        log.debug("REST request to update SocialPayments : {}, {}", id, socialPayments);
        if (socialPayments.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, socialPayments.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!socialPaymentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SocialPayments result = socialPaymentsRepository.save(socialPayments);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, socialPayments.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /social-payments/:id} : Partial updates given fields of an existing socialPayments, field will ignore if it is null
     *
     * @param id the id of the socialPayments to save.
     * @param socialPayments the socialPayments to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated socialPayments,
     * or with status {@code 400 (Bad Request)} if the socialPayments is not valid,
     * or with status {@code 404 (Not Found)} if the socialPayments is not found,
     * or with status {@code 500 (Internal Server Error)} if the socialPayments couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/social-payments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SocialPayments> partialUpdateSocialPayments(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SocialPayments socialPayments
    ) throws URISyntaxException {
        log.debug("REST request to partial update SocialPayments partially : {}, {}", id, socialPayments);
        if (socialPayments.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, socialPayments.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!socialPaymentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SocialPayments> result = socialPaymentsRepository
            .findById(socialPayments.getId())
            .map(existingSocialPayments -> {
                if (socialPayments.getCode() != null) {
                    existingSocialPayments.setCode(socialPayments.getCode());
                }
                if (socialPayments.getDescription() != null) {
                    existingSocialPayments.setDescription(socialPayments.getDescription());
                }

                return existingSocialPayments;
            })
            .map(socialPaymentsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, socialPayments.getId().toString())
        );
    }

    /**
     * {@code GET  /social-payments} : get all the socialPayments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of socialPayments in body.
     */
    @GetMapping("/social-payments")
    public List<SocialPayments> getAllSocialPayments() {
        log.debug("REST request to get all SocialPayments");
        return socialPaymentsRepository.findAll();
    }

    /**
     * {@code GET  /social-payments/:id} : get the "id" socialPayments.
     *
     * @param id the id of the socialPayments to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the socialPayments, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/social-payments/{id}")
    public ResponseEntity<SocialPayments> getSocialPayments(@PathVariable Long id) {
        log.debug("REST request to get SocialPayments : {}", id);
        Optional<SocialPayments> socialPayments = socialPaymentsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(socialPayments);
    }

    /**
     * {@code DELETE  /social-payments/:id} : delete the "id" socialPayments.
     *
     * @param id the id of the socialPayments to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/social-payments/{id}")
    public ResponseEntity<Void> deleteSocialPayments(@PathVariable Long id) {
        log.debug("REST request to delete SocialPayments : {}", id);
        socialPaymentsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
