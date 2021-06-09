package br.com.mapastartup.service.mapper;

import br.com.mapastartup.domain.*;
import br.com.mapastartup.service.dto.StartupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Startup} and its DTO {@link StartupDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StartupMapper extends EntityMapper<StartupDTO, Startup> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StartupDTO toDtoId(Startup startup);
}
