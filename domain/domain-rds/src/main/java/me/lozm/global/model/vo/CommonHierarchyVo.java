package me.lozm.global.model.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.global.code.HierarchyType;
import me.lozm.global.model.HierarchyRequestAble;
import org.springframework.util.Assert;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonHierarchyVo {

    @Getter
    public static class Request implements HierarchyRequestAble {
        private final HierarchyType hierarchyType;
        private final Long parentId;

        public Request() {
            this.hierarchyType = HierarchyType.ORIGIN;
            this.parentId = null;
        }

        public Request(HierarchyType hierarchyType, Long parentId) {
            Assert.notNull(hierarchyType, "계층 유형은 null 일 수 없습니다.");
            Assert.notNull(parentId, "부모 ID 는 null 일 수 없습니다.");

            this.hierarchyType = hierarchyType;
            this.parentId = parentId;
        }
    }

}
