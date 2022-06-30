package me.lozm.global.model.entity;

import lombok.Getter;
import me.lozm.global.model.HierarchyAble;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
public class HierarchicalEntity implements HierarchyAble {

    @Column(name = "COMMON_PARENT_ID")
    private Long commonParentId;

    @Column(name = "PARENT_ID")
    private Long parentId;

    @Column(name = "GROUP_ORDER")
    private Integer groupOrder;

    @Column(name = "GROUP_LAYER")
    private Integer groupLayer;

}
