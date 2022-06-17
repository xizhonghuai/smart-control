package com.smart.enums;

import lombok.Getter;

@Getter
public enum SystemRoleTypeEnum {

    SUPER_ADMIN(1L, "ADMIN"),

    AGENT(2L, "AGENT"),

    USER(3L, "USER");

    private final Long id;
    private final String name;

    SystemRoleTypeEnum(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
