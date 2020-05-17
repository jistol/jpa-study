package io.jistol.github.jpademo.entity.advancemapping.mappedsuperclass;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class User extends BaseEntity {
    private String username;
}
