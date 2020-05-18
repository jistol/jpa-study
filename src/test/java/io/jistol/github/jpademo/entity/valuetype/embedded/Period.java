package io.jistol.github.jpademo.entity.valuetype.embedded;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDate;

@Data
@Embeddable
@NoArgsConstructor
public class Period {
    private LocalDate startDate;
    private LocalDate endDate;
}
