package com.krnelx.persistence.repository.contract;

import com.krnelx.persistence.entity.Description;
import com.krnelx.persistence.repository.Repository;
import java.util.Set;
import java.util.UUID;

public interface DescriptionRepository extends Repository<Description>, ManyToMany {

    Set<Description> findAllDescriptionsCategory(UUID categoryId);

}