package io.jistol.github.jpademo.entity.management.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@NoArgsConstructor
@Embeddable
public class Address {
    private String zipcode;
    private String city;
    private String street;
}
