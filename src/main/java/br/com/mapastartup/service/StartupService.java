package br.com.mapastartup.service;

import br.com.mapastartup.service.dto.StartupDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link br.com.mapastartup.domain.Startup}.
 */
public interface StartupService {
    /**
     * Save a startup.
     *
     * @param startupDTO the entity to save.
     * @return the persisted entity.
     */
    StartupDTO save(StartupDTO startupDTO);

    /**
     * Partially updates a startup.
     *
     * @param startupDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StartupDTO> partialUpdate(StartupDTO startupDTO);

    /**
     * Get all the startups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StartupDTO> findAll(Pageable pageable);

    /**
     * Get the "id" startup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StartupDTO> findOne(Long id);

    /**
     * Delete the "id" startup.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
