package io.jistol.github.jpademo.entity.valuetype.embedded;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@Entity
public class PhoneNumberProvider {
    @Id
    private String name;
}
