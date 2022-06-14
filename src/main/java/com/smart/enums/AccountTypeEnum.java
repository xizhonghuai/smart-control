package com.smart.enums;

import lombok.Getter;

@Getter
public enum AccountTypeEnum {

    SUPER_ADMIN(1, "SUPER_ADMIN"),

    AGENT(2, "AGENT"),

    USER(3, "USER");

    private final Integer id;
    private final String name;

    AccountTypeEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
