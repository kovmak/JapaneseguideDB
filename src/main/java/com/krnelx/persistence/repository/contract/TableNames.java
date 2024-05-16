package com.krnelx.persistence.repository.contract;

public enum TableNames {
    CLIENT("client"),
    PERSON("person"),
    EVENTS("events"),
    DESCRIPTION("description"),
    USER("users");
    private final String name;

    TableNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
