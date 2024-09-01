package com.ilyap.productivityservice.model.dto;

import com.ilyap.productivityservice.model.entity.ActivityType;
import com.ilyap.productivityservice.model.entity.ProductivityStatus;
import com.ilyap.validationstarter.annotation.ValueOfEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Map;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProductivityCreateUpdateDto {

    @NotEmpty
    @NotBlank
    private String username;

    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Range(min = 1, max = 10)
    private Integer mood;

    @ValueOfEnum(ProductivityStatus.class)
    private String productivityStatus;

    private Map<ActivityType, Boolean> checklist;

    @Size(max = 512)
    private String notes;
}
