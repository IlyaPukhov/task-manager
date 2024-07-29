package com.ilyap.authservice.mapper;

import com.ilyap.authservice.dto.RegistrationDto;
import com.ilyap.authservice.dto.RegistrationRequest;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RegistrationMapper {

    RegistrationRequest toRequest(RegistrationDto registrationDto);

    @Mapping(source = "firstname", target = "firstName")
    @Mapping(source = "lastname", target = "lastName")
    UserRepresentation toUserRepresentation(RegistrationDto registrationDto);
}
