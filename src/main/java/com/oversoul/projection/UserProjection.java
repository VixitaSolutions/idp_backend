package com.oversoul.projection;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.UUID;

public interface UserProjection {
    public Long getId();

    public String getEmail();

    public String getFirstName();

    public String getLastName();

    public String getUserName();

    public Boolean getSignUpDone();

    public Date getCreatedOn();

    public String getMobile();

    public UUID getTenantId();

    public Boolean getActive();

    public String getClientName();


}
