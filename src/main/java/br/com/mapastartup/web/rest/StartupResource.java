package br.com.mapastartup.web.rest;

import br.com.mapastartup.repository.StartupRepository;
import br.com.mapastartup.service.StartupService;
import br.com.mapastartup.service.dto.StartupDTO;
import br.com.mapastartup.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.mapastartup.domain.Startup}.
 */
@RestController
@RequestMapping("/api")
public class StartupResource {

    private final Logger log = LoggerFactory.getLogger(StartupResource.class);

    private static final String ENTITY_NAME = "startup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StartupService startupService;

    private final StartupRepository startupRepository;

    public StartupResource(StartupService startupService, StartupRepository startupRepository) {
        this.startupService = startupService;
        this.startupRepository = startupRepository;
    }

    /**
     * {@code POST  /startups} : Create a new startup.
     *
     * @param startupDTO the startupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new startupDTO, or with status {@code 400 (Bad Request)} if the startup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/startups")
    public ResponseEntity<StartupDTO> createStartup(@RequestBody StartupDTO startupDTO) throws URISyntaxException {
        log.debug("REST request to save Startup : {}", startupDTO);
        if (startupDTO.getId() != null) {
            throw new BadRequestAlertException("A new startup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StartupDTO result = startupService.save(startupDTO);
        return ResponseEntity
            .created(new URI("/api/startups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /startups/:id} : Updates an existing startup.
     *
     * @param id the id of the startupDTO to save.
     * @param startupDTO the startupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated startupDTO,
     * or with status {@code 400 (Bad Request)} if the startupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the startupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/startups/{id}")
    public ResponseEntity<StartupDTO> updateStartup(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StartupDTO startupDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Startup : {}, {}", id, startupDTO);
        if (startupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, startupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!startupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StartupDTO result = startupService.save(startupDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, startupDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /startups/:id} : Partial updates given fields of an existing startup, field will ignore if it is null
     *
     * @param id the id of the startupDTO to save.
     * @param startupDTO the startupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated startupDTO,
     * or with status {@code 400 (Bad Request)} if the startupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the startupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the startupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/startups/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<StartupDTO> partialUpdateStartup(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StartupDTO startupDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Startup partially : {}, {}", id, startupDTO);
        if (startupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, startupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!startupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StartupDTO> result = startupService.partialUpdate(startupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, startupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /startups} : get all the startups.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of startups in body.
     */
    @GetMapping("/startups")
    public ResponseEntity<List<StartupDTO>> getAllStartups(Pageable pageable) {
        log.debug("REST request to get a page of Startups");
        Page<StartupDTO> page = startupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /startups/:id} : get the "id" startup.
     *
     * @param id the id of the startupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the startupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/startups/{id}")
    public ResponseEntity<StartupDTO> getStartup(@PathVariable Long id) {
        log.debug("REST request to get Startup : {}", id);
        Optional<StartupDTO> startupDTO = startupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(startupDTO);
    }

    /**
     * {@code DELETE  /startups/:id} : delete the "id" startup.
     *
     * @param id the id of the startupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/startups/{id}")
    public ResponseEntity<Void> deleteStartup(@PathVariable Long id) {
        log.debug("REST request to delete Startup : {}", id);
        startupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
