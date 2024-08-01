package com.ilyap.productivityservice.mapper;

import com.ilyap.productivityservice.model.dto.ProductivityReadDto;
import com.ilyap.productivityservice.model.entity.Productivity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductivityReadMapper {

    Productivity toEntity(ProductivityReadDto productivityReadDto);

    ProductivityReadDto toDto(Productivity productivity);
}