package io.jistol.github.jpademo.entity.advancemapping.idclass;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ParentId implements Serializable {
    private String id1;
    private String id2;
}
