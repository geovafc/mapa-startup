package br.com.mapastartup.service;

import br.com.mapastartup.domain.Membro;
import br.com.mapastartup.repository.MembroRepository;
import br.com.mapastartup.service.dto.MembroDTO;
import br.com.mapastartup.service.mapper.MembroMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Membro}.
 */
@Service
@Transactional
public class MembroService {

    private final Logger log = LoggerFactory.getLogger(MembroService.class);

    private final MembroRepository membroRepository;

    private final MembroMapper membroMapper;

    public MembroService(MembroRepository membroRepository, MembroMapper membroMapper) {
        this.membroRepository = membroRepository;
        this.membroMapper = membroMapper;
    }

    /**
     * Save a membro.
     *
     * @param membroDTO the entity to save.
     * @return the persisted entity.
     */
    public MembroDTO save(MembroDTO membroDTO) {
        log.debug("Request to save Membro : {}", membroDTO);
        Membro membro = membroMapper.toEntity(membroDTO);
        membro = membroRepository.save(membro);
        return membroMapper.toDto(membro);
    }

    /**
     * Partially update a membro.
     *
     * @param membroDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MembroDTO> partialUpdate(MembroDTO membroDTO) {
        log.debug("Request to partially update Membro : {}", membroDTO);

        return membroRepository
            .findById(membroDTO.getId())
            .map(
                existingMembro -> {
                    membroMapper.partialUpdate(existingMembro, membroDTO);
                    return existingMembro;
                }
            )
            .map(membroRepository::save)
            .map(membroMapper::toDto);
    }

    /**
     * Get all the membros.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MembroDTO> findAll() {
        log.debug("Request to get all Membros");
        return membroRepository.findAll().stream().map(membroMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one membro by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MembroDTO> findOne(Long id) {
        log.debug("Request to get Membro : {}", id);
        return membroRepository.findById(id).map(membroMapper::toDto);
    }

    /**
     * Delete the membro by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Membro : {}", id);
        membroRepository.deleteById(id);
    }
}
