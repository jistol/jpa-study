package io.jistol.github.jpademo.entity.manytomany.idclass;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class TriMemberProductId implements Serializable {
    private String member;
    private String product;
}
