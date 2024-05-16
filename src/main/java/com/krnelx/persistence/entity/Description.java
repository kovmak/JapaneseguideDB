package com.krnelx.persistence.entity;

import java.util.UUID;

public record Description(UUID id,
                          String name,
                          String description,
                          UUID categoryId) implements Entity, Comparable<Description> {

    // Конструктор з двома аргументами для id та name
    public Description(UUID id, String name) {
        this(id, name, "", null); // Додаткові поля description та categoryId ініціалізуються значеннями за замовчуванням
    }

    // Конструктор з трьома аргументами для id, name та description
    public Description(UUID id, String name, String description) {
        this(id, name, description, null); // categoryId ініціалізується значенням за замовчуванням
    }

    @Override
    public int compareTo(Description description) {
        return this.name.compareTo(description.name);
    }

    // Метод доступу до поля 'name'
    public String getName() {
        return name;
    }

    // Метод доступу до поля 'description'
    public String getDescriptions() {
        return description;
    }
}
