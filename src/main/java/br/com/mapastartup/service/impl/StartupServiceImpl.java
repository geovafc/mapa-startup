package br.com.mapastartup.service.impl;

import br.com.mapastartup.domain.Startup;
import br.com.mapastartup.repository.StartupRepository;
import br.com.mapastartup.service.StartupService;
import br.com.mapastartup.service.dto.StartupDTO;
import br.com.mapastartup.service.mapper.StartupMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Startup}.
 */
@Service
@Transactional
public class StartupServiceImpl implements StartupService {

    private final Logger log = LoggerFactory.getLogger(StartupServiceImpl.class);

    private final StartupRepository startupRepository;

    private final StartupMapper startupMapper;

    public StartupServiceImpl(StartupRepository startupRepository, StartupMapper startupMapper) {
        this.startupRepository = startupRepository;
        this.startupMapper = startupMapper;
    }

    @Override
    public StartupDTO save(StartupDTO startupDTO) {
        log.debug("Request to save Startup : {}", startupDTO);
        Startup startup = startupMapper.toEntity(startupDTO);
        startup = startupRepository.save(startup);
        return startupMapper.toDto(startup);
    }

    @Override
    public Optional<StartupDTO> partialUpdate(StartupDTO startupDTO) {
        log.debug("Request to partially update Startup : {}", startupDTO);

        return startupRepository
            .findById(startupDTO.getId())
            .map(
                existingStartup -> {
                    startupMapper.partialUpdate(existingStartup, startupDTO);
                    return existingStartup;
                }
            )
            .map(startupRepository::save)
            .map(startupMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StartupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Startups");
        return startupRepository.findAll(pageable).map(startupMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StartupDTO> findOne(Long id) {
        log.debug("Request to get Startup : {}", id);
        return startupRepository.findById(id).map(startupMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Startup : {}", id);
        startupRepository.deleteById(id);
    }
}
