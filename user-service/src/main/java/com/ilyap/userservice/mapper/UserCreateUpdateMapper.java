package com.ilyap.userservice.mapper;

import com.ilyap.userservice.model.dto.UserCreateUpdateDto;
import com.ilyap.userservice.model.entity.TaskManagerUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserCreateUpdateMapper {

    TaskManagerUser toEntity(UserCreateUpdateDto createUpdateDto);

    @Mapping(target = "id", ignore = true)
    TaskManagerUser toEntity(UserCreateUpdateDto createUpdateDto, @MappingTarget TaskManagerUser user);

    UserCreateUpdateDto toDto(TaskManagerUser user);
}
