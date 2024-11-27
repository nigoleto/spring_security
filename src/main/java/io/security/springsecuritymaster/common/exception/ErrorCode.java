package io.security.springsecuritymaster.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(404, "user not found"),
    USER_DEACTIVATED(400, "user deactivated"),
    USER_SUSPENDED(400, "user suspended"),
    DUPLICATE_EMAIL(409, "Email is already in use"),
    DUPLICATE_NICKNAME(409, "Nickname is already in use"),
    INVALID_PASSWORD(400, "invalid password"),
    CLOTHES_NOT_FOUND(404, "clothes not found"),
    COMMENT_NOT_FOUND(404, "comment not found"),
    ALREADY_DELETED(400, "Already deleted entity"),
    ALREADY_LIKED(400, "Already liked post");

    private final int status;
    private final String message;
}
