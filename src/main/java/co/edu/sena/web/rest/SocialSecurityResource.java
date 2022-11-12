package co.edu.sena.web.rest;

import co.edu.sena.domain.SocialSecurity;
import co.edu.sena.repository.SocialSecurityRepository;
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
 * REST controller for managing {@link co.edu.sena.domain.SocialSecurity}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SocialSecurityResource {

    private final Logger log = LoggerFactory.getLogger(SocialSecurityResource.class);

    private static final String ENTITY_NAME = "socialSecurity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SocialSecurityRepository socialSecurityRepository;

    public SocialSecurityResource(SocialSecurityRepository socialSecurityRepository) {
        this.socialSecurityRepository = socialSecurityRepository;
    }

    /**
     * {@code POST  /social-securities} : Create a new socialSecurity.
     *
     * @param socialSecurity the socialSecurity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new socialSecurity, or with status {@code 400 (Bad Request)} if the socialSecurity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/social-securities")
    public ResponseEntity<SocialSecurity> createSocialSecurity(@Valid @RequestBody SocialSecurity socialSecurity)
        throws URISyntaxException {
        log.debug("REST request to save SocialSecurity : {}", socialSecurity);
        if (socialSecurity.getId() != null) {
            throw new BadRequestAlertException("A new socialSecurity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SocialSecurity result = socialSecurityRepository.save(socialSecurity);
        return ResponseEntity
            .created(new URI("/api/social-securities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /social-securities/:id} : Updates an existing socialSecurity.
     *
     * @param id the id of the socialSecurity to save.
     * @param socialSecurity the socialSecurity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated socialSecurity,
     * or with status {@code 400 (Bad Request)} if the socialSecurity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the socialSecurity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/social-securities/{id}")
    public ResponseEntity<SocialSecurity> updateSocialSecurity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SocialSecurity socialSecurity
    ) throws URISyntaxException {
        log.debug("REST request to update SocialSecurity : {}, {}", id, socialSecurity);
        if (socialSecurity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, socialSecurity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!socialSecurityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SocialSecurity result = socialSecurityRepository.save(socialSecurity);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, socialSecurity.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /social-securities/:id} : Partial updates given fields of an existing socialSecurity, field will ignore if it is null
     *
     * @param id the id of the socialSecurity to save.
     * @param socialSecurity the socialSecurity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated socialSecurity,
     * or with status {@code 400 (Bad Request)} if the socialSecurity is not valid,
     * or with status {@code 404 (Not Found)} if the socialSecurity is not found,
     * or with status {@code 500 (Internal Server Error)} if the socialSecurity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/social-securities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SocialSecurity> partialUpdateSocialSecurity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SocialSecurity socialSecurity
    ) throws URISyntaxException {
        log.debug("REST request to partial update SocialSecurity partially : {}, {}", id, socialSecurity);
        if (socialSecurity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, socialSecurity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!socialSecurityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SocialSecurity> result = socialSecurityRepository
            .findById(socialSecurity.getId())
            .map(existingSocialSecurity -> {
                if (socialSecurity.getEps() != null) {
                    existingSocialSecurity.setEps(socialSecurity.getEps());
                }
                if (socialSecurity.getAfp() != null) {
                    existingSocialSecurity.setAfp(socialSecurity.getAfp());
                }

                return existingSocialSecurity;
            })
            .map(socialSecurityRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, socialSecurity.getId().toString())
        );
    }

    /**
     * {@code GET  /social-securities} : get all the socialSecurities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of socialSecurities in body.
     */
    @GetMapping("/social-securities")
    public List<SocialSecurity> getAllSocialSecurities() {
        log.debug("REST request to get all SocialSecurities");
        return socialSecurityRepository.findAll();
    }

    /**
     * {@code GET  /social-securities/:id} : get the "id" socialSecurity.
     *
     * @param id the id of the socialSecurity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the socialSecurity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/social-securities/{id}")
    public ResponseEntity<SocialSecurity> getSocialSecurity(@PathVariable Long id) {
        log.debug("REST request to get SocialSecurity : {}", id);
        Optional<SocialSecurity> socialSecurity = socialSecurityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(socialSecurity);
    }

    /**
     * {@code DELETE  /social-securities/:id} : delete the "id" socialSecurity.
     *
     * @param id the id of the socialSecurity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/social-securities/{id}")
    public ResponseEntity<Void> deleteSocialSecurity(@PathVariable Long id) {
        log.debug("REST request to delete SocialSecurity : {}", id);
        socialSecurityRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
