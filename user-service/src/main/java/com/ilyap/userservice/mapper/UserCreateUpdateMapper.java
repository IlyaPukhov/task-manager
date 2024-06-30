package com.ilyap.userservice.mapper;

import com.ilyap.userservice.model.dto.UserCreateUpdateDto;
import com.ilyap.userservice.model.entity.TaskManagerUser;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserCreateUpdateMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public abstract TaskManagerUser map(UserCreateUpdateDto createUpdateDto);

    public abstract TaskManagerUser map(UserCreateUpdateDto createUpdateDto, @MappingTarget TaskManagerUser user);

    @AfterMapping
    protected void encodePassword(@MappingTarget TaskManagerUser user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }
}
