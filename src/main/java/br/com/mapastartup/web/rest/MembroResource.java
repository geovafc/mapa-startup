package br.com.mapastartup.web.rest;

import br.com.mapastartup.repository.MembroRepository;
import br.com.mapastartup.service.MembroService;
import br.com.mapastartup.service.dto.MembroDTO;
import br.com.mapastartup.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.mapastartup.domain.Membro}.
 */
@RestController
@RequestMapping("/api")
public class MembroResource {

    private final Logger log = LoggerFactory.getLogger(MembroResource.class);

    private static final String ENTITY_NAME = "membro";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MembroService membroService;

    private final MembroRepository membroRepository;

    public MembroResource(MembroService membroService, MembroRepository membroRepository) {
        this.membroService = membroService;
        this.membroRepository = membroRepository;
    }

    /**
     * {@code POST  /membros} : Create a new membro.
     *
     * @param membroDTO the membroDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new membroDTO, or with status {@code 400 (Bad Request)} if the membro has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/membros")
    public ResponseEntity<MembroDTO> createMembro(@RequestBody MembroDTO membroDTO) throws URISyntaxException {
        log.debug("REST request to save Membro : {}", membroDTO);
        if (membroDTO.getId() != null) {
            throw new BadRequestAlertException("A new membro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MembroDTO result = membroService.save(membroDTO);
        return ResponseEntity
            .created(new URI("/api/membros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /membros/:id} : Updates an existing membro.
     *
     * @param id the id of the membroDTO to save.
     * @param membroDTO the membroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated membroDTO,
     * or with status {@code 400 (Bad Request)} if the membroDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the membroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/membros/{id}")
    public ResponseEntity<MembroDTO> updateMembro(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MembroDTO membroDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Membro : {}, {}", id, membroDTO);
        if (membroDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, membroDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!membroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MembroDTO result = membroService.save(membroDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, membroDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /membros/:id} : Partial updates given fields of an existing membro, field will ignore if it is null
     *
     * @param id the id of the membroDTO to save.
     * @param membroDTO the membroDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated membroDTO,
     * or with status {@code 400 (Bad Request)} if the membroDTO is not valid,
     * or with status {@code 404 (Not Found)} if the membroDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the membroDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/membros/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MembroDTO> partialUpdateMembro(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MembroDTO membroDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Membro partially : {}, {}", id, membroDTO);
        if (membroDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, membroDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!membroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MembroDTO> result = membroService.partialUpdate(membroDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, membroDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /membros} : get all the membros.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of membros in body.
     */
    @GetMapping("/membros")
    public List<MembroDTO> getAllMembros() {
        log.debug("REST request to get all Membros");
        return membroService.findAll();
    }

    /**
     * {@code GET  /membros/:id} : get the "id" membro.
     *
     * @param id the id of the membroDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the membroDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/membros/{id}")
    public ResponseEntity<MembroDTO> getMembro(@PathVariable Long id) {
        log.debug("REST request to get Membro : {}", id);
        Optional<MembroDTO> membroDTO = membroService.findOne(id);
        return ResponseUtil.wrapOrNotFound(membroDTO);
    }

    /**
     * {@code DELETE  /membros/:id} : delete the "id" membro.
     *
     * @param id the id of the membroDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/membros/{id}")
    public ResponseEntity<Void> deleteMembro(@PathVariable Long id) {
        log.debug("REST request to delete Membro : {}", id);
        membroService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
