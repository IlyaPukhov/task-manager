package com.ilyap.taskservice.mapper;

import org.mapstruct.MappingTarget;

public interface CreateUpdateMapper<D, E> {

    E map(D dto);

    E map(D dto, @MappingTarget E entity);
}
