package com.ilyap.productivityservice.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.util.ProxyUtils;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@Document
public class Productivity extends AuditingEntity {

    @Id
    private String id;

    private String userId;

    private Instant date;

    private Integer mood;

    private ProductivityStatus productivityStatus;

    private Map<ActivityType, Boolean> checklist;

    private String notes;

    {
        for (ActivityType activity : ActivityType.values()) {
            checklist.put(activity, false);
        }
    }

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
