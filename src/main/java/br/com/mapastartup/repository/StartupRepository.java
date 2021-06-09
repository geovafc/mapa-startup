package br.com.mapastartup.repository;

import br.com.mapastartup.domain.Startup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Startup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StartupRepository extends JpaRepository<Startup, Long> {}
