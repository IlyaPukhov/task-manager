package com.ilyap.productivityservice.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.util.ProxyUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@CompoundIndex(def = "{'username': 1, 'date': 1}", unique = true)
@Document
public class Productivity extends AuditingEntity implements Serializable {

    @Id
    private UUID id;

    private String username;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;

    private Integer mood;

    @JsonProperty("productivity_status")
    private ProductivityStatus productivityStatus;

    private Map<ActivityType, Boolean> checklist;

    private String notes;

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || ProxyUtils.getUserClass(this) != ProxyUtils.getUserClass(object))
            return false;
        Productivity that = (Productivity) object;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return ProxyUtils.getUserClass(this).hashCode();
    }
}
