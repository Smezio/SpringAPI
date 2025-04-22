package com.example.spring_boot_esercizio.model;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class EntityService {

    @Autowired
    private EntityRepository entityRepository;

    /**
     * 
     * Get all entities
     * 
     * @return List of all entities
     */
    public List<Entity> getAll() {
        List<Entity> res = entityRepository.getAll();
        return res;
    }

    /**
     * 
     * Save one or more entities
     * 
     * @param entities
     * @return List of all entities
     * @throws Exception 
     */
    public List<Entity> saveMany(List<Entity> entities) throws Exception {
        List<Entity> res = entityRepository.saveMany(entities);

        if(res.size() < 1)
            throw new Exception("No entity has been created");

        return entityRepository.getAll();
    }

    /**
     * 
     * Update the Entity value and its parents and children proportionally
     * 
     * @param entity
     * @return List of all entities
     * @throws Exception Invalid id has been received
     */
    public List<Entity> updateOne(Entity entity) throws Exception {
        Optional<Entity> res = entityRepository.updateOne(entity);

        if(res.isEmpty())
            throw new Exception("No entity has been updated");

        return entityRepository.getAll();
    }
}
