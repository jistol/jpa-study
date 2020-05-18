package io.jistol.github.jpademo.entity.valuetype.embedded;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@Entity
public class Member {
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    
    @Embedded
    private Period workPeriod;
    
    @Embedded
    private Address homeAddress;
    
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "COMPANY_CITY")),
            @AttributeOverride(name = "street", column = @Column(name = "COMPANY_STREET")),
            @AttributeOverride(name = "zipcode.zip", column = @Column(name = "COMPANY_ZIP")),
            @AttributeOverride(name = "zipcode.plusFour", column = @Column(name = "COMPANY_PLUSFOUR"))
    })
    private Address companyAddress;
    
    @Embedded
    private PhoneNumber phoneNumber;
}
