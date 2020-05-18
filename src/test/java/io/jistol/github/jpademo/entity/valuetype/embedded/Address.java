package io.jistol.github.jpademo.entity.valuetype.embedded;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Data
@NoArgsConstructor
@Embeddable
public class Address {
    @Column(name = "city")
    private String city;
    private String street;
    @Embedded
    private Zipcode zipcode;
}
