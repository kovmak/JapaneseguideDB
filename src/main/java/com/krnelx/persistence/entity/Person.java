package com.krnelx.persistence.entity;

import java.util.UUID;

public record Person(UUID id,
                     String name,
                     String address) implements Entity, Comparable<Person> {

    // Конструктор з двома аргументами для id та name
    public Person(UUID id, String name) {
        this(id, name, ""); // Додатковий параметр address ініціалізується пустим рядком
    }

    @Override
    public int compareTo(Person o) {
        return this.name.compareTo(o.name);
    }

    // Метод доступу до поля 'name'
    public String getName() {
        return name;
    }

    // Метод доступу до поля 'address'
    public String getAddress() {
        return address;
    }

    // Метод доступу до поля 'id'
    public UUID getId() {
        return id;
    }
}
