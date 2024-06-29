package com.ilyap.userservice.mapper;

import com.ilyap.userservice.model.dto.UserReadDto;
import com.ilyap.userservice.model.entity.TaskManagerUser;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserReadMapper {

    UserReadDto map(TaskManagerUser user);
}
