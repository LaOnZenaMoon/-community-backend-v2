package me.lozm.app.board.service;

import me.lozm.global.code.HierarchyType;
import me.lozm.global.model.HierarchyRequestAble;
import me.lozm.utils.exception.BadRequestException;
import me.lozm.utils.exception.CustomExceptionType;

import java.util.function.Function;

public abstract class HierarchyService<E> {

    public final <T extends HierarchyRequestAble, R> R createEntity(T request, Function<T, E> saveEntityFunction) {
        validateHook(request);

        E entity = saveEntity(request, saveEntityFunction);

        if (request.getHierarchyType() == HierarchyType.ORIGIN) {
            updateEntityWhenHierarchyTypeIsOrigin(entity, request);
        } else if (request.getHierarchyType() == HierarchyType.REPLY_FOR_ORIGIN) {
            updateEntityWhenHierarchyTypeIsReplyForOrigin(entity, request);
        } else if (request.getHierarchyType() == HierarchyType.REPLY_FOR_REPLY) {
            updateEntityWhenHierarchyTypeIsReplyForReply(entity, request);
        } else {
            throw new BadRequestException(CustomExceptionType.INVALID_HIERARCHY_TYPE);
        }

        return createResponse(entity);
    }

    protected <T extends HierarchyRequestAble> void validateHook(T request) {}

    private <T extends HierarchyRequestAble, E> E saveEntity(T request, Function<T, E> function) {
        return function.apply(request);
    }

    protected abstract <T extends HierarchyRequestAble> void updateEntityWhenHierarchyTypeIsOrigin(E entity, T request);

    protected abstract <T extends HierarchyRequestAble> void updateEntityWhenHierarchyTypeIsReplyForOrigin(E entity, T request);

    protected abstract <T extends HierarchyRequestAble> void updateEntityWhenHierarchyTypeIsReplyForReply(E entity, T request);

    protected abstract <R> R createResponse(E entity);

}
