package com.krnelx.persistence.entity;

import java.util.UUID;

public record Client(UUID id,
                     String name,
                     String phone,
                     String address) implements Entity, Comparable<Client> {

    @Override
    public int compareTo(Client o) {
        return this.name.compareTo(o.name);
    }
}
