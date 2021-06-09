package br.com.mapastartup.repository;

import br.com.mapastartup.domain.Membro;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Membro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MembroRepository extends JpaRepository<Membro, Long> {}
