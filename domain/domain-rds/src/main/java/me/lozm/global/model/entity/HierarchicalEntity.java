package me.lozm.global.model.entity;

import lombok.Getter;
import me.lozm.global.model.HierarchyResponseAble;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
public class HierarchicalEntity implements HierarchyResponseAble {

    @Column(name = "COMMON_PARENT_ID")
    private Long commonParentId;

    @Column(name = "PARENT_ID")
    private Long parentId;

    @Column(name = "GROUP_ORDER")
    private Integer groupOrder;

    @Column(name = "GROUP_LAYER")
    private Integer groupLayer;


    public void update(Long id) {
        commonParentId = id;
        parentId = id;
        groupOrder = 0;
        groupLayer = 0;
    }

    public void update(Long commonParentId, Long parentId, Integer groupLayer, Integer groupOrder) {
        this.commonParentId = commonParentId;
        this.parentId = parentId;
        this.groupLayer = groupLayer;
        this.groupOrder = groupOrder;
    }

    public void increaseGroupOrder() {
        this.groupOrder += 1;
    }

}
