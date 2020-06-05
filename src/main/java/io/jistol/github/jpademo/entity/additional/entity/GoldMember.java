package io.jistol.github.jpademo.entity.additional.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@Entity
@NoArgsConstructor
@NamedEntityGraphs({
        @NamedEntityGraph(name = "GoldMember.withOrderInfos", attributeNodes = @NamedAttributeNode("orderInfos")),
        @NamedEntityGraph(name = "GoldMember.withAll",
                attributeNodes = @NamedAttributeNode(value = "orderInfos", subgraph = "items"),
                subgraphs = @NamedSubgraph(name = "items", attributeNodes = @NamedAttributeNode("items"))
        )
})
public class GoldMember implements MemberBase {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    private Company company;

    @OneToMany(mappedBy = "goldMember", fetch = FetchType.LAZY)
    private List<OrderInfo> orderInfos = new ArrayList<>();

    @PostPersist
    public void postPersist() {
        log.warn("id : {}, order size : {}", this.id, this.orderInfos.size());
    }
}
