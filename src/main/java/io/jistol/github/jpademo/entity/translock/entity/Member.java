package io.jistol.github.jpademo.entity.translock.entity;

import io.jistol.github.jpademo.entity.NameSupport;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@NamedQuery(
        name = "Member.findByNameWithLock",
        query = "SELECT m FROM Member m WHERE m.name = :name",
        lockMode = LockModeType.OPTIMISTIC
)
public class Member implements NameSupport {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Version
    private Long version;
}
