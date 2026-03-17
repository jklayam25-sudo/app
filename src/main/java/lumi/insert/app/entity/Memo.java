package lumi.insert.app.entity;

import java.util.List;

import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lumi.insert.app.entity.nondatabase.BaseAuditing;
import lumi.insert.app.entity.nondatabase.EmployeeRole;

@Entity(name = "memos")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Audited
public class Memo extends BaseAuditing{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 55)
    private String title;

    @Column(nullable = false)
    private String body;

    @Column(nullable = true)
    private List<String> images;

    @Builder.Default
    private Boolean isActive = true;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EmployeeRole role = null;

}
