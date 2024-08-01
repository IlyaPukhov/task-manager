package com.ilyap.productivityservice.mapper;

import com.ilyap.productivityservice.model.dto.ProductivityCreateUpdateDto;
import com.ilyap.productivityservice.model.entity.Productivity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductivityCreateUpdateMapper {

    Productivity toEntity(ProductivityCreateUpdateDto productivityCreateUpdateDto);

    @Mapping(target = "id", ignore = true)
    Productivity toEntity(ProductivityCreateUpdateDto productivityCreateUpdateDto,
                          @MappingTarget Productivity productivity);

    ProductivityCreateUpdateDto toDto(Productivity productivity);
}
