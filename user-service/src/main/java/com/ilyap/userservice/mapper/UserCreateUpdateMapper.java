package com.ilyap.userservice.mapper;

import com.ilyap.userservice.model.dto.UserCreateUpdateDto;
import com.ilyap.userservice.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserCreateUpdateMapper {

    User toEntity(UserCreateUpdateDto createUpdateDto);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserCreateUpdateDto createUpdateDto, @MappingTarget User user);

    UserCreateUpdateDto toDto(User user);
}
