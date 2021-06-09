package br.com.mapastartup.service.mapper;

import br.com.mapastartup.domain.*;
import br.com.mapastartup.service.dto.MembroDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Membro} and its DTO {@link MembroDTO}.
 */
@Mapper(componentModel = "spring", uses = { StartupMapper.class })
public interface MembroMapper extends EntityMapper<MembroDTO, Membro> {
    @Mapping(target = "startup", source = "startup", qualifiedByName = "id")
    MembroDTO toDto(Membro s);
}
