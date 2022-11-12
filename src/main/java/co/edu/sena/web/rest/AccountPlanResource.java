package co.edu.sena.web.rest;

import co.edu.sena.domain.AccountPlan;
import co.edu.sena.repository.AccountPlanRepository;
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
 * REST controller for managing {@link co.edu.sena.domain.AccountPlan}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AccountPlanResource {

    private final Logger log = LoggerFactory.getLogger(AccountPlanResource.class);

    private static final String ENTITY_NAME = "accountPlan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountPlanRepository accountPlanRepository;

    public AccountPlanResource(AccountPlanRepository accountPlanRepository) {
        this.accountPlanRepository = accountPlanRepository;
    }

    /**
     * {@code POST  /account-plans} : Create a new accountPlan.
     *
     * @param accountPlan the accountPlan to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountPlan, or with status {@code 400 (Bad Request)} if the accountPlan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/account-plans")
    public ResponseEntity<AccountPlan> createAccountPlan(@Valid @RequestBody AccountPlan accountPlan) throws URISyntaxException {
        log.debug("REST request to save AccountPlan : {}", accountPlan);
        if (accountPlan.getId() != null) {
            throw new BadRequestAlertException("A new accountPlan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountPlan result = accountPlanRepository.save(accountPlan);
        return ResponseEntity
            .created(new URI("/api/account-plans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /account-plans/:id} : Updates an existing accountPlan.
     *
     * @param id the id of the accountPlan to save.
     * @param accountPlan the accountPlan to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountPlan,
     * or with status {@code 400 (Bad Request)} if the accountPlan is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountPlan couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/account-plans/{id}")
    public ResponseEntity<AccountPlan> updateAccountPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AccountPlan accountPlan
    ) throws URISyntaxException {
        log.debug("REST request to update AccountPlan : {}, {}", id, accountPlan);
        if (accountPlan.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountPlan.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AccountPlan result = accountPlanRepository.save(accountPlan);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountPlan.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /account-plans/:id} : Partial updates given fields of an existing accountPlan, field will ignore if it is null
     *
     * @param id the id of the accountPlan to save.
     * @param accountPlan the accountPlan to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountPlan,
     * or with status {@code 400 (Bad Request)} if the accountPlan is not valid,
     * or with status {@code 404 (Not Found)} if the accountPlan is not found,
     * or with status {@code 500 (Internal Server Error)} if the accountPlan couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/account-plans/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccountPlan> partialUpdateAccountPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AccountPlan accountPlan
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccountPlan partially : {}, {}", id, accountPlan);
        if (accountPlan.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountPlan.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccountPlan> result = accountPlanRepository
            .findById(accountPlan.getId())
            .map(existingAccountPlan -> {
                if (accountPlan.getCode() != null) {
                    existingAccountPlan.setCode(accountPlan.getCode());
                }
                if (accountPlan.getDescription() != null) {
                    existingAccountPlan.setDescription(accountPlan.getDescription());
                }

                return existingAccountPlan;
            })
            .map(accountPlanRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountPlan.getId().toString())
        );
    }

    /**
     * {@code GET  /account-plans} : get all the accountPlans.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountPlans in body.
     */
    @GetMapping("/account-plans")
    public List<AccountPlan> getAllAccountPlans() {
        log.debug("REST request to get all AccountPlans");
        return accountPlanRepository.findAll();
    }

    /**
     * {@code GET  /account-plans/:id} : get the "id" accountPlan.
     *
     * @param id the id of the accountPlan to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountPlan, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/account-plans/{id}")
    public ResponseEntity<AccountPlan> getAccountPlan(@PathVariable Long id) {
        log.debug("REST request to get AccountPlan : {}", id);
        Optional<AccountPlan> accountPlan = accountPlanRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(accountPlan);
    }

    /**
     * {@code DELETE  /account-plans/:id} : delete the "id" accountPlan.
     *
     * @param id the id of the accountPlan to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/account-plans/{id}")
    public ResponseEntity<Void> deleteAccountPlan(@PathVariable Long id) {
        log.debug("REST request to delete AccountPlan : {}", id);
        accountPlanRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
