package com.krnelx.persistence.entity;

import java.util.UUID;

public record Events(UUID id,
                     String name,
                     String description) implements Entity, Comparable<Events> {

    // Конструктор з двома аргументами для id та name
    public Events(UUID id, String name) {
        this(id, name, ""); // Додатковий параметр description ініціалізується пустим рядком
    }

    @Override
    public int compareTo(Events o) {
        return this.name.compareTo(o.name);
    }

    // Метод доступу до поля 'name'
    public String getName() {
        return name;
    }

    // Метод доступу до поля 'description'
    public String getDescription() {
        return description;
    }

    // Метод доступу до поля 'id'
    public UUID getId() {
        return id;
    }
}
