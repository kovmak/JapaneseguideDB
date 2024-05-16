package com.krnelx.persistence.repository.contract;

import java.util.UUID;

public interface OneToMany {
    boolean attach(UUID parentId, UUID childId);

    boolean detach(UUID parentId, UUID childId);
}
