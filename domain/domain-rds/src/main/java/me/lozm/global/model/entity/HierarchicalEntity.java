package me.lozm.global.model.entity;

import lombok.Getter;
import lombok.Setter;
import me.lozm.global.model.HierarchyAble;
import org.apache.commons.lang3.ObjectUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
public class HierarchicalEntity implements HierarchyAble {

    private Long commonParentId;

    private Long parentId;

    @Setter
    @Column(name = "GROUP_ORDER")
    private Integer groupOrder;

    @Column(name = "GROUP_LAYER")
    private Integer groupLayer;


    public static HierarchicalEntity createEntity() {
        HierarchicalEntity hierarchicalEntity = new HierarchicalEntity();
        hierarchicalEntity.groupOrder = 0;
        hierarchicalEntity.groupLayer = 0;
        return hierarchicalEntity;
    }

    public static HierarchicalEntity createEntity(Long commonParentId, Long parentId) {
        HierarchicalEntity hierarchicalEntity = new HierarchicalEntity();
        hierarchicalEntity.commonParentId = commonParentId;
        hierarchicalEntity.parentId = parentId;
        hierarchicalEntity.groupOrder = 0;
        hierarchicalEntity.groupLayer = 0;
        return hierarchicalEntity;
    }


    public void setDefaultParentId(Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new RuntimeException("계층 엔티티 ID 세팅에 실패하였습니다.");
        }

        this.commonParentId = id;
        this.parentId = id;
    }

    public void setReplyInfo(Integer parentGroupOrder, Integer parentGroupLayer) {
        this.groupOrder = parentGroupOrder + 1;
        this.groupLayer = parentGroupLayer + 1;
    }

}
