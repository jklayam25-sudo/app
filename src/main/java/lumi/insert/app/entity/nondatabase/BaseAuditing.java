package lumi.insert.app.entity.nondatabase;

import java.time.LocalDateTime;

import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseAuditing {

    @CreatedBy
    @NotAudited
    private String createdBy;

    @LastModifiedBy
    @NotAudited
    private String updatedBy;

    @CreatedDate
    @NotAudited
    private LocalDateTime createdAt;

    @LastModifiedDate
    @NotAudited
    private LocalDateTime updatedAt;

    @Version
    @NotAudited
    private Long version = 0L;

}
