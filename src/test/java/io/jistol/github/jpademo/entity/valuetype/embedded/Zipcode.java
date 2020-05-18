package io.jistol.github.jpademo.entity.valuetype.embedded;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@NoArgsConstructor
@Embeddable
public class Zipcode {
    private String zip;
    private String plusFour;
}
